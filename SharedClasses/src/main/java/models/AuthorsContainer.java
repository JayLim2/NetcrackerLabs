package models;

import exceptions.InvalidCommandAction;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlSeeAlso(Author.class)
public class AuthorsContainer {
    private List<Author> authors;

    private static final int AUTHORNAME_LIMIT = 60;

    public AuthorsContainer() {
        authors = new LinkedList<Author>();
    }

    @XmlElement(name = "authorList")
    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Author getAuthor(int id) {
        return authors.get(id);
    }

    public void addAuthor(Author author) throws InvalidCommandAction {

       if (!isExists(author, authors) && isValid(author)) {
            author.setName(author.getName().trim());
            authors.add(author);
            System.out.println("Author " + "\"" + author.getName() + "\"" + " added");
            sort();
        } else {

           System.out.println("Authors name is not correct");
           throw new InvalidCommandAction("This author is already exist");

       }

    }

    public void addAuthor(int index, Author author) {

        if (!isExists(author, authors) && isValid(author)) {
            author.setName(author.getName().trim());
            authors.add(index, author);
            System.out.println("Author " + "\"" + author.getName() + "\"" + " added");
        } else
            System.out.println("Authors name is not correct");

    }

    private boolean isExists(Author author, List<Author> authors) {
        for (Author currentAuthor : authors) {
            if (author.equals(currentAuthor))
                return true;
        }
        return false;
    }

    private boolean isValid(Author author) {
        return !author.getName().isEmpty() &&
                author.getName().length() <= AUTHORNAME_LIMIT;
    }

    private void sort() {
        authors.sort(new AuthorComparator());
    }

}
