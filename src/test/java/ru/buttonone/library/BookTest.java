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
import static org.hamcrest.Matchers.is;
import static ru.buttonone.library.specifications.LibrarySpecifications.*;


public class BookTest {
    private BooksDao booksDao = new BooksDaoImpl();


    @DisplayName("Test 1")
    @Test
    public void сreateSomeEntityWithPostCommandAndCheckingDatabaseWithHelpSelectCommand() throws JsonProcessingException {

        Book expectedBook = new Book(1, "Rowling", "Fantastic", "HarryPotter");
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .log().all()
                .statusCode(200);


        Book firstBook = booksDao.getBooksByTitle("HarryPotter").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Fantastic", firstBook.getGenre()),
                () -> Assertions.assertEquals("Rowling", firstBook.getAuthors()),
                () -> Assertions.assertEquals("HarryPotter", firstBook.getTitle())

        );
        booksDao.deleteBookByTitle("HarryPotter");


    }


    @DisplayName("Test 2")
    @Test
    public void createSomeEntityWithPostCommandAndCheckingWithHelpGetCommand() throws JsonProcessingException {
        Book firstExpectedBook = new Book(1, "Rowling", "Fantastic", "HarryPotter");
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(firstExpectedBook);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .log().all()
                .statusCode(200);

        Book secondExpectedBook = new Book(2, "Tolkien", "Fantasy", "LOTR");
//expectedBook -> json
        String secondJsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(secondExpectedBook);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(secondJsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .log().all()
                .statusCode(200);


        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);


        Assertions.assertAll(
                () -> Assertions.assertEquals("HarryPotter", listOfBooks.get(0).getTitle()),
                () -> Assertions.assertEquals("LOTR", listOfBooks.get(1).getTitle())
        );
        booksDao.deleteBookByTitle("HarryPotter");
        booksDao.deleteBookByTitle("LOTR");


    }

    @DisplayName("Test 3")
    @Test
    public void deleteSomeEntityWithDeleteCommandAndCheckingEntityWithHelpGetCommand() throws JsonProcessingException {
        Book firstExpectedBook = new Book(1, "Rowling", "Fantastic", "HarryPotter");
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(firstExpectedBook);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .log().all()
                .statusCode(200);


        booksDao.deleteBookByTitle("HarryPotter");

        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(listOfBooks.contains(firstExpectedBook))
        );

    }


    @DisplayName("Test 4")
    @Test
    public void createSomeEntityWithPostCommandThenDeleteHerAndCheckingWithHelpGetCommand() throws JsonProcessingException {
        Book firstExpectedBook = new Book(1, "Rowling", "Fantastic", "HarryPotter");
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(firstExpectedBook);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .log().all()
                .statusCode(200);

        RestAssured.given()
                .baseUri(BASE_URI)
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook)
                .when()
                .delete("/api/books/1")
                .then()
                .log().all()
                .statusCode(200);


        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(listOfBooks.contains(firstExpectedBook))
        );

        booksDao.deleteBookByTitle("HarryPotter");


    }
}
