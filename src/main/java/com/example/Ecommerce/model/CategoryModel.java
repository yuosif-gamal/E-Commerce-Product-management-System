package com.example.Ecommerce.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class CategoryModel {
    private Long id;

    private String name;
    private List<CategoryModel> categoriesModelList;

    public <E> CategoryModel(String name, ArrayList<E> es) {
    }
}
