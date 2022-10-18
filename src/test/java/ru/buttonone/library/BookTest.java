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
import static ru.buttonone.library.specifications.LibrarySpecifications.*;


public class BookTest {
    private BooksDao booksDao = new BooksDaoImpl();


    @DisplayName("после добавления книга появляется в БД")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBook() throws JsonProcessingException {

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


    }


    @DisplayName(" корректно получать книги из БД")
    @Test
    public void shouldHaveCorrectGetBooksFromDb() throws JsonProcessingException {
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


        RestAssured.given()
                .baseUri(BASE_URI)
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        Book firstBook = booksDao.getBooksByTitle("HarryPotter").get(0);
        Book secondBook = booksDao.getBooksByTitle("LOTR").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("HarryPotter", firstBook.getTitle()),
                () -> Assertions.assertEquals("Fantastic", firstBook.getGenre()),
                () -> Assertions.assertEquals("Rowling", firstBook.getAuthors()),
                () -> Assertions.assertEquals("LOTR", secondBook.getTitle()),
                () -> Assertions.assertEquals("Fantasy", secondBook.getGenre()),
                () -> Assertions.assertEquals("Tolkien", secondBook.getAuthors())
        );


    }


    @DisplayName(" корректно удалить книгу из БД")
    @Test
    public void shouldHaveCorrectDeleteBookFromDb() throws JsonProcessingException {
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


}
