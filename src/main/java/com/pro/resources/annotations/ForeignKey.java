package com.pro.resources.annotations;

public @interface ForeignKey {
    Class targetEntity();
    String filedName();
}
