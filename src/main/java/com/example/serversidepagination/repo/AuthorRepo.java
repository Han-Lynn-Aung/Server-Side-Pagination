package com.example.serversidepagination.repo;

import com.example.serversidepagination.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    @Query("SELECT DISTINCT a.name FROM Author a")
    List<String> getAllAuthors();
}
