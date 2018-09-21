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
@Table(name = "who")
public class Who {

    @Id
    @Column(name = "id", type = @Type(dataType = DataTypes.INT), unique = true, primaryKey = true, autoIncrement = true,
    foreignKey = @ForeignKey(targetEntity = Tester.class, filedName = "example"))
    private Integer id;

    @Column(name = "name", type = @Type(dataType = DataTypes.VARCHAR, lenght = 50))
    private String name;

    @Column(name = "category", type = @Type(dataType = DataTypes.TINY))
    private Category category;

}
