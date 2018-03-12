package exceptions;

import java.lang.Exception;

public class DOSAttemptException extends Exception {
    
    public DOSAttemptException() {
        super();
    }
    
    public DOSAttemptException(String message) {
        super(message);
    }
    
    public DOSAttemptException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DOSAttemptException(Throwable cause) {
        super(cause);
    }
}