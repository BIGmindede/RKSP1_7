package com.example.pract4client.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CatModel {
    private Long id;
    private String name;
    private Integer age;
    private String color;
    private String breed;
}
