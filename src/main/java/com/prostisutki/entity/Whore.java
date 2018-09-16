package com.prostisutki.entity;

import com.prostisutki.resources.annotations.*;
import com.prostisutki.resources.annotations.enums.DataTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "whores")
public class Whore {

    @Id
    @Column(name = "id", type = @Type(dataType = DataTypes.INT), unique = false, primaryKey = true, autoIncrement = true,
    foreignKey = @ForeignKey(targetEntity = Object.class, filedName = "example"))
    private Integer id;

    @Column(name = "name", type = @Type(dataType = DataTypes.VARCHAR, lenght = 50))
    private String name;
}
