package com.pro.resource.annotations;

import com.pro.resource.annotations.enums.DataType;

public @interface Type {
    DataType dataType();
    int lenght() default -1;
}
