package factories;

import model.Author;

public class AuthorFactory {

    public static Author createAuthor(int authorID, String authorName){
        return new Author(authorID, authorName);
    }
}
