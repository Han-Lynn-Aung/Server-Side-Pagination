package com.example.serversidepagination.service;

import com.example.serversidepagination.entity.Author;
import com.example.serversidepagination.repo.AuthorRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepo authorRepo;

    public AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public List<Author> getAllAuthor() {
        return authorRepo.findAll();
    }

    @Override
    public void saveAuthor(Author author) {
        authorRepo.save(author);
    }

    @Override
    public Author getAuthorById(long id) {
        Optional<Author> optional = authorRepo.findById(id);
        Author author = null;
        if (optional.isPresent()){
            author = optional.get();
        }else {
            throw new RuntimeException("Author not found for id :: " + id);
        }
        return author;
    }

    @Override
    public void deleteAuthorById(long id) {
        authorRepo.deleteById(id);
    }

    @Override
    public Page<Author> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable page = PageRequest.of(pageNo-1,pageSize,sort);

        return authorRepo.findAll(page);

    }
}

