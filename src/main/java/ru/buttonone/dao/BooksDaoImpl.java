package ru.buttonone.dao;

import ru.buttonone.exception.BookNotFoundException;
import ru.buttonone.domain.Book;
import ru.buttonone.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDaoImpl implements BooksDao {
    private Props props = new Props();

    @Override
    public List<Book> getBooks(String titleOfBook) {
        String insertSQL = "select b.id, b.title, a.fio, g.name from books b join books_authors ba on b.id = ba.book_id join authors a on ba.author_id = a.id join genres g \n" +
                "on b.genre_id  = g.id where title = ?";
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
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String author = resultSet.getString("fio");
                String genre = resultSet.getString("name");
                String title = resultSet.getString("title");
                list.add(new Book(id, author, genre, title));
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
