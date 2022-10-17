package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.BooksDao;
import ru.buttonone.dao.BooksDaoImpl;
import ru.buttonone.domain.Book;

import java.util.List;

import static io.restassured.RestAssured.given;


public class BookTest {
    private final static String BASE_URI = "http://localhost:8080";
    private final static String BOOKS_PATH = "http://localhost:8080/api/books/{id}";
    private BooksDao booksDao = new BooksDaoImpl();


    @DisplayName("После добавления книга появляется в БД")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBook() throws JsonProcessingException {

        Book expectedBook = new Book(1, "Rowling", "Fantastic", "HarryPotter");
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        RestAssured.given()
                .baseUri("http://localhost:8080")
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .post("/api/books/add")
                .then()
                .log().all()
                .statusCode(200);


        Book firstBook = booksDao.getBooksByTitle("HarryPotter").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Fantastic", firstBook.getGenre()),
                () -> Assertions.assertEquals("Rowling", firstBook.getAuthors()),
                () -> Assertions.assertEquals("HarryPotter", firstBook.getTitle())

        );


    }


    @DisplayName(" корректно получать книги из БД")
    @Test
    public void shouldHaveCorrectGetBooksFromDb() throws JsonProcessingException {
        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get("/api/books")
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        Book firstBook = booksDao.getBooksByTitle("HarryPotter").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("HarryPotter", firstBook.getTitle()),
                () -> Assertions.assertEquals("Fantastic", firstBook.getGenre()),
                () -> Assertions.assertEquals("Rowling", firstBook.getAuthors())
        );


    }


    @DisplayName(" корректно получать книги из БД по ID")
    @Test
    public void shouldHaveCorrectGetBooksFromDbOnId() {

        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .pathParam("id", 1)
                .get(BOOKS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        Book firstBook = booksDao.getBooksById(1).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, firstBook.getId())
        );

    }


    @DisplayName(" корректно удалить книгу из БД")
    @Test
    public void shouldHaveCorrectDeleteBookFromDb() throws JsonProcessingException {

        RestAssured.given()
                .baseUri("http://localhost:8080")
                .header(new Header("Content-Type", "application/json"))
                .when()
                .pathParam("id", 1)
                .delete(BOOKS_PATH)
                .then()
                .log().all()
                .statusCode(200);


        List<Book> bookList = booksDao.getBooksById(1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, bookList.size())
        );

    }

    @DisplayName(" корректно получать пустой массив из БД когда в нем нет книг")
    @Test
    public void shouldHaveCorrectGetEmptyArrayFromDb() throws JsonProcessingException {
        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get("/api/books")
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, listOfBooks.size())
        );


    }


}
