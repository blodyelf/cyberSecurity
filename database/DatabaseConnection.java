package database;

import java.sql.*;
import org.sqlite.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class DatabaseConnection {
     
    static final String JDBC_DRIVER = "org.sqlite.JDBC";  
    static final String DB_URL = "jdbc:sqlite:database/database.db";

    //  Database credentials
    static final String USER = "wondough";
    static final String PASS = "myUltraSecureSQLITEPassword123!";
     
    private Connection conn;
     /**
     * @param args the command line arguments
     */
    public DatabaseConnection(){
        /*conn = null;
        try {
            try{
                DriverManager.registerDriver(new JDBC());
            } catch(Exception ex){
                ex.printStackTrace();
            }
            
            // db parameters
            String url = "jdbc:sqlite:database/database.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }
    
    // Create database
    public static void main(String[] args) {
       //DatabaseConnection db = new DatabaseConnection();
       
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();
          
            String sql = "CREATE DATABASE database";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
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
        }//end try
       
    }
    
     /**
     * Connect to a sample database
     */
    public Connection getConnection() {
        return conn;
    }
    
    public String getPassword(String username) {
        
        return null;
        
    }
    
}