import java.lang.Exception;

public IncorrectLoginDetails extends Exception {
    public IncorrectPassword(){
        super("Incorrect login details");
    }
}