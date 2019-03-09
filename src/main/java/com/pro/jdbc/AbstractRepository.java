package com.pro.jdbc;

import com.pro.resource.annotations.Column;
import com.pro.resource.annotations.Table;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> {

    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = " VALUES";
    protected final JdbcTemplate jdbcTemplate;

    private final String tableName;

    public AbstractRepository(DataSource dataSource, Class entity){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        tableName = ((Table)(entity.getAnnotation(Table.class))).name();
    }

    public void save(T entity) {

        Map<String, Object> entityParamMap = null;
        try {
            entityParamMap = getEntityParamMap(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String names = "(" + String.join(",", entityParamMap.keySet()) + ") ";
        String values = "(" + entityParamMap.entrySet().stream().map(this::getStringValue).collect(Collectors.joining(",")) + ")";

        jdbcTemplate.execute(INSERT_INTO + tableName + names + VALUES + values);
    }

    private String getStringValue(Map.Entry<String, Object> e) {
        Object value = e.getValue();

        if(value instanceof String){
            return "'" + value + "'";
        }

        if(value instanceof Enum) {
            return String.valueOf(((Enum)value).ordinal());
        }

        return value.toString();
    }

    /*
    ** This method will properly works only for entities without super class and primary types!
     */
    @SneakyThrows
    public Optional<T> findByParameters(T entity) {
        if(entity.getClass().getSuperclass() != Object.class) throw new RuntimeException("Not Allowed for entity with superclass != Object");

        Map<String, Object> params = getEntityParamMap(entity);

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (Map.Entry<String, Object> e: params.entrySet()) {
            query.append(e.getKey()).append("=?").append(" AND ");
        }
        query.replace(query.lastIndexOf("AND"), query.length(), "");

        try {
            return (Optional<T>) Optional.of(jdbcTemplate.queryForObject(query.toString(), params.values().toArray(), new BeanPropertyRowMapper(entity.getClass())));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    private Map<String, Object> getEntityParamMap(T entity) throws IllegalAccessException {
        Map<String, Object> params = new LinkedHashMap<>();
        for(Field field : entity.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.get(entity) == null) continue;
            params.put(field.getAnnotation(Column.class).name(), field.get(entity));
        }
        return params;
    }

    public RowMapper rowMapper(Class aClass) { //TODO test
        return (resultSet, i) -> {
            try {
                Object o = aClass.newInstance();
                for (Field declaredField : o.getClass().getDeclaredFields()) {
                    Object value = resultSet.getObject(declaredField.getAnnotation(Column.class).name());
                    declaredField.set(o, value);
                }

                return o;
            } catch (Exception e){
                e.printStackTrace();
                return null;}
        };
    }
}
