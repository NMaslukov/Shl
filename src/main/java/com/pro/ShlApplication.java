package com.pro;

import com.pro.jdbc.WhoDAO;
import com.pro.services.CrutchNate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"com.pro"})
@EnableScheduling
public class ShlApplication extends SpringBootServletInitializer {

    @Autowired
    private DataSource dataSource;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShlApplication.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ShlApplication.class, args);
        WhoDAO bean = run.getBean(WhoDAO.class);
        System.out.println(bean);
    }

    @Bean
    public DataSource masterDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        //todo
        return hikariDataSource;
    }

    @PostConstruct
    public void setUp(){
        String basePackage = "com.pro.entity";
        CrutchNate.run(basePackage, dataSource);
    }

}
