package hr.java.production.exception;

/**
 * Takes checked type of exception
 */
public class Anomaly extends Exception { //oznacene
    /**
     * Takes a String type of message.
     *
     * @param message Type String.
     */
    public Anomaly(String message) {
        super(message);
    }

    /**
     * Takes a cause of exception.
     *
     * @param cause Type Throwable.
     */
    public Anomaly(Throwable cause) {
        super(cause);
    }

    /**
     * Takes message and cause of exception.
     *
     * @param message Type String.
     * @param cause   Type Throwable.
     */
    public Anomaly(String message, Throwable cause) {
        super(message, cause);
    }
}
