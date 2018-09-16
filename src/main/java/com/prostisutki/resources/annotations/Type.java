package com.prostisutki.resources.annotations;

import com.prostisutki.resources.annotations.enums.DataTypes;

public @interface Type {
    DataTypes dataType();
    int lenght() default -1;
}
