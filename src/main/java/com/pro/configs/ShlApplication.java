package com.pro.configs;

import com.pro.resources.annotations.Table;
import com.zaxxer.hikari.HikariDataSource;
import generator.DDLgenerator;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Set;

@SpringBootApplication(scanBasePackages = "com.pro")
public class ShlApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShlApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ShlApplication.class, args);
    }

    @Bean
    public DataSource masterDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/prositutki");
        //todo
        return hikariDataSource;
    }

    @PostConstruct
    public void setUp(){
        Reflections reflections = new Reflections("com.pro.entity");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Table.class);
        DDLgenerator.process(typesAnnotatedWith);
    }
}
