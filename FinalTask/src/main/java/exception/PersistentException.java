package exception;

public class PersistentException extends Exception {
    public PersistentException() {}

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistentException(String message) {
        super(message);
    }

    public PersistentException(Throwable cause) {
        super(cause);
    }
}