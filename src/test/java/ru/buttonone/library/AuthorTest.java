package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.AuthorsDao;
import ru.buttonone.dao.AuthorsDaoImpl;
import ru.buttonone.dao.BooksDao;
import ru.buttonone.dao.BooksDaoImpl;
import ru.buttonone.domain.Author;
import ru.buttonone.library.specifications.LibrarySpecifications;

import static ru.buttonone.library.specifications.LibraryConstants.*;
import static ru.buttonone.library.specifications.LibraryEndpoints.ADD_BOOK_PATH;

public class AuthorTest {
    private final AuthorsDao authorsDao = new AuthorsDaoImpl();
    private final BooksDao booksDao = new BooksDaoImpl();

    @DisplayName(" корректно добавлять книгу в БД и проверять автора через БД")
    @Test
    public void shouldHaveCorrectPostBookToDb() throws JsonProcessingException {
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

        Author firstAuthor = authorsDao.getAuthorsByFio(bookHarryPotter.getAuthors()).get(0);
        Assertions.assertEquals(bookHarryPotter.getAuthors(), firstAuthor.getFio());
    }

    @AfterEach
    public void deleteTestBook() {
        booksDao.deleteBookByTitle(HARRY_POTTER);
    }
}
