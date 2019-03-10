package com.pro.utils.generator;

import com.pro.cruthnate.dto.ParsedEntity;
import com.pro.resource.annotations.Column;
import com.pro.resource.annotations.ForeignKey;
import com.pro.resource.annotations.Table;
import com.pro.resource.annotations.Type;
import com.pro.utils.Writer;

import java.lang.reflect.Field;
import java.util.*;

public class DDLgenerator {

    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String AUTO_INCREMENT = "AUTO_INCREMENT";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = "REFERENCES";
    private static final String DELIMITER = "\n";
    private static final String NOT_NULL = "NOT NULL";
    private static final String UNIQUE = "UNIQUE";
    private static final String schema_name = "test";
    private static final String DROP_DATABASE_IF_EXISTS = "DROP DATABASE IF EXISTS";
    private static final String CREATE_DATABASE = "CREATE DATABASE";
    private static final String USE = "use";
    private static final StringBuilder resultDDl = new StringBuilder();
    private static final List<String> createdEntities = new LinkedList<>();
    private static final String pathToSaveSQL = "src/main/resources/schema.sql";


    public static String process(Set<Class<?>> entities){
        Map<String, ParsedEntity> parsedEntityMap = new LinkedHashMap<>();
        entities.forEach(e-> {
            fillParsedEntityMap(e, parsedEntityMap);
        });

        concatByOrder(parsedEntityMap);
        Writer.overwriteTo(pathToSaveSQL, resultDDl.toString());
        return resultDDl.toString();
    }

    private static void concatByOrder(Map<String, ParsedEntity> entityDDlMap) {
        while (entityDDlMap.size() > 0){
            orderingAppending(entityDDlMap, entityDDlMap.entrySet().stream().findFirst().get());
        }
    }

    private static void orderingAppending(Map<String, ParsedEntity> entityDDlMap, Map.Entry<String, ParsedEntity> entry) {
        List<String> dependsOn = entry.getValue().getDependsOn();
        dependsOn.removeAll(createdEntities);
        if(dependsOn.size() == 0){
            resultDDl.append(entry.getValue().getDdl());
            entityDDlMap.remove(entry.getKey());
            createdEntities.add(entry.getKey());
            return;
        }
        for (String dependency : dependsOn) {
            orderingAppending(entityDDlMap, entityDDlMap.entrySet().stream().filter(e -> e.getKey().equals(dependency)).findFirst().get());
        }
    }

    private static void fillParsedEntityMap(Class<?> entity, Map<String, ParsedEntity> entityDDlMap){
        String header = CREATE_TABLE_IF_NOT_EXISTS + entity.getAnnotation(Table.class).name() + " (" + DELIMITER;
        final StringBuilder tableDDL = new StringBuilder(header);
        List<String> constraints = new LinkedList<>();

        for (int i = 0; i < entity.getDeclaredFields().length; i++) {
            Column columnDefinition = entity.getDeclaredFields()[i].getAnnotation(Column.class);
            if(columnDefinition == null) throw new RuntimeException("Column annotation can not be null!");

            appendFieldDDL(columnDefinition, tableDDL);
            storeKeyConstraint(columnDefinition, constraints);
        }
        appendKeyConstraints(constraints, tableDDL);
        normalize(tableDDL);

        ParsedEntity parsedEntity = new ParsedEntity();
        parsedEntity.setDdl(tableDDL.toString());
        fillDependsOn(entity, parsedEntity);
        entityDDlMap.put(entity.getAnnotation(Table.class).name(), parsedEntity);
    }

    private static void fillDependsOn(Class<?> entity, ParsedEntity parsedEntity) {
        for (Field declaredField : entity.getDeclaredFields()) {
            for (ForeignKey foreignKey : declaredField.getAnnotation(Column.class).foreignKey()) {
                parsedEntity.getDependsOn().add(((Table)foreignKey.targetEntity().getAnnotation(Table.class)).name());
            }
        }
    }

    private static void normalize(StringBuilder tableDDL) {
        tableDDL.append(");" + DELIMITER);
        tableDDL.replace(tableDDL.lastIndexOf(","), tableDDL.lastIndexOf(",") + 1, "");
    }

    public static void appendFieldDDL(Column columnDefinition, StringBuilder tableDDL) {
        tableDDL.append(columnDefinition.name()).append(" ").append(typeToString(columnDefinition.type()[0])).append(addOtherConstraints(columnDefinition))
                .append(columnDefinition.autoIncrement()? "   " + AUTO_INCREMENT : "").append("," + DELIMITER);
    }

    private static void appendKeyConstraints(List<String> constraints, StringBuilder tableDDL) {
        for (String constraint : constraints) {
            tableDDL.append(constraint).append("," + DELIMITER);
        }
    }

    private static void storeKeyConstraint(Column columnDefinition, List<String> constraints) {
        if(columnDefinition.primaryKey()){
            constraints.add(PRIMARY_KEY + " (" + columnDefinition.name() + ")");
        }

        if(columnDefinition.foreignKey().length > 0 ){
            String targetEntityName = ((Table) columnDefinition.foreignKey()[0]
                    .targetEntity().getAnnotation(Table.class)).name();
            constraints.add(FOREIGN_KEY + " (" + columnDefinition.name() + ")" + " " + REFERENCES + " " + targetEntityName
                    + "(" + columnDefinition.foreignKey()[0].filedName() + ")");
        }
    }

    private static String addOtherConstraints(Column definition) {
        return definition.notNull()? "  " + NOT_NULL : "" + (definition.unique()? " " + UNIQUE : "");
    }

    private static String typeToString(Type definition) {
        return  definition.dataType() + (definition.lenght() != -1 ? "(" + definition.lenght() + ")" : "");
    }

}
