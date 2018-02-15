package models;

import exceptions.InvalidCommandAction;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * author container model. encapsulates a list of authors.
 * authors contain books
 * @author �lexader,Komarov,Korostelev
 */
@XmlRootElement
@XmlSeeAlso(Author.class)
public class AuthorsContainer {
    /**
     * The contained list of Author models.
     * Main dtabase entity.
     */
    private List<Author> authors;

    /**
     * The limit of acceptable author name string length.
     */
    private static final int AUTHORNAME_LIMIT = 60;

    /**
     * Empty constructor for serialization purposes.
     */
    public AuthorsContainer() {
        authors = new LinkedList<Author>();
    }

    /**
     * Retrieves the contained auhtor list.
     * @return the contained author list.
     */
    @XmlElement(name = "authorList")
    public final List<Author> getAuthors() {
        return authors;
    }

    /**
     * Sets the contained author list.
     * @param newAuthors the list to be contained
     */
    public final void setAuthors(final List<Author> newAuthors) {
        this.authors = newAuthors;
    }

    /**
     * @deprecated
     * retrive an author by id
     * @param id author id(list id)
     * @return author at position id
     */
    public final Author getAuthor(final int id) {
        return authors.get(id);
    }

    /**
     * Add an author to the container.
     * @param author author to add
     * @throws InvalidCommandAction if author with
     * the same name alredy exists in this container
     */
    public final void addAuthor(final Author author)
            throws InvalidCommandAction {
       if (!isExists(author, authors) && isValid(author)) {
            author.setName(author.getName().trim());
            authors.add(author);
            System.out.println("Author " + "\""
                    + author.getName() + "\"" + " added");
            sort();
        } else {

           System.out.println("Authors name is not correct");
           throw new InvalidCommandAction("This author is already exist");

       }

    }

    /**
     * @deprecated
     * @param index Author's new position in list
     * @param author Author to add
     */
    public final void addAuthor(final int index, final Author author) {
        if (!isExists(author, authors) && isValid(author)) {
            author.setName(author.getName().trim());
            authors.add(index, author);
            System.out.println("Author " + "\""
                    + author.getName() + "\"" + " added");
        } else {
            System.out.println("Authors name is not correct");
        }
    }

    /**
     * Check if an equal author exists in container.
     * @param newAuthor author to check
     * @param newAuthors ??????
     * @return true if exists, false if not
     */
    private boolean isExists(final Author newAuthor,
            final List<Author> newAuthors) {
        for (Author currentAuthor : newAuthors) {
            if (newAuthor.equals(currentAuthor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cheking if the author name has propel length.
     * @param author author to check
     * @return true if proper, false if too big or empty
     */
    private boolean isValid(final Author author) {
        return !author.getName().isEmpty()
                && author.getName().length() <= AUTHORNAME_LIMIT;
    }

    /**
     * Sortsthe list with the default AuthorComparator.
     */
    private void sort() {
        authors.sort(new AuthorComparator());
    }

}
