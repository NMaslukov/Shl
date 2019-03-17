package com.pro;

import com.pro.services.CrutchNate;

import javax.sql.DataSource;

public class CrutchNateInitializer {

    public CrutchNateInitializer(String basePackage, DataSource dataSource) {
        CrutchNate.run(basePackage, dataSource);
    }

}
