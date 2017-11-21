package Models;

import java.util.Comparator;
//Возвращает 1, если первая строка больше второй
public class AuthorComparator implements Comparator<Author> {
    public int compare(Author author1, Author author2){
        if (author1.getName().compareTo(author2.getName())<0)
            return 1;
        if (author1.getName().compareTo(author2.getName())>0)
            return -1;

            return 0;
    }

}
