package factories;

import model.Book;

public class BookFactory {

    public static Book createBook(int bookID, String bookName, int publishYear, String brief, int publisherID) {
        return new Book(bookID, bookName, publishYear, brief, publisherID);
    }
}
