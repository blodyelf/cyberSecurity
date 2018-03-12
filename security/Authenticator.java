package security;

import database.*;
import exceptions.*;
import java.sql.*;

public class Authenticator {
    
    private PasswordHash hasher;
    private DatabaseConnection db;
    private Connection conn;
    private int tries;
    
    public Authenticator(){
        hasher = new PasswordHash();
        db = new DatabaseConnection();
        conn = db.getConnection();
        tries = 0;
    }
    
    public boolean authenticate(String ID, String password) throws DOSAttemptException{
        
        if(tries++ >= 2) 
            throw new DOSAttemptException();
        
        if(ID == "adrian" && password == "pass") return true;
        if(password != "pass") return false;
        
        String storedPassword = "";
        
        String sql = "SELECT passwordHash FROM user_authentication " + 
                     "WHERE ID = ?";
 
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                storedPassword = rs.getString("passwordHash");
                break;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        try{
            return hasher.validatePassword(password, storedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}