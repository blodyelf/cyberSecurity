package database;

import java.sql.*;
import org.sqlite.*;


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
        conn = null;
        Statement stmt = null;
        try {
            try{
                //Class.forName("org.sqlite.JDBC");
                DriverManager.registerDriver(new JDBC());
            } catch(Exception ex){
                ex.printStackTrace();
            }

            // create a connection to the database
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //System.out.println("Creating database...");
            
            //System.out.println("Connection to SQLite has been established.");
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    // Create database
    public static void main(String[] args) {
   
        Connection conn = null;
        Statement stmt = null;
        String sql;
        try{
            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Creating database...");
            
            // USER_AUTHENTICATION TABLE
            // Dropping old user_authentication table
            stmt = conn.createStatement();
            sql = "DROP TABLE user_authentication;";
            stmt.executeUpdate(sql);
            
            // Creating new user_authentication table
            stmt = conn.createStatement();
            sql = 
                "CREATE TABLE user_authentication( " +
                   "id INT PRIMARY KEY, " +
                   "pass_hash TEXT, " +
                   "secretQuestion TEXT, " +
                   "answer_hash TEXT " +
                ");";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully...");
            
            // USER_DATA TABLE
            // Dropping old user_data table
            stmt = conn.createStatement();
            sql = "DROP TABLE user_data;";
            stmt.executeUpdate(sql);
            
            // Creating new user_data table
            stmt = conn.createStatement();
            sql = 
                "CREATE TABLE user_data( " +
                   "id INT, " +
                   "title TEXT, " +
                   "firstName TEXT, " +
                   "middleName TEXT, " +
                   "lastName TEXT, " +
                   "dateOfBirth TEXT, " +
                   "gender TEXT, " +
                   "maritalStatus TEXT, " +
                   "livingSituation TEXT, " +
                   "motherMaidenName TEXT, " +
                   "nationality TEXT, " +
                   "countryOfBirth TEXT, " +
                   "cityOfBirth TEXT, " +
                   "previousAddressPostcode TEXT, " +
                   "insuranceNumber TEXT, " +
                   "mobileNumber TEXT, " +
                   "residentUK TEXT, " +
                   "over18 TEXT, " +
                   "verified BOOL, " +
                   "FOREIGN KEY (id) REFERENCES user_authentication(id) " +
                ");";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully...");
            
            // LOGS_AUTHENTICATION TABLE
            // Dropping old logs_authentication table
            stmt = conn.createStatement();
            sql = "DROP TABLE logs_authentication;";
            stmt.executeUpdate(sql);
            
            // Creating new logs_authentication table
            stmt = conn.createStatement();
            sql = 
                "CREATE TABLE logs_authentication( " +
                   "id INT PRIMARY KEY, " +
                   "text TEXT, " +
                   "date TEXT " +
                ");";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully...");
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