//import Controllers.AuthorController;
//import Controllers.BookController;
import Controllers.AuthorController;
import Models.Author;
import Models.Book;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
       // AuthorController authorController = new AuthorController();
       // BookController bookController = new BookController();
        System.out.println(UUID.randomUUID());
        Book nb = new Book();
        Author a1 = new Author();
        Author a2 = new Author();
        a1.setName("Leha");
        a2.setName("Alexey");
        a1.addBook(new Book("Voina i mir", a1, 12, "a","b"));
        a1.addBook(new Book("ololo", a1, 12, "a","b"));
        System.out.println();
        AuthorController ac = new AuthorController();
        ac.addAuthor(a1);

    }


}
