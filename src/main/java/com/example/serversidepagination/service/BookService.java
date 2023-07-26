package com.example.serversidepagination.service;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.entity.Book;
import org.springframework.data.domain.Page;

public interface BookService {

    Page<Book> listAll(int pageNum, int pageSize, String sortField, String sortDir);

    Book findById(Long id);

    void save(Book book);
    void update(Long id, Book book);

    void deleteById(Long id);
}
