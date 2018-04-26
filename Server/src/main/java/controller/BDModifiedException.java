/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Алескандр
 */
public class BDModifiedException extends Exception {

    /**
     * Creates a new instance of <code>BDModifiedException</code> without detail
     * message.
     */
    public BDModifiedException() {
    }

    /**
     * Constructs an instance of <code>BDModifiedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BDModifiedException(String msg) {
        super(msg);
    }
}
