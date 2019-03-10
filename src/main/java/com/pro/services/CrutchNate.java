package com.pro.services;

import com.pro.resource.annotations.Table;
import com.pro.utils.generator.DDLgenerator;
import com.pro.utils.validator.SchemaValidator;
import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.util.Set;

public class CrutchNate {

    public static void run(String entityPackage, DataSource dataSource) {
        try {
            Set<Class<?>> entities = scanEntities(entityPackage);
            DDLgenerator.process(entities);
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("schema.sql"));

            SchemaValidator validator = new SchemaValidator(dataSource, "test");
            validator.validate(entities);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Set<Class<?>> scanEntities(String entityPackage) {
        Reflections reflections = new Reflections(entityPackage);
        return reflections.getTypesAnnotatedWith(Table.class);
    }
}
