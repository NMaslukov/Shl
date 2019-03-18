package com.pro;

import com.pro.resource.annotations.CrutchEntityScan;
import com.pro.services.CrutchNate;
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

    private static final String CLASS_DELIMITER = "$";

    @Bean
    @SneakyThrows
    public Object crutchNateInitializer(ApplicationContext context, DataSource dataSource) {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(CrutchEntityScan.class);
        initCrutchNate((AnnotationConfigApplicationContext) context, dataSource, beansWithAnnotation);
        throw new RuntimeException("Annotation CrutchEntityScan not found");
    }

    private void initCrutchNate(AnnotationConfigApplicationContext context, DataSource dataSource, Map<String, Object> beansWithAnnotation) throws ClassNotFoundException {
        String basePackage = extractBasePackage(context, beansWithAnnotation);
        log.info("Scanning " + basePackage);
        CrutchNate.run(basePackage, dataSource);
    }

    private String extractBasePackage(AnnotationConfigApplicationContext context, Map<String, Object> beansWithAnnotation) throws ClassNotFoundException {
        Map.Entry<String, Object> entry = beansWithAnnotation.entrySet().stream().findFirst().orElseThrow(() -> new RuntimeException("Annotation CrutchEntityScan not found!"));
        BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition(entry.getKey());
        String rawBeanClassName = beanDefinition.getBeanClassName();
        String beanClassName = rawBeanClassName.substring(0, rawBeanClassName.indexOf(CLASS_DELIMITER));
        return Class.forName(beanClassName).getAnnotation(CrutchEntityScan.class).basePackage();
    }
}

