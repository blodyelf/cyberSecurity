import java.lang.Exception;

public class InvalidPersonalDetails extends Exception {
    
    public InvalidPersonalDetails(){
        super("Invalid personal details!");
    }
}