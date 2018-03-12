package data_classes;

import java.util.*;
import database.*;
import exceptions.*;
import java.sql.*;

public class UserData {
    
    // Personal Data
    private long userID;
    
    private String residentUK,
        over18,
        title, 
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
        
    private String data[];
    
    private boolean verified = false;
    
    public UserData(String title, String firstName, String middleName, String lastName,
                    String dateOfBirth, String gender, String maritalStatus,
                    String livingSituation, String motherMaidenName, String nationality,
                    String countryOfBirth, String cityOfBirth, String previousAddressPostcode, 
                    String insuranceNumber, String mobileNumber, String residentUK, String over18){
        
        this.residentUK = residentUK;
        this.over18 = over18;
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
        
        //this.userID = userID;
    }
    
    public UserData(String[] data) {
        
        this.title = data[0]; 
        this.firstName = data[1];
        this.middleName = data[2];
        this.lastName = data[3];
        this.dateOfBirth = data[4];
        this.gender = data[5];
        this.maritalStatus = data[6];
        this.livingSituation = data[7];
        this.motherMaidenName = data[8];
        this.nationality = data[9];
        this.countryOfBirth = data[10];
        this.cityOfBirth = data[11];
        this.previousAddressPostcode = data[12];
        this.insuranceNumber = data[13];
        this.mobileNumber = data[14];
        this.residentUK = data[15];
        this.over18 = data[16];
        
        this.data = new String[17];
        
        for(int i = 0; i < 17; i++){
            this.data[i] = data[i];
        }
        //this.userID = userID;
    }
    
    // Save data to database
    public boolean saveToDatabase(long userID) {
        
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        
        String sql = "INSERT INTO user_data VALUES (";
        
        for(int i = 0; i < 18; i++)
            sql += " ? ,";
        
        sql += "? )";
                     
  
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setLong(1, userID);
            int i = 0;
            for(i = 0; i < 17; i++){
                pstmt.setString(i + 2, data[i]);
            }
            pstmt.setBoolean(i + 2, verified);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }   
            
        return true;    
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