package com.example.Ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")

    private Long id ;

    @NotBlank(message = "Name of category is require")
    private String name;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;


    @ManyToOne
    @JoinColumn(name = "Parent_id")
    @JsonIgnore

    private Category parentCategory ;
    @OneToMany(mappedBy = "parentCategory",fetch = FetchType.LAZY)
    private List<Category> subCategories;

}
