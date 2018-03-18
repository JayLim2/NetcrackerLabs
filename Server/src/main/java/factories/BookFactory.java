package factories;

import model.Book;

public class BookFactory {

    private static BookFactory instance;

    public static BookFactory getInstance() {
        if (instance == null)
            instance = new BookFactory();
        return instance;
    }

    public Book createBook(int bookID, String bookName, int publishYear, String brief, int publisherID) {
        return new Book(bookID, bookName, publishYear, brief, publisherID);
    }
}
