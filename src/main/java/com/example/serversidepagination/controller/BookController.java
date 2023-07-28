package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.entity.Book;
import com.example.serversidepagination.repo.AuthorRepo;
import com.example.serversidepagination.repo.BookRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final BookRepo bookRepo;

    private final AuthorRepo authorRepo;

    public BookController(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/books/bookList")
    public String showBooks() {
        return "book-list";
    }

    @GetMapping("/books/newBook")
    public String showAuthorForm(Model model) {
        List<Author> authors = authorRepo.findAll();
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);
        return "book-form";
    }

    @PostMapping("/books/saveBook")
    public String saveBook(@Valid Book book, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Author> authors = authorRepo.findAll();
            result.getModel().put("authors", authors);
            return "edit-book";
        }

        // Check if the provided author ID is not null
        Long authorId = book.getAuthor().getId();
        Author author;
        if (authorId != null) {
            author = authorRepo.findById(authorId).orElse(null);
            if (author == null) {
                result.rejectValue("author.id", "error.book", "Author not Found!");
                return "edit-book";
            }
        } else {
            // If the author ID is null, it means no author is selected, so set the author to null
            author = null;
        }

        // Create a new book with the provided details
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setGenre(book.getGenre());
        newBook.setPublishedDate(book.getPublishedDate());
        newBook.setAuthor(author);

        bookRepo.save(newBook);
        redirectAttributes.addFlashAttribute("saveSuccess", true);
        return "redirect:/books/bookList";
    }


    @GetMapping("/books/editBook/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Book ID: " + id));
        List<Author> authors = authorRepo.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors); // Add the list of authors to the model
        return "edit-book";
    }

    @PostMapping("/books/editBook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            List<Author> authors = authorRepo.findAll();
            result.getModel().put("authors", authors);
            return "edit-book";
        }

        Book existingBook = bookRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid Book ID : " + id)
        );

        if (existingBook == null) {
            return "redirect:/books/bookList";
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setGenre(book.getGenre());
        existingBook.setPublishedDate(book.getPublishedDate());

        // Check if the provided author ID is not null
        Long authorId = book.getAuthor().getId();
        if (authorId != null) {
            Author updatedAuthor = authorRepo.findById(authorId).orElse(null);
            if (updatedAuthor == null) {
                result.rejectValue("author.id", "error.book", "Author not Found!");
            } else {
                existingBook.setAuthor(updatedAuthor);
            }
        } else {
            // If the author ID is null, it means no author is selected, so set the author to null
            existingBook.setAuthor(null);
        }

        bookRepo.save(existingBook);
        return "redirect:/books/bookList";
    }

    @GetMapping("/books/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            bookRepo.delete(book);
            redirectAttributes.addFlashAttribute("deleteSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("deleteError", true);
        }
        return "redirect:/books/bookList";
    }
}
