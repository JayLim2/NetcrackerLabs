package models;

import java.util.Comparator;
//Возвращает 1, если первая строка больше второй
public class AuthorComparator implements Comparator<Author> {
    public int compare(Author author1, Author author2){
        Integer a = author1.getId();
        Integer b = author2.getId();
            return a.compareTo(b);
    }

}
