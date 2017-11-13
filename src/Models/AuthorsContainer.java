package Models;

import java.util.List;

public class AuthorsContainer {
    private List<Author> authors;

    public List<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(List<Author> authors){
        this.authors = authors;
    }

    public void addAuthor(Author author){
        author.id=authors.size();
        authors.add(author);
        sort();
    }

    private void sort(){
        authors.sort(new AuthorComparator());
    }
}
