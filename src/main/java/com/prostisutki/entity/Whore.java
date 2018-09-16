package com.prostisutki.entity;

import com.prostisutki.resources.annotations.Column;
import com.prostisutki.resources.annotations.Id;
import com.prostisutki.resources.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "whores")
public class Whore {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
