package com.pro.services;

import com.pro.resource.annotations.Table;
import com.pro.utils.generator.DDLgenerator;
import com.pro.utils.validator.SchemaValidator;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Log4j2
public class CrutchNate {

    public static final String SCHEMA_SQL = "schema.sql";

    @SneakyThrows
    public static void run(String entityPackage, DataSource dataSource) {
        try {
            Set<Class<?>> entities = scanEntities(entityPackage);
            String resultDDl = DDLgenerator.process(entities);
            String[] split = resultDDl.split(";");
            split[split.length - 1] = null;
            new JdbcTemplate(dataSource).batchUpdate(split);
            String jdbcUrl = ((HikariDataSource) dataSource).getJdbcUrl();
            String schemaName = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
            SchemaValidator validator = new SchemaValidator(dataSource,  schemaName);
            validator.validate(entities);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void executeScript(DataSource dataSource) throws IOException, SQLException {
        ClassPathResource resource = new ClassPathResource(SCHEMA_SQL);
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
    }

    private static Set<Class<?>> scanEntities(String entityPackage) {
        Reflections reflections = new Reflections(entityPackage);
        return reflections.getTypesAnnotatedWith(Table.class);
    }
}
