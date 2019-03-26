package com.pro;

import com.pro.resource.annotations.CrutchEntityScan;
import com.pro.resource.annotations.CruthNateRepository;
import com.pro.services.CrutchNate;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Configuration
@Log4j2
public class CrutchNateAutoConfiguration {

    private static final String CLASS_DELIMITER = "$";

    @Bean
    @SneakyThrows
    public Object crutchNateInitializer(ApplicationContext context, DataSource dataSource) {
        Map<String, Object> entityScanAnnotationBeans = context.getBeansWithAnnotation(CrutchEntityScan.class);
        if(entityScanAnnotationBeans.size() == 0) throw new RuntimeException("Annotation CrutchEntityScan not found");
        initCrutchNate((AnnotationConfigApplicationContext) context, dataSource, entityScanAnnotationBeans);
        proxyRepositories(context, null);
        return new Object();
    }

    @SneakyThrows
    private void proxyRepositories(ApplicationContext context, Map<String, Object> repositoryScanAnnotationBeans) {
        Reflections reflections = new Reflections("com.example.demo.dao");
        for (Class<?> repo : reflections.getTypesAnnotatedWith(CruthNateRepository.class)) {
            Object proxyRepo = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Class.forName(repo.getName())}, new Delegetor(template));
            ((ConfigurableApplicationContext) context).getBeanFactory().registerSingleton(proxyRepo.getClass().getCanonicalName(), proxyRepo);


        }
    }

    private void initCrutchNate(AnnotationConfigApplicationContext context, DataSource dataSource, Map<String, Object> beansWithAnnotation) throws ClassNotFoundException {
        String basePackage = extractBasePackage(context, beansWithAnnotation);
        log.info("Scanning " + basePackage);
        CrutchNate.run(basePackage, dataSource);
    }

    private String extractBasePackage(AnnotationConfigApplicationContext context, Map<String, Object> entityScanAnnotationBeans) throws ClassNotFoundException {
        Map.Entry<String, Object> entry = entityScanAnnotationBeans.entrySet().stream().findFirst().orElseThrow(() -> new RuntimeException("Annotation CrutchEntityScan not found!"));
        BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition(entry.getKey());
        String rawBeanClassName = beanDefinition.getBeanClassName();
        String beanClassName = rawBeanClassName.substring(0, rawBeanClassName.indexOf(CLASS_DELIMITER));
        return Class.forName(beanClassName).getAnnotation(CrutchEntityScan.class).basePackage();
    }
}

