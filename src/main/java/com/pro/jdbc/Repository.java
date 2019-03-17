package com.pro.jdbc;

import lombok.SneakyThrows;

import java.util.List;

public interface Repository<T> {
    void save(T entity);

    /*
    ** This method will properly works only for entities without super class and primary types!
     */
    @SneakyThrows
    List<T> findByParameters(T entity);
}
