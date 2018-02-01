/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * responses in response packet 
 * all packets possess a response field which is used to determine its type
 * based on type the packet has additional arguments
 * packet - response - args
 * OkPacket - OK - none
 * ErrorPacket - Error - String describing error
 * ViewBooksResponsePacket - OK - AuthorContainer object 
 * @author Alexander
 */
@XmlRootElement
@XmlEnum(String.class)
public enum Responses {
    OK,ERROR
}
