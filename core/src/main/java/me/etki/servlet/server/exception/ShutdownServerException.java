package me.etki.servlet.server.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ShutdownServerException extends Exception {

    public ShutdownServerException() {
    }

    public ShutdownServerException(String message) {
        super(message);
    }

    public ShutdownServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShutdownServerException(Throwable cause) {
        super(cause);
    }

    public ShutdownServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
