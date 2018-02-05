/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import models.AuthorsContainer;

/**
 * @see ResponsePacket
 * @see Responses
 * @author Alexander
 */
@XmlRootElement
public class ViewBooksResponsePacket extends ResponsePacket{
    private AuthorsContainer authorsContainer;
    
    public ViewBooksResponsePacket(){}
    
    public ViewBooksResponsePacket(Responses response, AuthorsContainer authorsContainer){
        super(response);
        this.authorsContainer = authorsContainer;
    }
    
    @XmlElement
    public void setAuthorsContainer(AuthorsContainer authorsContainer){
        this.authorsContainer = authorsContainer;
    }
    
    public AuthorsContainer getAuthorsContainer(){
        return authorsContainer;
    }
}
