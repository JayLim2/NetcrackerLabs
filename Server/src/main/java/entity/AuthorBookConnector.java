package entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AuthorBookConnector {

    private String authorID;
    private String bookID;

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    //    public HashMap<Integer, List<Integer>> authorBookConnectors;

//    public AuthorBookConnector() {
//        this.authorBookConnectors = new HashMap<>();
//    }
//
//    public void addAuthorID(int id){
//        authorBookConnectors.put(id, new LinkedList<>());
//    }
//
//    public void deleteAuthorID(int id){
//        authorBookConnectors.remove(id);
//    }
//
//    public void addBookID(int authorID, int bookID){
//        authorBookConnectors.get(authorID).add(bookID);
//    }
//
//    public void deleteBookID(int authorID, int bookID){
//        authorBookConnectors.get(authorID).remove(bookID);
//    }
//
//    public List<Integer> getBookIDFromAuthorID(int authorID){
//        List<Integer> booksIDFromAuthor = new LinkedList<>(authorBookConnectors.get(authorID));
//        return Collections.unmodifiableList(booksIDFromAuthor);
//    }
}
