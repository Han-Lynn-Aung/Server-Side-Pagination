package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.repo.AuthorRepo;
import com.example.serversidepagination.utils.AuthorSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

@RestController
public class AuthorApiController {
    private final AuthorRepo authorRepo;

    public AuthorApiController(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @GetMapping("/api/authors")
    public ResponseEntity<Map<String, Object>> listAuthors(HttpServletRequest request) {
        int draw = Integer.parseInt(request.getParameter("draw"));
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        String searchValue = request.getParameter("search[value]");
        String sortColumn = request.getParameter("columns[" + request.getParameter("order[0][column]") + "][data]");
        String sortDirection = request.getParameter("order[0][dir]");

        Pageable pageable = PageRequest.of(start / length, length, getSort(sortColumn, sortDirection));

        Page<Author> authorsPage;
        if (searchValue != null && !searchValue.isEmpty()) {
            Specification<Author> searchSpec = AuthorSpecifications.searchAuthors(searchValue);
            authorsPage = authorRepo.findAll(searchSpec, pageable);
        } else {
            authorsPage = authorRepo.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", authorsPage.getTotalElements());
        response.put("recordsFiltered", authorsPage.getTotalElements());
        response.put("data", authorsPage.getContent());

        return ResponseEntity.ok(response);
    }

    private Sort getSort(String sortColumn, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, sortColumn);
    }
}


