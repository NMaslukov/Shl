package generator;

import com.pro.resources.annotations.Column;
import com.pro.resources.annotations.Table;
import com.pro.resources.annotations.Type;

import java.lang.reflect.Field;
import java.util.Set;

public class DDLgenerator {
    public static void process(Set<Class<?>> entities){
        final StringBuilder fullDDL = new StringBuilder();
        entities.forEach(e-> {
            generateTableDDL(e, fullDDL);
        });
        System.out.println(fullDDL.toString());
    }

    private static void generateTableDDL(Class<?> e, StringBuilder fullDDL){
        final StringBuilder tableDDL = new StringBuilder("create table " + e.getAnnotation(Table.class).name() +" if not exists(\n");

        for (Field declaredField : e.getDeclaredFields()) {
            Column definition = declaredField.getAnnotation(Column.class);
            tableDDL.append(definition.name() + " " + typeToString(definition.type()) + ",\n");
        }

        tableDDL.append(");\n").replace(tableDDL.lastIndexOf(","), tableDDL.lastIndexOf(",") + 1, "");
        fullDDL.append(tableDDL.toString());
    }

    private static String typeToString(Type definition) {
        return  definition.dataType() + (definition.lenght() != -1 ? "(" + definition.lenght() + ")" : "");
    }
}
