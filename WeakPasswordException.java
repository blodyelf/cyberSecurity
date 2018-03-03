import java.lang.Exception;

public class WeakPasswordException extends Exception {
    
    public WeakPasswordException() {
        super();
    }
    
    public WeakPasswordException(String message) {
        super(message);
    }
    
    public WeakPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WeakPasswordException(Throwable cause) {
        super(cause);
    }
}