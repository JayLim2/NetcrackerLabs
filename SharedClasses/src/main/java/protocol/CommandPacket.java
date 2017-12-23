/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Алескандр
 */

public class CommandPacket {
    private Commands command;
    
    public CommandPacket(){}
    
    public CommandPacket(Commands command){
        this.command = command;
    }
    
    public Commands getCommand(){
        return command;
    }
    
    @XmlElement
    public void setCommand(Commands command){
        this.command = command;
    }
}
