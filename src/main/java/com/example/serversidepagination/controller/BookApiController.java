package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Book;
import com.example.serversidepagination.repo.BookRepo;
import com.example.serversidepagination.utils.BookSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BookApiController {
    private final BookRepo bookRepo;

    public BookApiController(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/api/books")
    public ResponseEntity<Map<String, Object>> listBooks(
            @RequestParam(name = "draw", defaultValue = "0") int draw,
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,
            @RequestParam(name = "search[value]", required = false) String searchValue,
            @RequestParam(name = "order[0][column]", defaultValue = "0") String sortColumnIndex,
            @RequestParam(name = "order[0][dir]", defaultValue = "asc") String sortDirection
    ) {
        // Convert sortColumnIndex to integer safely
        int columnIdx;
        try {
            columnIdx = Integer.parseInt(sortColumnIndex);
        } catch (NumberFormatException e) {
            columnIdx = 0; // Default to the first column if parsing fails
        }

        // Create Pageable with sort based on the requested column and direction
        Pageable pageable = PageRequest.of(start / length, length, getSort(columnIdx, sortDirection));

        Page<Book> booksPage;
        if (searchValue != null && !searchValue.isEmpty()) {
            Specification<Book> searchSpec = BookSpecifications.searchBooks(searchValue);
            booksPage = bookRepo.findAll(searchSpec, pageable);
        } else {
            booksPage = bookRepo.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", booksPage.getTotalElements());
        response.put("recordsFiltered", booksPage.getTotalElements());
        response.put("data", booksPage.getContent());

        return ResponseEntity.ok(response);
    }

    private Sort getSort(int sortColumnIndex, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        String[] sortColumns = {"id", "title", "genre", "publishedDate", "author.name" };
        String sortColumn = sortColumns[sortColumnIndex];

        return Sort.by(direction, sortColumn);
    }
}
