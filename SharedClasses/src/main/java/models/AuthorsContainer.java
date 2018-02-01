package models;

import exceptions.InvalidCommandAction;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 * author container model. encapsulates a list of authors.
 * authors contain books
 * @author Àlexader,Komarov,Korostelev
 */
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

    /**
     * @deprecated 
     * retrive an author by id
     * @param id author id(list id)
     * @return author at position id
     */
    public Author getAuthor(int id) {
        return authors.get(id);
    }

    /**
     * Add an author to the container
     * @param author author to add
     * @throws InvalidCommandAction if author with the same name alredy exists in this container 
     */
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
    
    /**
     * @deprecated 
     * @param index
     * @param author 
     */
    public void addAuthor(int index, Author author) {

        if (!isExists(author, authors) && isValid(author)) {
            author.setName(author.getName().trim());
            authors.add(index, author);
            System.out.println("Author " + "\"" + author.getName() + "\"" + " added");
        } else
            System.out.println("Authors name is not correct");

    }

    /**
     * check if an equal author exists in container
     * @param author author to check
     * @param authors ??????
     * @return true if exists, false if not
     */
    private boolean isExists(Author author, List<Author> authors) {
        for (Author currentAuthor : authors) {
            if (author.equals(currentAuthor))
                return true;
        }
        return false;
    }

    /**
     * cheking if the author name has propel length
     * @param author author to check
     * @return true if proper, false if too big or empty
     */
    private boolean isValid(Author author) {
        return !author.getName().isEmpty() &&
                author.getName().length() <= AUTHORNAME_LIMIT;
    }

    /**
     * sortsthe list with the defult AuthorComparator
     */
    private void sort() {
        authors.sort(new AuthorComparator());
    }

}
