package com.example.producttestapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CategoryModel {    private int id;

    private String name;
    private List<CategoryModel> categoriesModelList;

    public <E> CategoryModel(String name, ArrayList<E> es) {
    }
}
