package com.prostisutki.configs;

import com.prostisutki.resources.annotations.Table;
import com.zaxxer.hikari.HikariDataSource;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Set;

@SpringBootApplication(scanBasePackages = "com.prostisutki")
public class ShlyuxiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShlyuxiApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ShlyuxiApplication.class, args);
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
    public void t(){
        Reflections reflections = new Reflections("com.prostisutki.entity");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Table.class);
        typesAnnotatedWith.forEach(e -> System.out.println(e.getClass()));
        System.out.println(typesAnnotatedWith);
    }
}
