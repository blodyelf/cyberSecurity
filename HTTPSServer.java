import data_classes.*;
import exceptions.*;
import security.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
 
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
 
public class HTTPSServer {
    private int port = 9999;
    private boolean isServerDone = false;
     
    public static void main(String[] args){
        HTTPSServer server = new HTTPSServer();
        server.run();
    }
     
    HTTPSServer(){      
    }
     
    HTTPSServer(int port){
        this.port = port;
    }
     
    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("KeyStore.jks"),"myKeystorePassword123".toCharArray());
             
            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "myKeystorePassword123".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();
             
            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();
             
            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(km,  tm, null);
             
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
         
        return null;
    }
     
    // Start to run the server
    public void run(){
        SSLContext sslContext = this.createSSLContext();
         
        try{
            // Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
             
            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);
             
            System.out.println("SSL server started");
            while(!isServerDone){
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                 
                // Start the server thread
                new ServerThread(sslSocket).start();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     
    // Thread handling the socket from client
    static class ServerThread extends Thread {
        private SSLSocket sslSocket = null;
         
        ServerThread(SSLSocket sslSocket){
            this.sslSocket = sslSocket;
        }
         
        public void run(){
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
             
            try{
                // Start handshake
                sslSocket.startHandshake();
                 
                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();
                 
                System.out.println("SSLServerSession :");
                System.out.println("\tServer-Protocol : "+sslSession.getProtocol());
                System.out.println("\tServer-Cipher suite : "+sslSession.getCipherSuite());
                 
                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();
                 
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                 
                // Ask for action
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    
                    if(!line.trim().isEmpty())
                        System.out.println("Client-Inut : "+line);
                    
                    if(line.trim().equals("Hello server, i am dood!")){
                        printWriter.println("Hello dood, how can i help?");
                        printWriter.println();
                        printWriter.flush();
                        break;
                    }
                }
                
                String action = ""; 
                // acknowledge action 
                while(true){
                    line = bufferedReader.readLine();
                    
                    if(!line.trim().isEmpty())
                        System.out.println("Client-Inut : "+line);
                    
                    if(line.trim().equals("authenticate")){
                        action = "authenticate";
                        printWriter.println("You want to authenticate!");
                        printWriter.println();
                        printWriter.flush();
                        break;
                    }
                    
                    if(line.trim().equals("register")){
                        action = "register";
                        printWriter.println("You want to register!");
                        printWriter.println();
                        printWriter.flush();
                        
                        break;
                    }
                    
                    if(line.trim().equals("recover")){
                        action = "recover";
                        printWriter.println("You want to recover your account!");
                        printWriter.println();
                        printWriter.flush();
                        break;
                    }
                }
                
                // Handle action
                if(action.equals("authenticate")) { // AUTHENTICATION
                    Authenticator auth = new Authenticator();
                    try{
                        while(true){
                            line = bufferedReader.readLine();
                            
                            if(!line.trim().isEmpty()) {
                                System.out.println("Client-Inut : " + line);
                                
                                int separator = line.indexOf(", ");
                                String username = line.substring(0, separator);
                                String password = line.substring(separator + 2);
                                
                                //System.out.println(username + ",|" + password);
                                if(auth.authenticate(username, password)) {
                                    
                                    break;
                                }
                                
                                printWriter.println("Failed!");
                                printWriter.println();
                                printWriter.flush();
                            }
                        }
                    } catch (DOSAttemptException dos) {
                        printWriter.println("HTTP/1.1 504");
                        printWriter.println();
                        printWriter.flush();
                        sslSocket.close();
                        return;
                    }
                } else if (action.equals("register")){ // REGISTER
                    while(true){
                        line = bufferedReader.readLine();
                        
                        if(!line.trim().isEmpty()) {
                            System.out.println("Client-Inut : "+line);
                            register(line);
                            break;
                        }
                    }
                } else if(action.equals("recover")) { // RECOVERY
                    while(true){
                        line = bufferedReader.readLine();
                        
                        if(!line.trim().isEmpty()) {
                            System.out.println("Client-Inut : "+line);
                            break;
                        }
                    }
                }
                
                // Write data
                printWriter.print("SUCCESS!");
                printWriter.print("HTTP/1.1 200\r\n");
                printWriter.flush();
                 
                //sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        public void register(String str) {
            String[] words = str.split(", ");
            int i = 0;
            for(String w : words) {
                System.out.println(i++ + ": " + w);
            }
            
            String[] data = new String[17];
            for(i = 0; i < 17; i++){
                data[i] = words[i];
            }
            
            register(new UserData(data), words[17], words[18], words[19]);
        }
        
        public void register(UserData userData, String password, String secretQuestion, String secretAnswer) {
            if(userData == null) {
                System.out.println("Error");
                return;
            }
            
            try {
                checkPasswordStrength(password);
            } catch (Exception e){
                e.printStackTrace();
                //sslOS.write(e.toString());
                //sslOS.flush();
                return;
            }  
            
            UserAccount userAccount = new UserAccount(userData, password, secretQuestion, secretAnswer);
            
            if(userAccount == null) {
                System.out.println("Error");
                return;
            }
        }
    }

    
    public static void checkPasswordStrength(String pass) throws WeakPasswordException, Exception{
        
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