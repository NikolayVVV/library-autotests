package ru.buttonone.library.specifications;

import ru.buttonone.domain.Book;

public class LibraryConstants {
    public final static String HARRY_POTTER = "HarryPotter";
    public final static String LOTR = "LOTR";
    public final static Book bookHarryPotter = new Book(1, "Rowling", "Fantastic", "HarryPotter");
    public final static Book bookLOTR = new Book(2, "Tolkien", "Fantasy", "LOTR");
}
