package ru.buttonone.dao;

import ru.buttonone.domain.Author;
import ru.buttonone.exception.AuthorNotFoundException;
import ru.buttonone.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorsDaoImpl implements AuthorsDao {
    private final Props props = new Props();

    @Override
    public List<Author> getAuthorsByFio(String fioFromAuthors) {
        String insertSQL = "select fio from authors where fio = ?";
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(insertSQL);
            preparedStatement.setString(1, fioFromAuthors);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Author> list = new ArrayList<>();
            while (resultSet.next()) {
                String fio = resultSet.getString("fio");
                list.add(new Author(fio));
            }
            return list;
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        throw new AuthorNotFoundException("Author not found");
    }
}
