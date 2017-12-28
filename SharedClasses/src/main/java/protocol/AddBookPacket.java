/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import models.Book;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Алескандр
 */
@XmlRootElement
public class AddBookPacket extends CommandPacket{
    private Book book;
    private int id;

    public AddBookPacket(){}

    public AddBookPacket(Commands command, int id, Book book) {
        super(command);
        this.book = book;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @XmlElement
    public void setId(int id){
        this.id = id;
    }

    @XmlElement
    public void setBook(Book book){
        this.book = book;
    }

    public Book getBook(){
        return book;
    }
}
