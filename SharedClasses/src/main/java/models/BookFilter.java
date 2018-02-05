/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.regex.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * book filter class.
 * Contains Pattern objects corresponding to book's fields
 * @author Алескандр
 */
@XmlRootElement
public class BookFilter {
    private String title;
    private String author;
    private String publishYear;
    private String brief;
    private String publisher;
    
    
    public BookFilter(){
        
    }
    //wasteful string usage
    /**
     * BookFilter Constructor
     * all parameters must be Strings with * for any number of undefined symbols
     * and ? for one undefined symbol 
     * they are then converted into appropriate regexes
     * and then pattens are constructed based on said regexes
     * and these patterns are stored for later use
     * @param title
     * @param author
     * @param publishYear
     * @param brief
     * @param publisher 
     */
    public BookFilter(String title, String author, String publishYear, String brief, String publisher){
       this.title =  title;
       this.author =  author;
       this.publishYear =  publishYear;
       this.brief =  brief;
       this.publisher =  publisher;
    }
    
    /**
     * Checks if book fullfills the generatedd criteria
     * (wasteful will rep later)
     * @param book
     * @return true if does, false if does not
     */
    public boolean accept(Book book){
        String temp1 = title.replaceAll("\\*", ".*");
        String temp2 = temp1.replaceAll("\\?", ".");
        Pattern titleP =  Pattern.compile(temp2);
        temp1 = author.replaceAll("\\*", ".*");
        temp2 = temp1.replaceAll("\\?", ".");
        Pattern authorP =  Pattern.compile(temp2);
        temp1 = publishYear.replaceAll("\\*", ".*");
        temp2 = temp1.replaceAll("\\?", ".");
        Pattern publishYearP =  Pattern.compile(temp2);
        temp1 = brief.replaceAll("\\*", ".*");
        temp2 = temp1.replaceAll("\\?", ".");
        Pattern briefP =  Pattern.compile(temp2);
        temp1 = publisher.replaceAll("\\*", ".*");
        temp2 = temp1.replaceAll("\\?", ".");
        Pattern publisherP =  Pattern.compile(temp2);
        Matcher titleM = titleP.matcher(book.getTitle());
        Matcher authorM =  authorP.matcher(book.getAuthor().getName());
        Matcher publishYearM = publishYearP.matcher(String.valueOf(book.getPublishYear()));
        Matcher briefM = briefP.matcher(book.getBrief());
        Matcher publisherM = publisherP.matcher(book.getPublisher());
        return titleM.matches() && authorM.matches() && publishYearM.matches() && briefM.matches() && publisherM.matches();
    }
    
    @XmlElement
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    @XmlElement
    public String getAuthor(){
        return author;
    }
    
    public void setAuthor(String author){
        this.author = author;
    }
    
    @XmlElement
    public String getPublishYear(){
        return publishYear;
    }
    
    
    public void setPublishYear(String publishYear){
        this.publishYear = publishYear;
    }
    
    @XmlElement
    public String getBrief(){
        return brief;
    }
    
    public void setBrief(String brief){
        this.brief = brief;
    }
    
    @XmlElement
    public String getPublisher(){
        return publisher;
    }
    
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
}
