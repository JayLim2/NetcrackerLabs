/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import models.Book;

/**
 * @see Commands
 * @see CommandPacket
 * @author Alexander
 */
@XmlRootElement
public class SetBookPacket extends CommandPacket{
    private Book book;
    private int newAuthorId;
    private int bookId;
    
    public SetBookPacket(){}
    
    public SetBookPacket(Commands command, int newAuthorId,int bookId, Book book){
        super(command);
        this.book = book;
        this.newAuthorId = newAuthorId;
        this.bookId = bookId;
    }
            
    public int getNewAuthorId(){
        return newAuthorId;
    }
    
    @XmlElement
    public void setNewAuthorId(int id){
        this.newAuthorId = id;
    }
    
    public int getBookId(){
        return bookId;
    }
    
    @XmlElement
    public void setBookId(int id){
        this.bookId = id;
    }
    
    @XmlElement
    public void setBook(Book book){
        this.book = book;
    }
    
    public Book getBook(){
        return book;
    }
}
