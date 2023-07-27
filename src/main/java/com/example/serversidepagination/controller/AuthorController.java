package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.repo.AuthorRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class AuthorController {

    private final AuthorRepo authorRepo;

    public AuthorController(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @GetMapping("/authors/authorList")
    public String showAuthors() {
        return "author-list";
    }

    @GetMapping("/authors/newAuthor")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author-form";
    }

    @PostMapping("/authors/saveAuthor")
    public String saveAuthor(@Valid Author author, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("saveError", true);
            return "author-form";
        }
        Date currentDate = new Date();
        if (author.getDateOfBirth() != null && author.getDateOfBirth().after(currentDate)){
            redirectAttributes.addFlashAttribute("dateOfBirthError", true);
            return "author-form";
        }
        authorRepo.save(author);
        redirectAttributes.addFlashAttribute("saveSuccess", true);

        return "redirect:/authors/authorList";
    }

    @GetMapping("/authors/editAuthor/{id}")
    public String showEditAuthorForm(@PathVariable("id") Long id, Model model) {
        Author author = authorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + id));
        model.addAttribute("author", author);
        return "edit-author";
    }

    @PostMapping("/authors/editAuthor/{id}")
    public String updateAuthor(@PathVariable("id") Long id, @Valid Author author, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-author";
        }
        Author existingAuthor = authorRepo.getById(id);
        existingAuthor.setName(author.getName());
        existingAuthor.setDateOfBirth(author.getDateOfBirth());
        existingAuthor.setAddress(author.getAddress());

        authorRepo.save(existingAuthor);
        return "redirect:/authors/authorList";
    }

    @GetMapping("/authors/deleteAuthor/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        authorRepo.deleteById(id);
        return "redirect:/authors/authorList";
    }
}

