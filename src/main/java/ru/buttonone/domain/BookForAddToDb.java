package ru.buttonone.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookForAddToDb {
    private String id;
    private String authors;
    private String genre;
    private String title;
}
