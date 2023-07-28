package com.example.serversidepagination.utils;

import com.example.serversidepagination.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<Book> searchBooksByTitle(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String likePattern = "%" + title.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
        });
    }

    public static Specification<Book> searchBooksByGenre(String genre) {
        return ((root, query, criteriaBuilder) -> {
            if (genre == null || genre.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String likePattern = "%" + genre.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")), likePattern);
        });
    }

    public static Specification<Book> searchBooksByAuthor(String author) {
        return ((root, query, criteriaBuilder) -> {
            if (author == null || author.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String likePattern = "%" + author.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("name")), likePattern);
        });
    }

    public static Specification<Book> searchBooks(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("name")), likePattern)
            );
        });
    }
}
