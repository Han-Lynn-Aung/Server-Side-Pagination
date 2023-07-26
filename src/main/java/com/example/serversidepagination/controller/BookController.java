package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.entity.Book;
import com.example.serversidepagination.service.AuthorServiceImpl;
import com.example.serversidepagination.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BookController {

    private final BookServiceImpl bookService;
    private final AuthorServiceImpl authorService;

    @Autowired
    public BookController(BookServiceImpl bookService, AuthorServiceImpl authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

}
