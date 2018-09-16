package com.prostisutki.resources.annotations;

import java.lang.reflect.Field;

public @interface ForeignKey {
    Class targetEntity();
    String filedName();
}
