/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Алескандр
 */
public class BookAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of <code>BookAlreadyExistsException</code> without
     * detail message.
     */
    public BookAlreadyExistsException() {
    }

    /**
     * Constructs an instance of <code>BookAlreadyExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public BookAlreadyExistsException(String msg) {
        super(msg);
    }
}
