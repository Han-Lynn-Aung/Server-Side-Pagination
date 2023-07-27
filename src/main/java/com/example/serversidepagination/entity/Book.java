package com.example.serversidepagination.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required!")
    private String title;

    @NotBlank(message = "Genre is required!")
    private String genre;

    @NotNull(message = "Published Date is required!")
    @Past(message = "Published Date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
