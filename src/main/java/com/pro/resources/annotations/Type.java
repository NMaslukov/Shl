package com.pro.resources.annotations;

import com.pro.resources.annotations.enums.DataTypes;

public @interface Type {
    DataTypes dataType();
    int lenght() default -1;
}
