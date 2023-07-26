package com.example.serversidepagination.service;

import com.example.serversidepagination.entity.Book;
import com.example.serversidepagination.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookServiceImpl {

        private final BookRepo bookRepo;

        @Autowired
        public BookServiceImpl(BookRepo bookRepo) {
            this.bookRepo = bookRepo;
        }

        public Page<Book> listAll(int pageNum, int pageSize, String sortField, String sortDir) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
            return bookRepo.findAll(pageable);
        }

        public Book findById(Long id) {
            return bookRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));
        }

        public void save(Book book) {
            bookRepo.save(book);
        }

        public void update(Long id, Book updatedBook) {
            Book book = findById(id);
            book.setTitle(updatedBook.getTitle());
            book.setGenre(updatedBook.getGenre());
            book.setPublishedDate(updatedBook.getPublishedDate());
            book.setAuthor(updatedBook.getAuthor());
            bookRepo.save(book);
        }

        public void deleteById(Long id) {
            bookRepo.deleteById(id);
        }
}
