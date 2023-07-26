package com.example.serversidepagination.controller;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.service.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @Autowired
    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model){
        return findPaginated(1,"name","asc",model);
    }

    @GetMapping("/showNewAuthor")
    public String showNewAuthorForm(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "author_form";
    }

    @PostMapping("/saveAuthor")
    public String saveEmployee(@ModelAttribute ("author") Author author){
        authorService.saveAuthor(author);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model){
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "update_author";
    }

    @GetMapping("/deleteAuthor/{id}")
    public String deleteAuthor(@PathVariable(value = "id") long id){
        authorService.deleteAuthorById(id);
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                            @RequestParam("sorDir") String sortDir, Model model){

        int pageSize = 10;

        Page<Author> page = authorService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Author> authors = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")? "desc" : "asc");

        model.addAttribute("authors", authors);
        return "list_author";

    }

}

