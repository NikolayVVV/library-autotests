package ru.buttonone.library;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.buttonone.dao.AuthorsDao;
import ru.buttonone.dao.AuthorsDaoImpl;
import ru.buttonone.domain.Author;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AuthorTest {
    private final static String BASE_URI = "http://localhost:8080";
    private final static String AUTHORS_PATH = "http://localhost:8080/api/books/1";
    private final AuthorsDao authorsDao = new AuthorsDaoImpl();

    @DisplayName(" корректно получать автора из БД")
    @Test
    public void shouldHaveCorrectGetAuthorsFromDb() {

        ValidatableResponse validatableResponse = given()
                .baseUri(BASE_URI)
                .when()
                .get(AUTHORS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .statusCode(200);

        List<Author> authorList = authorsDao.getAuthorsByFio("a1");
        Author firstAuthor = authorList.get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("a1", firstAuthor.getFio()),
                () -> Assertions.assertEquals(1, firstAuthor.getId())
        );


    }


}
