package models;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * the book model class
 * book consists of title(String), auhtor(Author),
 * publishYear(int), publisher(String), breif(String)
 *
 * @author Alexander, Korostelev, Komarov
 */
@XmlRootElement
public class Book {
    private String title;
    private Author author;
    private int publishYear;
    private String publisher;
    private String brief;
    private int publisherID;
    private static List<Integer> busyId;
    private int id = -1;


    static {
        busyId = new LinkedList<>();
    }

    /**
     * empty constructor for serialization
     */
    public Book() {
    }

    /**
     * @param title       book's title
     * @param author      Author of the book
     * @param publishYear publush year. cant be bigger than current
     * @param publisher
     * @param brief
     * @throws YearOutOfBoundsException if publish year bigger than current one
     */
    public Book(String title, Author author, int publishYear, String publisher, String brief) throws YearOutOfBoundsException {
        this.title = title.trim();
        this.author = author;
        setPublishYear(publishYear);
        this.publisher = publisher.trim();
        this.brief = brief.trim();
    }

    public Book(String title, int publishYear, String brief, int publisherID) throws YearOutOfBoundsException {
        this.title = title.trim();
        setPublishYear(publishYear);
        this.brief = brief;
        this.publisherID = publisherID;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    @XmlTransient
    public Author getAuthor() {
        return author;
    }



    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int val) {
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
     * id dispatcher.to be ivoked upon addition to a container
     */
    public void dispatchId() {
        int nid = 0;
        while (busyId.contains(nid)) nid++;
        id = nid;
        busyId.add(id);
    }

    public static void removeId(int id) {
//busyId.remove(new Integer(id));
    }

    public static void resetId() {
        busyId = new LinkedList<>();
    }

    public void setAuthor(Author author) {
//        if(this.author != null){
//            this.author.getBooks().remove(this);
//        }
//        if (!author.addBook(this)){
//           if(this.author != null) this.author.addBook(this);
//        }//and throw something later
        this.author = author;
    }

    @XmlElement
    public int getPublishYear() {
        return publishYear;
    }

    public final void setPublishYear(int publishYear) throws YearOutOfBoundsException {
        if (!(publishYear >= 0 && publishYear <= Calendar.getInstance().get(Calendar.YEAR)))
            throw new YearOutOfBoundsException();
        this.publishYear = publishYear;
    }

    @XmlElement
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher.trim();
    }

    @XmlElement
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief.trim();
    }

    public int getPublisherID() {
        return publisherID;
    }

    //Book Equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Book) {
            Book book = (Book) obj;
            return title.equals(book.title.trim()) &&
                    (author == author) &&
                    publishYear == book.publishYear &&
                    publisher.equals(book.publisher.trim()) &&
                    brief.equals(book.brief.trim());
        }

        return false;
    }
}

