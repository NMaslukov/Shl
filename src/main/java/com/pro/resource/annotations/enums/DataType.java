package com.pro.resource.annotations.enums;

public enum DataType {
    VARCHAR, SHORT("short"), INT("int"), BIG_DECIMAL("BIGDECIMAL"), TINY("TINYINT");

    String stringVal;

    DataType(String sV){
        stringVal = sV;
    }

    DataType(){
        stringVal = super.toString();
    }

    @Override
    public String toString() {
        return stringVal;
    }
}
