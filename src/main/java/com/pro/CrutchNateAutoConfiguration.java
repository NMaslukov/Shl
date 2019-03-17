package com.pro;

import com.pro.resource.annotations.CrutchEntityScan;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@Log4j2
public class CrutchNateAutoConfiguration {

    @Bean
    @SneakyThrows
    public CrutchNateInitializer crutchNateInitializer(ApplicationContext context, DataSource dataSource){
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(CrutchEntityScan.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) context).getBeanFactory().getBeanDefinition(entry.getKey());
            String rawBeanClassName = beanDefinition.getBeanClassName();
            String beanClassName = rawBeanClassName.substring(0, rawBeanClassName.indexOf("$"));
            String basePackage = Class.forName(beanClassName).getAnnotation(CrutchEntityScan.class).basePackage();
            log.info("Scanning " + basePackage);
            return new CrutchNateInitializer(basePackage, dataSource);
        }
        throw new RuntimeException("Annonation CrutchEntityScan not found");
    }
}
