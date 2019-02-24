package generator;

import com.pro.resource.annotations.Column;
import com.pro.resource.annotations.Table;
import com.pro.resource.annotations.Type;
import com.pro.utils.Writer;

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

    public static void process(Set<Class<?>> entities){
        final StringBuilder fullDDL = new StringBuilder("DROP DATABASE IF EXISTS test;\n" +
                "CREATE DATABASE test;\n" +
                "use test;\n\n");
        Map<String, String> entityDDlMap = new LinkedHashMap<>();
        entities.forEach(e-> {
            appendEntityDDL(e, entityDDlMap);
        });
        concatByOrder(entityDDlMap, fullDDL); //TODO add depends on into map for ordering concationation
        Writer.overwriteTo("src/main/resources/schema.sql" ,fullDDL.toString());
    }

    private static void concatByOrder(Map<String, String> entityDDlMap, StringBuilder fullDDL) {

    }

    private static void appendEntityDDL(Class<?> entity, Map<String, String> entityDDlMap){
        String header = CREATE_TABLE_IF_NOT_EXISTS + entity.getAnnotation(Table.class).name() + " (" + DELIMITER;
        final StringBuilder tableDDL = new StringBuilder(header);
        List<String> constraints = new LinkedList<>();

        for (int i = 0; i < entity.getDeclaredFields().length; i++) {
            Column columnDefinition = entity.getDeclaredFields()[i].getAnnotation(Column.class);
            appendFieldDDL(entity, columnDefinition, tableDDL);
            storeKeyConstraint(columnDefinition, constraints);
        }
        appendKeyConstraints(constraints, tableDDL);
        normilize(tableDDL);
        entityDDlMap.put(entity.getName(), tableDDL.toString());
    }

    private static void normilize(StringBuilder tableDDL) {
        tableDDL.append(");" + DELIMITER);
        tableDDL.replace(tableDDL.lastIndexOf(","), tableDDL.lastIndexOf(",") + 1, "");
    }

    private static void appendFieldDDL(Class<?> e, Column columnDefinition, StringBuilder tableDDL) {
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
