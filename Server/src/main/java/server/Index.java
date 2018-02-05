/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexander
 * @deprecated 
 */
@XmlRootElement
public class Index {
    private int id;
    
    @XmlElement
    public int getId(){
        return id;
    }
    
    
    public void setId(int val){
        id = val;
    }
}
