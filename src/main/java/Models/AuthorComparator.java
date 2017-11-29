package Models;

import java.util.Comparator;
//Возвращает 1, если первая строка больше второй
public class AuthorComparator implements Comparator<Author> {
    public int compare(Author author1, Author author2){
            return author1.getName().compareTo(author2.getName());
    }

}
