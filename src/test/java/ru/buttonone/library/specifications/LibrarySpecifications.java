package ru.buttonone.library.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static ru.buttonone.library.specifications.LibraryEndpoints.*;

public class LibrarySpecifications {

    public static RequestSpecification postRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
    public static ResponseSpecification postResponseSpecification() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectStatusCode(STATUS_CODE)
                .build();
    }

    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
    public static ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectStatusCode(STATUS_CODE)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification deleteRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
    public static ResponseSpecification deleteResponseSpecification() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectStatusCode(STATUS_CODE)
                .expectContentType(ContentType.TEXT)
                .build();
    }
}
