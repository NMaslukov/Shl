package com.pro.entity;

import com.pro.resource.Category;
import com.pro.resource.annotations.*;
import com.pro.resource.annotations.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "who")
public class Who {

    @Id
    @Column(name = "id", type = @Type(dataType = DataType.INT), unique = true, primaryKey = true, autoIncrement = true,
    foreignKey = @ForeignKey(targetEntity = Tester.class, filedName = "id"))
    private Integer id;

    @Column(name = "name", type = @Type(dataType = DataType.VARCHAR, lenght = 50))
    private String name;

    @Column(name = "category", type = @Type(dataType = DataType.TINY))
    private Category category;

}
