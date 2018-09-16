package com.prostisutki.resources.annotations;

import com.prostisutki.resources.annotations.enums.DataTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
    Type type();
    boolean unique() default false;
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
    ForeignKey[] foreignKey() default {};
}
