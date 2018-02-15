package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.LinkedList;
import java.util.List;

/**
 * Author model. contains name and booklist
 * @author Alexander, Korostelev, Komarov
 */
@XmlRootElement
@XmlSeeAlso(Book.class)
public class Author {
    private String name;
    private List<Book> books;

    private static List<Integer> busyId;
    private int id = -1;


    private static final int TITLE_LIMIT = 75;
    private static final int BRIEF_LIMIT = 1500;
    private static final int PUBLISHER_LIMIT = 75;

    static {
        busyId = new LinkedList<>();
    }

    /**
     * empty constructor for serialization purposes
     */
    public Author() {
        books = new LinkedList<>();
    }

    /**
     * main constructor
     * @param name author name
     */
    public Author(String name) {
        this.name = name.trim();
        books = new LinkedList<>();
    }

    /**
     * id generator. to be invoked upon addition to a container
     */
    public void dispatchId() {
        int nid = 0;
        while (busyId.contains(nid)) nid++;
        id = nid;
        busyId.add(id);
    }

    /**
     * @deprecated
     * @param id
     */
    public static void removeId(int id) {
        //busyId.remove(new Integer(id));
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * set and add the new id to author busyid list
     * @param val new id
     */
    public void setId(int val) {//throws ValidationException {
        if (val != -1) {
            busyId.remove(new Integer(id));
            if (busyId.contains(val)) {
                busyId.add(id);
                //throw new ValidationException("busy id");
            }
            id = val;
            busyId.add(id);
        }
    }

    /**
     * reset author id list. to be used upon reload from file for example
     */
    public static void resetId() {
        busyId = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    @XmlElement(name = "booksList")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * @deprecated
     * @param book
     * @return
     */
    public boolean addBook(Book book) {
        if (!isExists(book, books) && isValid(book)) {
            book.setTitle(book.getTitle().trim());
            book.setPublisher(book.getPublisher().trim());
            book.setBrief(book.getBrief().trim());

            books.add(book);
            books.sort(new BooksComparator());
            return true;
        }
        return false;
    }

    /**
     * @deprecated
     * @param book
     * @param bookList
     * @return
     */
    private boolean isExists(Book book, List<Book> bookList) {
        for (Book currentBook : bookList) {
            if (book.equals(currentBook))
                return true;
        }
        return false;
    }

    /**
     * @deprecated
     * @param book
     * @return
     */
    private boolean isValid(Book book) {
        return !book.getTitle().isEmpty() &&
                book.getTitle().length() <= TITLE_LIMIT &&
                book.getBrief().length() <= BRIEF_LIMIT &&
                book.getPublisher().length() <= PUBLISHER_LIMIT;
    }

    //Author equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Author) {
            Author author = (Author) obj;
            return name.equals(author.name.trim()) &&
                    books.equals(author.books);
        }

        return false;
    }

    //Author toString
    @Override
    public String toString() {
        return name;
    }
}
