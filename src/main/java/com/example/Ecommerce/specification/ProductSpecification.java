package com.example.Ecommerce.specification;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.filter.ProductFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductSpecification implements Specification<Product> {
    private final ProductFilter filter;

    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getMinPrice() != null && filter.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.between(root.get("price"), filter.getMinPrice(), filter.getMaxPrice()));
        }

        if (filter.getProductNameContain() != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getProductNameContain() + "%"));
        }

        if (filter.getMinQuantity() != 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), filter.getMinQuantity()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
