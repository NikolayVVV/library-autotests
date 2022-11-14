package ru.buttonone.dao;

import ru.buttonone.domain.Author;

import java.util.List;

public interface AuthorsDao {
    List<Author> getAuthorsByFio(String fio);
}
