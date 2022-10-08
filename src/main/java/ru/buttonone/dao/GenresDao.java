package ru.buttonone.dao;

import ru.buttonone.domain.Genre;

import java.util.List;

public interface GenresDao {
    List<Genre> getGenresByName(String name);
}
