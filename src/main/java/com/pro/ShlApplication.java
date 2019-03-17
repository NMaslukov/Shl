package com.pro;

import com.pro.services.CrutchNate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"com.pro"})
@EnableScheduling
public class ShlApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ShlApplication.class, args);
    }

    @Bean
    public DataSource masterDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        return hikariDataSource;
    }

    @PostConstruct
    public void setUp(){
        String basePackage = "com.pro.entity";
        CrutchNate.run(basePackage, dataSource);
    }

}
