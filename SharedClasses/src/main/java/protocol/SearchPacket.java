/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import models.BookFilter;

/**
 * @see Commands
 * @see CommandPacket
 * @author Alexander
 */
@XmlRootElement
public class SearchPacket extends CommandPacket{
    private BookFilter bookFilter;
    
    public SearchPacket(BookFilter bookFilter){
        super(Commands.SEARCH);
        this.bookFilter = bookFilter;
    }
    
    public BookFilter getBookFilter(){
        return bookFilter;
    }
    
    @XmlElement
    public void setBookFilter(BookFilter newBookFilter){
        this.bookFilter = newBookFilter;
    }
}
