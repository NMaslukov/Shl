package com.pro.jdbc;

import com.pro.resource.annotations.Column;
import com.pro.resource.annotations.Table;
import lombok.SneakyThrows;
import org.apache.commons.lang.enums.EnumUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> {

    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = " VALUES";
    protected final NamedParameterJdbcTemplate jdbcTemplate;

    private final String tableName;
    private final Class entityClass;

    public AbstractRepository(DataSource dataSource, Class entityClass){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        tableName = ((Table)(entityClass.getAnnotation(Table.class))).name();
        this.entityClass = entityClass;
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

        jdbcTemplate.update(INSERT_INTO + tableName + names + VALUES + values, Collections.emptyMap());
    }

    private String getStringValue(Map.Entry<String, Object> e) {
        Object value = e.getValue();

        if(value instanceof String || value instanceof Enum){
            return "'" + value + "'";
        }

        return value.toString();
    }

    /*
    ** This method will properly works only for entities without super class and primary types!
     */
    @SneakyThrows
    public Optional<T> findByParameters(T entity) {
        Map<String, Object> params = getEntityParamMap(entity);

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (Map.Entry<String, Object> e: params.entrySet()) {
            query.append(e.getKey()).append("=?").append(" AND ");
        }
        query.replace(query.lastIndexOf("AND"), query.length(), "");

        try {
            return (Optional<T>) Optional.ofNullable(jdbcTemplate.queryForObject(query.toString(), params, new BeanPropertyRowMapper(entity.getClass())));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @SneakyThrows
    public int updateByParameters(T entity){
        Map<String, Object> params = getEntityParamMap(entity);
        StringBuilder query = new StringBuilder("UPDATE " + entity.getClass().getAnnotation(Table.class).name() + " SET ");

        for (Map.Entry<String, Object> paramsEntry : params.entrySet()) {
            query.append(paramsEntry.getKey() + " = :" + paramsEntry + ", ");
        }
        query.replace(query.lastIndexOf(","), query.length(), "");

        return jdbcTemplate.update(query.toString(), params);
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

    public RowMapper<T> mapper() {
        return (resultSet, i) -> {
            try {
                T newInstance = (T) entityClass.newInstance();
                for (Field declaredField : newInstance.getClass().getDeclaredFields()) {
                    Object value = resultSet.getObject(declaredField.getAnnotation(Column.class).name());
                    declaredField.setAccessible(true);
                    if(((Class)declaredField.getGenericType()).isEnum()){
                        setEnumValue(newInstance, declaredField, value);
                        continue;
                    }
                    ;
                    declaredField.set(newInstance, value);
                }
                return newInstance;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        };
    }

    private void setEnumValue(T newInstance, Field declaredField, Object value) throws ClassNotFoundException, IllegalAccessException {
        Iterator<?> iterator = Arrays.stream(Class.forName(declaredField.getGenericType().getTypeName()).getEnumConstants()).iterator();
        while (iterator.hasNext()){
            Enum next = (Enum) iterator.next();
            if(next.name().equals(String.valueOf(value))){
                declaredField.set(newInstance, next);
            }
        }
    }
}
