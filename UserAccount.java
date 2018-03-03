public UserAccount {
                   
    // Password and security
    private String ID;
    private String pass_hash;
    
    // Data
    private UserData userData;
    private AccountData accountData;
    
    
    // create account
    public UserAccount(UserData userData, String password) {
        
        pass_hash = generatePasswordHash(password);
        
        boolean available = false;
        
        // get user logging ID 
        while(!available) {
            ID = "";
            for(int i = 0; i < 10; i++)
                ID += (int) (Math.random() * 10);  
            
            available = !checkExistingInDatabase(ID);
        }
        
        accountData = new AccountData();
        
        saveToDatabase();
    }
    
    // Retrieve from database
    public UserAccount getUserAccount(String firstName, String lastName, String securityNumber, String password) throws IncorrectLoginDetails {
        
        try {
            
        } catch (IncorrectLoginDetails e) {
            throw e;
        }
    }
    
    public boolean checkExistingInDatabase(String s) {
        
        // Check database
        return false;
    }
    
    // Save to database 
    public saveToDatabase(){
        
        // Handle this
    }
}