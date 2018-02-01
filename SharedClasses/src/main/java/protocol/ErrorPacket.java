/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see ResponsePacket
 * @see Responses
 * @author Alexander
 */
@XmlRootElement
public class ErrorPacket extends ResponsePacket{
    private String description;
    
    public ErrorPacket(){}
    
    public ErrorPacket(Responses response, String description){
        super(response);
        this.description = description;
    }
    
    @XmlElement
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
}
