/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlRootElement;
import models.Author;

/**
 *
 * @author ���������
 */
@XmlRootElement
public class SetAuthorPacket extends CommandPacket{
    private Author author;
    private int id; 
    
    public SetAuthorPacket(){}
    
    public SetAuthorPacket(Commands command, int id, Author author){
        super(command);
        this.id = id;
        this.author = author;
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setAuthor(Author author){
        this.author = author;
    }
    
    public Author getAuthor(){
        return author;
    }
}