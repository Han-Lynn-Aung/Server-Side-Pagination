package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Book;
import com.example.serversidepagination.repo.AuthorRepo;
import com.example.serversidepagination.repo.BookRepo;
import com.example.serversidepagination.utils.BookSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookApiController {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;

    public BookApiController(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> listBooks(
            @RequestParam(name = "draw", defaultValue = "0") int draw,
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,
            @RequestParam(name = "search[value]", required = false) String searchValue,
            @RequestParam(name = "order[0][column]", defaultValue = "0") String sortColumnIndex,
            @RequestParam(name = "order[0][dir]", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "titleFilter", required = false) String titleFilter,
            @RequestParam(name = "genreFilter", required = false) String genreFilter,
            @RequestParam(name = "authorFilter", required = false) String authorFilter
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

        // Prepare Specifications for filtering
        Specification<Book> titleSpec = BookSpecifications.searchBooksByTitle(titleFilter);
        Specification<Book> genreSpec = BookSpecifications.searchBooksByGenre(genreFilter);
        Specification<Book> authorSpec = BookSpecifications.searchBooksByAuthor(authorFilter);

        // Combine all specifications using AND or OR as needed
        Specification<Book> combinedSpec = Specification.where(titleSpec)
                .and(genreSpec)
                .and(authorSpec);

        Page<Book> booksPage;
        if (searchValue != null && !searchValue.isEmpty()) {
            Specification<Book> searchSpec = BookSpecifications.searchBooks(searchValue);
            // Combine searchSpec with other filters using AND
            combinedSpec = Specification.where(combinedSpec).and(searchSpec);
        }

        booksPage = bookRepo.findAll(combinedSpec, pageable);

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

        String[] sortColumns = {"id", "title", "genre", "publishedDate", "author.name"};
        String sortColumn = sortColumns[sortColumnIndex];
        return Sort.by(direction, sortColumn);
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> getFilterValues(
            @RequestParam(name = "filter") String filter
    ) {
        List<String> filterValues;
        if ("titleFilter".equalsIgnoreCase(filter)) {
            filterValues = bookRepo.getAllBookTitles();
        } else if ("genreFilter".equalsIgnoreCase(filter)) {
            filterValues = bookRepo.getAllGenres();
        } else if ("authorFilter".equalsIgnoreCase(filter)) {
            filterValues = authorRepo.getAllAuthors(); // Assuming Author has a 'name' field
        } else {
            filterValues = Collections.emptyList();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", filterValues);
        return ResponseEntity.ok(response);
    }
}
