package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.AuthorsDao;
import ru.buttonone.dao.AuthorsDaoImpl;
import ru.buttonone.dao.BooksDao;
import ru.buttonone.dao.BooksDaoImpl;
import ru.buttonone.domain.Author;
import ru.buttonone.domain.Book;


import static ru.buttonone.library.specifications.LibrarySpecifications.ADD_BOOK_PATH;
import static ru.buttonone.library.specifications.LibrarySpecifications.BASE_URI;

public class AuthorTest {
    private final AuthorsDao authorsDao = new AuthorsDaoImpl();
    private BooksDao booksDao = new BooksDaoImpl();

    @DisplayName(" корректно добавлять книгу в БД и проверять автора через БД")
    @Test
    public void shouldHaveCorrectPostBookToDb() throws JsonProcessingException {

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

        Author firstAuthor = authorsDao.getAuthorsByFio("Rowling").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Rowling", firstAuthor.getFio())
        );

        booksDao.deleteBookByTitle("HarryPotter");


    }


}
