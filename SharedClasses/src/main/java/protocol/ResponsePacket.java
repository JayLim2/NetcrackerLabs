/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Алескандр
 */
public class ResponsePacket {
    private Responses response;
    
    public ResponsePacket(){}
    
    public ResponsePacket(Responses response){
        this.response = response;
    }
    
    public Responses getResponse(){
        return response;
    }
    
    @XmlElement
    public void setResponse(Responses response){
        this.response = response;
    }
}
