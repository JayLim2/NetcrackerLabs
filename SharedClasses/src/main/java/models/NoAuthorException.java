package models;

/**
 * Throws if author is not selected in modification form.
 *
 * @author Sergey Komarov
 */
public class NoAuthorException extends Exception {
    /**
     * Creates new instance of <code>NoAuthorException</code>
     * withoud detail information about error.
     */
    public NoAuthorException() {
        super();
    }

    /**
     * Creates new instance of <code>NoAuthorException</code>
     *
     * @param message detail information about error
     */
    public NoAuthorException(String message) {
        super(message);
    }
}
