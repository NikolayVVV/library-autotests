package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.BooksDao;
import ru.buttonone.dao.BooksDaoImpl;
import ru.buttonone.domain.Book;
import ru.buttonone.library.specifications.LibrarySpecifications;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.buttonone.library.specifications.LibraryConstants.*;


public class BookTest {
    private final BooksDao booksDao = new BooksDaoImpl();


    @DisplayName("BUT-69 - Test 1")
    @Test
    public void createEntityWithPostAndCheckDbWithSelect() throws JsonProcessingException {

//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(bookHarryPotter);

        RestAssured.given()
                .spec(LibrarySpecifications.postRequestSpecification())
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.postResponseSpecification());

        Book firstBook = booksDao.getBooksByTitle(HARRY_POTTER).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(bookHarryPotter.getGenre(), firstBook.getGenre()),
                () -> Assertions.assertEquals(bookHarryPotter.getAuthors(), firstBook.getAuthors()),
                () -> Assertions.assertEquals(HARRY_POTTER, firstBook.getTitle())
        );
        booksDao.deleteBookByTitle(HARRY_POTTER);
    }


    @DisplayName("BUT-70 - Test 2")
    @Test
    public void createEntityWithPostAndCheckWithGet() throws JsonProcessingException {
//expectedBook -> json
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(bookHarryPotter);

        given()
                .spec(LibrarySpecifications.postRequestSpecification())
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.postResponseSpecification());

        String secondJsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(bookLOTR);

        given()
                .spec(LibrarySpecifications.postRequestSpecification())
                .body(secondJsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.postResponseSpecification());

        ValidatableResponse validatableResponse = given()
                .spec(LibrarySpecifications.getRequestSpecification())
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.getResponseSpecification());

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HARRY_POTTER, listOfBooks.get(0).getTitle()),
                () -> Assertions.assertEquals(LOTR, listOfBooks.get(1).getTitle())
        );
        booksDao.deleteBookByTitle(HARRY_POTTER);
        booksDao.deleteBookByTitle(LOTR);
    }

    @DisplayName("BUT-71 - Test 3")
    @Test
    public void deleteEntityWithHelpDeleteAndCheckEntityWithHelpGet() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(bookHarryPotter);

        RestAssured.given()
                .spec(LibrarySpecifications.postRequestSpecification())
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.postResponseSpecification());

        booksDao.deleteBookByTitle(HARRY_POTTER);

        ValidatableResponse validatableResponse = given()
                .spec(LibrarySpecifications.getRequestSpecification())
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.getResponseSpecification());

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertFalse(listOfBooks.contains(bookHarryPotter));
    }

    @DisplayName("BUT-72 - Test 4")
    @Test
    public void createEntityWithPostThenDeleteHerAndCheckWithHelpGet() throws JsonProcessingException {
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(bookHarryPotter);

        RestAssured.given()
                .spec(LibrarySpecifications.postRequestSpecification())
                .body(jsonExpectedBook)
                .when()
                .post(ADD_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.postResponseSpecification());

        RestAssured.given()
                .spec(LibrarySpecifications.deleteRequestSpecification())
                .when()
                .delete(DELETE_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.deleteResponseSpecification());

        ValidatableResponse validatableResponse = given()
                .spec(LibrarySpecifications.getRequestSpecification())
                .when()
                .get(GET_BOOK_PATH)
                .then()
                .spec(LibrarySpecifications.getResponseSpecification());

        List<Book> listOfBooks = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        Assertions.assertFalse(listOfBooks.contains(bookHarryPotter));

        booksDao.deleteBookByTitle(HARRY_POTTER);
    }
}
