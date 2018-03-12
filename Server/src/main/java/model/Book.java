package model;

public class Book {

    private int bookID;
    private String bookName;
    private int publushYear;
    private String brief;
    private int publisherID;

    public Book() {
    }

    public Book(int bookID, String bookName, int publushYear, String brief, int publisherID) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.publushYear = publushYear;
        this.brief = brief;
        this.publisherID = publisherID;
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

    public int getPublushYear() {
        return publushYear;
    }

    public void setPublushYear(int publushYear) {
        this.publushYear = publushYear;
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
                + ", " + bookName + ", " + publushYear + ", " + brief + ")");
        return stringBuilder.toString();
    }
}
