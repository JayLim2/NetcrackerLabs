package models;

/**
 * Throws if any of input fields is empty.
 *
 * @author Sergey Komarov
 */
public class EmptyFieldException extends Exception {
    /**
     * Creates new instance of <code>EmptyFieldException</code>
     * without detail information about error.
     */
    public EmptyFieldException() {
        super();
    }

    /**
     * Creates new instance of <code>EmptyFieldException</code>
     *
     * @param message the detail infromation about error
     */
    public EmptyFieldException(String message) {
        super(message);
    }
}
