package Models;

import java.util.Comparator;
//Возвращает 1, если первая строка больше второй
public class BooksComparator implements Comparator<Book> {
    public int compare(Book b1, Book b2){
        return b1.getTitle().compareTo(b2.getTitle());
    }

}