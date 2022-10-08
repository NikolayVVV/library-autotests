package ru.buttonone.dao;

import ru.buttonone.exception.BookNotFoundException;
import ru.buttonone.domain.Book;
import ru.buttonone.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDaoImpl implements BooksDao {
    private Props props = new Props();
    private Book book;

    @Override
    public List<Book> getBooks(String titleOfBook) {
        String insertSQL = "select * from books where title = ?";
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(insertSQL);
            preparedStatement.setString(1, titleOfBook);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> list = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int genreId = resultSet.getInt("genre_id");
                list.add(new Book(id, title, genreId));
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new BookNotFoundException("Book not found");
    }

    @Override
    public void addBooks(String title, int genreId) {
        String insertSQL = "insert into books (title, genre_id) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(insertSQL);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, genreId);
            int insertRows = preparedStatement.executeUpdate();
            System.out.println("Book added");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void deleteBooks(String title) {
        String insertSQL = "delete from books where title  = ?";
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
        ) {

            PreparedStatement preparedStatement = connection
                    .prepareStatement(insertSQL);
            preparedStatement.setString(1, title);
            int insertRows = preparedStatement.executeUpdate();
            System.out.println("Book removed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
