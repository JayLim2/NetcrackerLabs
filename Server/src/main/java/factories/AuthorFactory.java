package factories;

import model.Author;

public class AuthorFactory {
    private static AuthorFactory instance;

    public static AuthorFactory getInstance() {
        if (instance == null)
            instance = new AuthorFactory();
        return instance;
    }


    public Author createAuthor(int authorID, String authorName) {
        return new Author(authorID, authorName);
    }
}
