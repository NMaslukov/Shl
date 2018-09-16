package com.prostisutki.resources.annotations.enums;

import javax.xml.crypto.Data;

public enum DataTypes {
    VARCHAR, SHORT("short"), INT("int"), BYTE("byte"), BIG_DECIMAL;

    String stringVal;

    DataTypes(String sV){
        stringVal = sV;
    }

    DataTypes(){
        stringVal = super.toString();
    }

    @Override
    public String toString() {
        return stringVal;
    }
}
