package com.example.serversidepagination.utils;

import com.example.serversidepagination.entity.Author;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecifications {
    public static Specification<Author> searchAuthors(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String searchPattern = "%" + searchValue.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern)
                   /* criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), searchPattern)*/
            );
        };
    }
}

