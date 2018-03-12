package database;

import java.sql.*;
import org.sqlite.*;

public class DBCheck {
     

    private Connection conn;
     /**
     * @param args the command line arguments
     */
    
    
    // Create database
    public static void main(String[] args) {
        
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        Statement stmt = null;
        String sql;
        try{
  
            // USER_AUTHENTICATION TABLE
            System.out.println("Table: USER_AUTHENTICATION");
            System.out.println("id, pass_hash, security_question, answer_hash");
            stmt = conn.createStatement();
            sql = "SELECT * FROM user_authentication;";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
               System.out.print( rs.getInt(1) + ", ");
               System.out.print( rs.getString(2) + ", ");
               System.out.print( rs.getString(3) + ", ");
               System.out.println( rs.getString(4) + "");
            }
            System.out.println();
            System.out.println();
            
           
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try*/
    }
    
}