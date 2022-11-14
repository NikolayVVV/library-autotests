package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.BooksDao;
import ru.buttonone.dao.BooksDaoImpl;
import ru.buttonone.dao.GenresDao;
import ru.buttonone.dao.GenresDaoImpl;
import ru.buttonone.domain.Genre;
import ru.buttonone.library.specifications.LibrarySpecifications;

import static ru.buttonone.library.specifications.LibraryConstants.*;
import static ru.buttonone.library.specifications.LibraryEndpoints.ADD_BOOK_PATH;

public class GenreTest {
    private final GenresDao genresDao = new GenresDaoImpl();
    private final BooksDao booksDao = new BooksDaoImpl();

    @DisplayName(" корректно добавлять книгу в БД и проверять жанр через БД")
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

        Genre firstGenres = genresDao.getGenresByName(bookHarryPotter.getGenre()).get(0);
        Assertions.assertEquals(bookHarryPotter.getGenre(), firstGenres.getName());
    }

    @AfterEach
    public void deleteTestBook() {
        booksDao.deleteBookByTitle(HARRY_POTTER);
    }
}
