package com.pro.resource.annotations;

public @interface ForeignKey {
    Class targetEntity();
    String filedName();
}
