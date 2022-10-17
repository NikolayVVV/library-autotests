package ru.buttonone.library;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.GenresDao;
import ru.buttonone.dao.GenresDaoImpl;
import ru.buttonone.domain.Genre;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GenreTest {
    private final static String BASE_URI = "http://localhost:8080";
    private final static String GENRES_PATH = "http://localhost:8080/api/books/1";
    private final GenresDao genresDao = new GenresDaoImpl();

    @DisplayName(" корректно получать жанр из БД")
    @Test
    public void shouldHaveCorrectGetAuthorsFromDb() {

        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get(GENRES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        Genre firstGenres = genresDao.getGenresByName("Fantastic").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Fantastic", firstGenres.getName())
        );

    }


}
