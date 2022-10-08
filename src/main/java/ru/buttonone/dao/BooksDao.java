package ru.buttonone.dao;

import ru.buttonone.domain.Book;

import java.util.List;

public interface BooksDao {
    List<Book> getBooks(String titleOfBook);

    void addBooks(String title, int genreId);

    void deleteBooks(String title);

}
