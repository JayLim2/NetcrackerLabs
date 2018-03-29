package model;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int bookID;
    private String bookName;
    private int publishYear;
    private String brief;
    private int publisherID;
    private List<String> authorNames;

    public Book() {
        authorNames = new ArrayList<String>();
    }

    public Book(int bookID, String bookName, int publishYear, String brief, int publisherID, List<String> authorNames) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.publishYear = publishYear;
        this.brief = brief;
        this.publisherID = publisherID;
        this.authorNames = authorNames;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }
    
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName() + " (" + bookID
                + ", " + bookName + ", " + publishYear + ", " + brief + "{");
        for(String aname:authorNames ){
            stringBuilder.append(aname + ", ");   
        }
        stringBuilder.append("} )");
        return stringBuilder.toString();
    }
}
