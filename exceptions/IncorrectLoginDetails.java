package exceptions;

import java.lang.Exception;

public class IncorrectLoginDetails extends Exception {
    public IncorrectLoginDetails(){
        super("Incorrect login details");
    }
}