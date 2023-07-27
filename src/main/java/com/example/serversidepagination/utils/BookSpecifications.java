package com.example.serversidepagination.utils;

import com.example.serversidepagination.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<Book> searchBooks(String keyword){
        return ((root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")),likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("name")),likePattern)
            );
        });
    }
}
