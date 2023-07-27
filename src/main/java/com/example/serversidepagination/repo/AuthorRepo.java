package com.example.serversidepagination.repo;

import com.example.serversidepagination.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepo extends JpaRepository<Author,Long>, JpaSpecificationExecutor<Author> {
}
