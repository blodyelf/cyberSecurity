import java.util.*;

public class UserData {
    
    // Personal Data
    private boolean residentUK;
    private boolean over18;
    private boolean alreadyExisting;
    
    private String title, 
        firstName,
        middleName,
        lastName,
        dateOfBirth,
        gender,
        maritalStatus,
        livingSituation,
        motherMaidenName,
        nationality,
        countryOfBirth,
        cityOfBirth,
        previousAddressPostcode,
        insuranceNumber,
        mobileNumber;
    
    public UserData(boolean residentUK, boolean over18, boolean alreadyExisting, 
                    String title, String firstName, String middleName, String lastName,
                    String dateOfBirth, String gender, String maritalStatus,
                    String livingSituation, String motherMaidenName, String nationality,
                    String countryOfBirth, String cityOfBirth, String previousAddressPostcode, 
                    String insuranceNumber, String mobileNumber){
        
        this.residentUK = residentUK;
        this.over18 = over18;
        this.alreadyExisting = alreadyExisting;
    
        this.title = title; 
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.livingSituation = livingSituation;
        this.motherMaidenName = motherMaidenName;
        this.nationality = nationality;
        this.countryOfBirth = countryOfBirth;
        this.cityOfBirth = cityOfBirth;
        this.previousAddressPostcode = previousAddressPostcode;
        this.insuranceNumber = insuranceNumber;
        this.mobileNumber = mobileNumber;
    }
    
    // Save data to database
    public void saveToDatabase() throws InvalidPersonalDetails {
        
        if(!validPersonalData()) 
            throw new InvalidPersonalDetails();
        
        // Save to database
    }
    
    // Get data from database
    public static UserData getUserData() {
        
        // Access database and give user details or throw exception
        return null;
    }
    
    // This should probably be checked by hand/ by another entity 
    private boolean validPersonalData() {
        
        return true;
    }
    
    
    
}