package ru.buttonone.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private String id;
    private String authors;
    private String genre;
    private String title;

}
