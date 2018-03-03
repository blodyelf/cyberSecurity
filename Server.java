import java.net.*;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class Server{
    
    private static InputStream sslIS;
    private static OutputStream sslOS;
    
    public static void main(String args[]) throws IOException {
        
        int serverPort = 443;
        String keyAndTrustStoreClasspathPath = new String("keystore.jks");
        String passphrase = new String("MyKeystorePassword123");
        
        
        SSLServerSocketFactory factory = makeSSLSocketFactory(keyAndTrustStoreClasspathPath, passphrase.toCharArray());
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(serverPort);
        serverPort = serverSocket.getLocalPort();
        
        // Connection ready
        boolean serverReady = true;
        
        while(serverReady) {
            SSLSocket sslSocket = (SSLSocket) serverSocket.accept();
            
            System.out.println("Accepted: " + sslSocket);
            
            sslIS = sslSocket.getInputStream();
            sslOS = sslSocket.getOutputStream();
            
            //sslIS.read();
            sslOS.write(85);
            sslOS.flush();

            //sslSocket.close();
        }
    }
    
    private static SSLServerSocketFactory makeSSLSocketFactory(String keyAndTrustStoreClasspathPath, char[] passphrase) throws IOException {
        return null;
    }
    
    public void register(UserData userData, String password) {
        
        try{
            userData.saveToDatabase();
        } catch (InvalidPersonalDetails e) {
            //sslOS.write(e.toString());
            //sslOS.flush();
            return;
        }
        
        try {
            checkPassordStrength(password);
        } catch (Exception e){
            //sslOS.write(e.toString());
            //sslOS.flush();
            return;
        }
        
        
        
    }
    
    private void checkPassordStrength(String pass) throws WeakPasswordException, Exception{
        
        if(pass.length() < 8) 
            throw new WeakPasswordException("Password under 8 characters!");
        
        boolean digit = false,
                upper = false,
                lower = false,
                special = false;
        
        for(int i = 0; i < pass.length(); i++) {
            
            char c = pass.charAt(i);
            
            if(!Character.isDefined(c)) 
                throw new Exception("Undefined Unicode character!");
            
            if(!Character.isLetterOrDigit(c)) special = true;    
            else if(Character.isUpperCase(c)) upper = true;
            else if(Character.isLowerCase(c)) lower = true;
            else if(Character.isDigit(c)) digit = true;
        }
        
        if(!digit || !upper || !lower || ! special) {
            
            boolean ok = false;
            StringBuilder sb = new StringBuilder("The password must contain at least:");
            
            if(!digit) { 
                sb.append(" a digit");
                ok = true;
            }
            
            if(!upper) {
                if(ok) sb.append(",");
                ok = true;
                sb.append(" an upper case character");
            }
            if(!lower) {
                if(ok) sb.append(",");
                ok = true;
                sb.append(" an upper case character");
            }
            if(!special) {
                if(ok) sb.append(",");
                sb.append(" an upper case character");
            }
            
            sb.append(".");
            
            throw new WeakPasswordException(sb.toString());
        }
        
        
    }
    
    private void savePassword(String password){
        
    }

}