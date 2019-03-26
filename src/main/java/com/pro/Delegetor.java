package com.pro;

import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Delegetor implements InvocationHandler {
    private JdbcTemplate template;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println(template);
        //
        if(method.getName().contains("set"))
        isChanged = true;
        return method.invoke(o, objects);
    }
}
