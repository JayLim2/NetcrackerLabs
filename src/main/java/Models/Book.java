package Models;

import javax.xml.bind.annotation.*;
import java.util.Calendar;


@XmlRootElement
public class Book {
    private String title;
    private Author author;
    private int publishYear;
    private String publisher;
    private String brief;
    private static int  cid = 0;
    private int id = 0;

    {
        id = cid++;
    }
    
    public Book() {    }

    public Book(String title, Author author, int publishYear, String publisher, String brief) throws YearOutOfBoundsException {
        this.title = title;
        this.author = author;
        setPublishYear(publishYear);
        this.publisher = publisher;
        this.brief = brief;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    @XmlTransient
    public Author getAuthor() {
            return author;
    }
    
    @XmlTransient
    public int getId(){
        return id;
    }

    public void setAuthor(Author author) {
            this.author = author;
    }

    @XmlElement
    public int getPublishYear() {
            return publishYear;
    }

    public final void setPublishYear(int publishYear) throws YearOutOfBoundsException {
        if (!(publishYear>=0&&publishYear<Calendar.getInstance().get(Calendar.YEAR))) throw new YearOutOfBoundsException(); 
        this.publishYear = publishYear;
 
    }

    @XmlElement
    public String getPublisher() {
            return publisher;
    }

    public void setPublisher(String publisher) {
            this.publisher = publisher;
    }

    @XmlElement
    public String getBrief() {
            return brief;
    }

    public void setBrief(String brief) {
            this.brief = brief;
    }
}

