package com.pro.jdbc;

import com.pro.resource.annotations.Column;
import com.pro.resource.annotations.Table;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> implements Repository<T> {

    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = " VALUES";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String tableName;
    private final Class entityClass;

    @SneakyThrows
    public AbstractRepository(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        ParameterizedTypeImpl superClass = (ParameterizedTypeImpl) this.getClass().getGenericSuperclass();
        this.entityClass = Class.forName(superClass.getActualTypeArguments()[0].getTypeName());
        
        tableName = ((Table)(entityClass.getAnnotation(Table.class))).name();
    }

    @Override
    public void save(T entity) {

        Map<String, Object> entityParamMap;
        try {
            entityParamMap = getEntityParamMap(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String names = "(" + String.join(",", entityParamMap.keySet()) + ") ";
        String values = "(" + entityParamMap.entrySet().stream().map(this::getStringQueryValue).collect(Collectors.joining(",")) + ")";

        jdbcTemplate.update(INSERT_INTO + tableName + names + VALUES + values, Collections.emptyMap());
    }

    private String getStringQueryValue(Map.Entry<String, Object> e) {
        Object value = e.getValue();

        if(value instanceof String || value instanceof Enum){
            return "'" + value.toString() + "'";
        }

        return value.toString();
    }

    /*
    ** This method will properly works only for entities without super class and primary types!
     */
    @Override
    @SneakyThrows
    public List<T> findByParameters(T entity) {
        Map<String, Object> params = getEntityParamMap(entity);
        if(params.size() == 0) throw new RuntimeException("Params size 0");

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (Map.Entry<String, Object> e: params.entrySet()) {
            query.append(e.getKey()).append(" = :").append(e.getKey()).append(" AND ");
        }
        query.replace(query.lastIndexOf("AND"), query.length(), "");

        paramsToString(params);
        printFullQuery(params, query);
        try {
            return jdbcTemplate.query(query.toString(), params, mapper());
        } catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    private void printFullQuery(Map<String, Object> params, StringBuilder query) {
        String resultQuery = query.toString();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String forReplace = ":" + entry.getKey();
            resultQuery = resultQuery.replace(forReplace, entry.getValue().toString());
        }
        System.out.println(resultQuery);
    }

    private void paramsToString(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            entry.setValue(entry.getValue().toString());
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
