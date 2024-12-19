package com.example.pract4client.DTOs;

import com.example.pract4client.models.CatModel;

import java.util.List;

public class CatListWrapper {
    private List<CatModel> cats;
    public List<CatModel> getCats() {
        return cats;
    }
}
