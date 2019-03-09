package com.pro.cruthnate.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ParsedEntity {
    private List<String> dependsOn;
    private String ddl;

    public ParsedEntity() {
        dependsOn = new LinkedList<>();
    }
}
