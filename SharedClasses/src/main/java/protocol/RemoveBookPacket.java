/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ���������
 */
@XmlRootElement
public class RemoveBookPacket extends CommandPacket{
     private int id;
    
    public RemoveBookPacket(){}

    public RemoveBookPacket(Commands command, int id) {
        super(command);
        this.id = id;
    }
    
    @XmlElement
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
}
