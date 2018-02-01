/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.regex.*;

/**
 * book filter class.
 * Contains Pattern objects corresponding to book's fields
 * @author Алескандр
 */
public class BookFilter {
    private Pattern title,author,publishYear,brief,publisher;
    
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
       String temp1 = title.replaceAll("\\*", ".*");
       String temp2 = temp1.replaceAll("\\?", ".");
       this.title =  Pattern.compile(temp2);
       temp1 = author.replaceAll("\\*", ".*");
       temp2 = temp1.replaceAll("\\?", ".");
       this.author =  Pattern.compile(temp2);
       temp1 = publishYear.replaceAll("\\*", ".*");
       temp2 = temp1.replaceAll("\\?", ".");
       this.publishYear =  Pattern.compile(temp2);
       temp1 = brief.replaceAll("\\*", ".*");
       temp2 = temp1.replaceAll("\\?", ".");
       this.brief =  Pattern.compile(temp2);
       temp1 = publisher.replaceAll("\\*", ".*");
       temp2 = temp1.replaceAll("\\?", ".");
       this.publisher =  Pattern.compile(temp2);
    }
    
    /**
     * Checks if book fullfills the generatedd criteria
     * @param book
     * @return true if does, false if does not
     */
    public boolean accept(Book book){
        Matcher titleM = title.matcher(book.getTitle());
        Matcher authorM =  author.matcher(book.getAuthor().getName());
        Matcher publishYearM = publishYear.matcher(String.valueOf(book.getPublishYear()));
        Matcher briefM = brief.matcher(book.getBrief());
        Matcher publisherM = publisher.matcher(book.getPublisher());
        return titleM.matches() && authorM.matches() && publishYearM.matches() && briefM.matches() && publisherM.matches();
    }
}
