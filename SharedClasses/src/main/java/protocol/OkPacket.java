/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see ResponsePacket
 * @see Responses
 * @author Alexander
 */
@XmlRootElement
public class OkPacket extends ResponsePacket{
    
    public OkPacket(){}
    
    public OkPacket(Responses response){
        super(response);
    }
}
