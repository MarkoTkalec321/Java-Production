package hr.java.production.exception;

/**
 * Takes unchecked type of exception.
 */
public class Uncommon extends RuntimeException {
    /**
     * Takes a String type of message.
     *
     * @param message Type String
     */
    public Uncommon(String message) {
        super(message);
    }

    /**
     * Takes a cause of exception.
     *
     * @param cause Type Throwable.
     */
    public Uncommon(Throwable cause) {
        super(cause);
    }

    /**
     * Takes message and cause of exception.
     *
     * @param message Type String.
     * @param cause   Type Throwable.
     */
    public Uncommon(String message, Throwable cause) {
        super(message, cause);
    }
}
