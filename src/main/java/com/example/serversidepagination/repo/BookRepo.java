package com.example.serversidepagination.repo;

import com.example.serversidepagination.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT DISTINCT b.title FROM Book b")
    List<String> getAllBookTitles();

    @Query("SELECT DISTINCT b.genre FROM Book b")
    List<String> getAllGenres();
}
