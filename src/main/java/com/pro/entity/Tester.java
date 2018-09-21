package com.pro.entity;

import com.pro.resources.Category;
import com.pro.resources.annotations.*;
import com.pro.resources.annotations.enums.DataTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tester")
public class Tester {

    @Id
    @Column(name = "id", type = @Type(dataType = DataTypes.INT), unique = true, notNull = true, primaryKey = true, autoIncrement = true)
    private Integer id;

    @Column(name = "dwadwa", type = @Type(dataType = DataTypes.VARCHAR, lenght = 50))
    private String name;

    @Column(name = "dawwda", type = @Type(dataType = DataTypes.TINY))
    private Category category;

}
