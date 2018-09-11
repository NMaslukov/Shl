package com.prostisutki.jdbc;

import com.prostisutki.entity.Whore;
import com.prostisutki.resources.annotations.Column;
import com.prostisutki.resources.annotations.Table;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRepository<T> {

    protected final JdbcTemplate jdbcTemplate;

    private final Class entity;
    private final String tableName;

    public AbstractRepository(DataSource dataSource, Class entity){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.entity = entity;
        tableName = ((Table)entity.getAnnotation(Table.class)).name();
    }

    /*
    ** This method will properly works only for entities without super class!
     */
    protected Optional<T> findByParameters(T obj) throws IllegalAccessException, ClassNotFoundException {
        if(obj.getClass().getSuperclass() != Object.class) throw new RuntimeException("Not Allowed for entity with superclass != Object");

        Map<String, Object> params = new LinkedHashMap<>();
        for(Field field : obj.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.get(obj) == null) continue;
            params.put(field.getAnnotation(Column.class).name(), field.get(obj));
        }
        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (Map.Entry<String, Object> e: params.entrySet()) {
            query.append(e.getKey() + "=? AND ");
        }
        query.replace(query.lastIndexOf("AND"), query.length(), "");
        try {
            return (Optional<T>) Optional.of(jdbcTemplate.queryForObject(query.toString(), params.values().toArray(), new BeanPropertyRowMapper(entity)));
        } catch (EmptyResultDataAccessException e){
            return Optional.ofNullable(null);
        }
    }
}
