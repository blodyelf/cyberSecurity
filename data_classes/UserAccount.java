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
            PasswordHash hasher = new PasswordHash();
            
            pass_hash = hasher.generatePasswordHash(password);
            answer_hash = hasher.generatePasswordHash(secretAnswer);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        boolean available = false;
        
        // get user logging ID 
        while(!available) {
            ID = 100000000 + (long) (Math.random() * 900000000);  
            
            available = checkUnique(ID);
        }
        
        accountData = new AccountData();
        this.userData = userData;
        
        saveToDatabase();
    }
    
    // Retrieve from database
    public UserAccount getUserAccount(String firstName, String lastName, String securityNumber, String password) throws IncorrectLoginDetails {
        
        throw new IncorrectLoginDetails();
        
        //return null;
    }
    
    public boolean checkUnique(long id) {
        
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        
        String sql = "SELECT * FROM user_authentication WHERE id = ?";
                     
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setLong(1, ID);
            
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next() ) {
                return true;
            } 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
        
        // Check database
        return false;
    }
    
    // Save to database 
    public boolean saveToDatabase(){
                   
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        
        String sql = "INSERT INTO user_authentication VALUES ( ? , ? , ? , ? )";
                     
  
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            System.out.println("Id: " + ID);
            pstmt.setLong(1, ID);
            pstmt.setString(2, pass_hash);
            pstmt.setString(3, secretQuestion);
            pstmt.setString(4, answer_hash);
            
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }   
        
        if(!userData.saveToDatabase(ID)) {
            deleteFromDatabase(conn);
            return false;
        }   
        return true;
    }
    
    public boolean deleteFromDatabase(Connection conn){
        String sql = "DELETE FROM user_authentication WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);    
            pstmt.setLong(1, ID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }   
        
        return true;
    }
    
    
}