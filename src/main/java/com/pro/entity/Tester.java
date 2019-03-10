package com.pro.entity;

import com.pro.resource.Category;
import com.pro.resource.annotations.*;
import com.pro.resource.annotations.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tester")
@EqualsAndHashCode
public class Tester {

    @Id
    @Column(name = "id", type = @Type(dataType = DataType.INT), unique = true, notNull = true, primaryKey = true, autoIncrement = true)
    private Integer id;

    @Column(name = "dwadwa", type = @Type(dataType = DataType.VARCHAR, lenght = 50))
    private String name;

    @Column(name = "dawwda", type = @Type(dataType = DataType.VARCHAR, lenght = 20))
    private Category category;

    @Column(name = "lala", type = @Type(dataType = DataType.VARCHAR, lenght = 20))
    private Category lala;


}
