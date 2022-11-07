package ru.buttonone.library.specifications;

import ru.buttonone.domain.Book;

public class LibraryConstants {
    public final static String BASE_URI = "http://localhost:8081";
    public final static String ADD_BOOK_PATH = "/api/books/add";
    public final static String GET_BOOK_PATH = "/api/books";
    public final static String DELETE_BOOK_PATH ="/api/books/1";
    public final static String HARRY_POTTER = "HarryPotter";
    public final static String LOTR = "LOTR";
    public final static int STATUS_CODE = 200;
    public final static Book bookHarryPotter = new Book(1, "Rowling", "Fantastic", "HarryPotter");
    public final static Book bookLOTR = new Book(2, "Tolkien", "Fantasy", "LOTR");



}
