package ir.bki.filters.auhtorize;


/**
 * Thrown if errors occur during the authorization process.
 *
 * @author cassiomolin
 */
public class AccessDeniedException extends RuntimeException {

    private StatusCode statusCode;

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, StatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
