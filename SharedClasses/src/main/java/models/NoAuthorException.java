package models;

public class NoAuthorException extends Exception {
    public NoAuthorException() {
        super();
    }

    public NoAuthorException(String message) {
        super(message);
    }
}
