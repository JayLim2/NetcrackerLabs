package Models;

import java.util.Comparator;
//Возвращает 1, если первая строка больше второй
public class BooksComparator implements Comparator<Book> {
    public int compare(Book b1, Book b2){
        Integer a = b1.getId();
        Integer b = b2.getId();
        return a.compareTo(b);
    }

}