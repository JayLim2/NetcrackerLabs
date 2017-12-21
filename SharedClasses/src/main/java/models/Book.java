package models;

import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.ValidationException;


@XmlRootElement
public class Book {
    private String title;
    private Author author;
    private int publishYear;
    private String publisher;
    private String brief;
    private static List<Integer> busyId;
    private int id = -1;


    static{
        busyId = new LinkedList<>();
    }
    
    public Book() {
    }

    public Book(String title, Author author, int publishYear, String publisher, String brief) throws YearOutOfBoundsException {
        this.title = title.trim();
        this.author = author;
        setPublishYear(publishYear);
        this.publisher = publisher.trim();
        this.brief = brief.trim();
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
    
    public void setId(int val) throws ValidationException {
		if (val != -1){
			busyId.remove(new Integer(id));
			if (busyId.contains(val)) {
				busyId.add(id);
				throw new ValidationException("busy id");
			}
			id = val;
			busyId.add(id);
		}
    }
	
	public void dispatchId(){
		int nid = 0;
		while(busyId.contains(id)) nid++;
		id = nid;
	}
	
	public static void removeId(int id){
		busyId.remove(new Integer(id));
	}
    
    public static void resetId(){
        busyId = new LinkedList<>();
    }

    public void setAuthor(Author author) {
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

    //Book Equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Book) {
            Book book = (Book) obj;
            return title.equals(book.title.trim()) &&
                    author.equals(book.author) &&
                    publishYear == book.publishYear &&
                    publisher.equals(book.publisher.trim()) &&
                    brief.equals(book.brief.trim());
        }

        return false;
    }
}

