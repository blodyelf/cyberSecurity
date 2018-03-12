package data_classes;

import exceptions.*;
import security.*;
import database.*;
import java.sql.*;

public class UserAccount {
                   
    // Password and security
    private long ID;
    private String pass_hash;
    private String secretQuestion;
    private String answer_hash;
    
    // Data
    private UserData userData;
    private AccountData accountData;
    
    
    // create account
    public UserAccount(UserData userData, String password, 
            String secretQuestion, String secretAnswer) {
        
        try{
            pass_hash = new PasswordHash().generatePasswordHash(password);
            answer_hash = new PasswordHash().generatePasswordHash(answer_hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        boolean available = false;
        
        // get user logging ID 
        while(!available) {
            ID = 1000000000 + (long) (Math.random() * 9000000000l);  
            
            available = !checkExistingInDatabase(ID);
        }
        
        accountData = new AccountData();
        
        saveToDatabase();
    }
    
    // Retrieve from database
    public UserAccount getUserAccount(String firstName, String lastName, String securityNumber, String password) throws IncorrectLoginDetails {
        
        throw new IncorrectLoginDetails();
        
        //return null;
    }
    
    public boolean checkExistingInDatabase(long id) {
        
        // Check database
        return false;
    }
    
    // Save to database 
    public boolean saveToDatabase(){
        
        if(!userData.saveToDatabase(ID)) 
            return false;
        
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        
        String sql = "INSERT INTO user_authentication VALUES ( ? , ? , ? , ? )";
                     
  
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setLong(1, ID);
            pstmt.setString(2, pass_hash);
            pstmt.setString(3, secretQuestion);
            pstmt.setString(4, answer_hash);
            
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }   
        // Handle this
        return true;
    }
}