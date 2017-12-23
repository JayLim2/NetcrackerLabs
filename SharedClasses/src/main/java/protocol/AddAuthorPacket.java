/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import models.Author;

/**
 *
 * @author Алескандр
 */
@XmlRootElement
public class AddAuthorPacket extends CommandPacket{
    private Author author;
    
    public AddAuthorPacket(){}
    
    public AddAuthorPacket(Commands command,Author author){
        super(command);
        this.author = author;
    }
    
    @XmlElement
    public void setAuthor(Author author){
        this.author = author;
    }
    
    public Author getAuthor(){
        return author;
    }
}
