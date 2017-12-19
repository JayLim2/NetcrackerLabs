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
public class YearOutOfBoundsException extends Exception {

    /**
     * Creates a new instance of <code>YearOutOfBoundsException</code> without
     * detail message.
     */
    public YearOutOfBoundsException() {
    }

    /**
     * Constructs an instance of <code>YearOutOfBoundsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public YearOutOfBoundsException(String msg) {
        super(msg);
    }
}
