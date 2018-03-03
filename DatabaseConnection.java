package net.sqlitetutorial;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
/**
 *
 * @author sqlitetutorial.net
 */
public class DatabaseConnection {
     
    private Connection conn;
     /**
     * @param args the command line arguments
     */
    public DatabaseConnection(){
        conn = null;
        try {
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
        }
    }
     /**
     * Connect to a sample database
     */
    public Connection getConnection() {
        return conn;
    }
    
}