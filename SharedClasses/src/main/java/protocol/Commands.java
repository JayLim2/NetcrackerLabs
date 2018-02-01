/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * commamds in coommand packet 
 * all packets possess a command field which is used to determine its type
 * based on type the packet has additional arguments
 * packet - command - args
 * AddAuthorPacket - ADD_AUTHOR - an Author
 * AddBookPacket - ADD_BOOK - a Book and an id of the author to attach book to
 * RemoveAuthorPacket - REMOVE_AUTHOR - an id of the author to remove
 * RemoveBookPacket - REMOVE_BOOK - an id of the book to remove
 * SearchPacket - SEARCH - a BookFilter object
 * SetAuthorPacket - SET_AUTHOR - an author and an id of the author to change
 * SetBookPacket - SET_BOOK - a book ,an id of the new author and id of thebook to change
 * @author Alexander
 */
@XmlRootElement
@XmlEnum(String.class)
public enum Commands {
    ADD_AUTHOR, ADD_BOOK, SET_AUTHOR, SET_BOOK, REMOVE_BOOK, REMOVE_AUTHOR, VIEW_BOOKS, VIEW_AUTHORS, BYE, SEARCH
}
