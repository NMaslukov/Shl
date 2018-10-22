package com.pro.configs.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class AspectTest {

//    @Before(value = "execution(* com.pro.jdbc.WhoDAO.t(..))")
    public final void before(JoinPoint joinPoint){
        System.out.println("Before test");
    }
}
