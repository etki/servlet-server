package me.etki.servlet.server.exception;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ContextPathOccupiedException extends Exception {

    public ContextPathOccupiedException() {
    }

    public ContextPathOccupiedException(String message) {
        super(message);
    }

    public ContextPathOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextPathOccupiedException(Throwable cause) {
        super(cause);
    }

    public ContextPathOccupiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
