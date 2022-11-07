package ru.buttonone.dao;

import ru.buttonone.domain.Genre;
import ru.buttonone.exception.GenreNotFoundException;
import ru.buttonone.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenresDaoImpl implements GenresDao {
    private final Props props = new Props();

    @Override
    public List<Genre> getGenresByName(String nameOfGenres) {
        String insertSQL = "select name from genres where name = ?";
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(insertSQL);
            preparedStatement.setString(1, nameOfGenres);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Genre> list = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                list.add(new Genre(name));
            }
            return list;
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        throw new GenreNotFoundException("Genre not found");
    }
}
