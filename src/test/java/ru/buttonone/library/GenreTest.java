package ru.buttonone.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.GenresDao;
import ru.buttonone.dao.GenresDaoImpl;
import ru.buttonone.domain.Book;
import ru.buttonone.domain.Genre;

import static ru.buttonone.library.specifications.LibrarySpecifications.ADD_BOOK_PATH;
import static ru.buttonone.library.specifications.LibrarySpecifications.BASE_URI;

public class GenreTest {
    private final GenresDao genresDao = new GenresDaoImpl();

    @DisplayName(" корректно получать жанр из БД")
    @Test
    public void shouldHaveCorrectGetAuthorsFromDb() throws JsonProcessingException {
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

        Genre firstGenres = genresDao.getGenresByName("Fantastic").get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Fantastic", firstGenres.getName())
        );

    }


}
