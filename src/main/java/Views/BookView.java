package Views;

import Models.Book;

public class BookView {
    void printBook(Book book) {
        System.out.printf("%25s %15s\n", book.getTitle(), book.getAuthor().getName());
    }
}

