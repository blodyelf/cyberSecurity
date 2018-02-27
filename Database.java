public class Database {
    
    
    public static Connection getConnection() {
		String user;
		String passwrd;
		Connection conn;

		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException x)
		{
			System.out.println ("Driver could not be loaded");
		}

        //user = readEntry("Enter database account:");
		//passwrd = readEntry("Enter a password:");
        user = "ops$u1612728";
        passwrd = "myNewPassword";
		try
		{
			conn = DriverManager.getConnection(
               "jdbc:oracle:thin:@daisy.warwick.ac.uk:1521:daisy",user,passwrd);
			return conn;
		}
		catch(SQLException e)
		{
			System.out.println("Error retrieving connection");
			return null;
		}
}