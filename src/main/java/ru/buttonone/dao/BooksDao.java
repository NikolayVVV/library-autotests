package ru.buttonone.dao;

import ru.buttonone.domain.Book;

import java.util.List;

public interface BooksDao {
    List<Book> getBooksByTitle(String titleOfBook);

    void deleteBookByTitle(String titleOfBook);
}
