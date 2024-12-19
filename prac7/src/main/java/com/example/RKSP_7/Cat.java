package com.example.RKSP_7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("cats")
public class Cat {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("color")
    private String color;
    @Column("age")
    private Integer age;
    @Column("breed")
    private String breed;
}