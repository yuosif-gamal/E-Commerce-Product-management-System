package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.util.pattern.PathPattern;

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

    private int id ;
    private String name;
    @OneToMany(mappedBy = "categoryID")
    @JsonIgnore
    private List<Product> products;


    @ManyToOne
    @JoinColumn(name = "Parent_id")
    @JsonIgnore

    private Category parentCategory ;
    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory",fetch = FetchType.LAZY)
    private List<Category> subCategories;

}
