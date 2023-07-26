package com.example.serversidepagination.service;

import com.example.serversidepagination.entity.Author;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthor();
    void saveAuthor(Author author);
    Author getAuthorById(long id);
    void deleteAuthorById(long id);
    Page<Author> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}

