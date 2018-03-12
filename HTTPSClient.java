import java.io.BufferedReader;
import java.io.IOException;
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
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import exceptions.*;
 
public class HTTPSClient {
    private String host = "127.0.0.1";
    private int port = 9999;
     
    public static void main(String[] args){
        HTTPSClient client = new HTTPSClient();
        client.run();
    }
     
    HTTPSClient(){      
    }
     
    HTTPSClient(String host, int port){
        this.host = host;
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
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
             
            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);
             
            System.out.println("SSL client started");
            new ClientThread(sslSocket).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     
    // Thread handling the socket to server
    static class ClientThread extends Thread {
        private SSLSocket sslSocket = null;
         
        ClientThread(SSLSocket sslSocket){
            this.sslSocket = sslSocket;
        }
         
        public void run(){
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
             
            try{
                // Start handshake
                sslSocket.startHandshake();
                 
                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();
                 
                System.out.println("SSLClientSession :");
                System.out.println("\tClient-Protocol : "+sslSession.getProtocol());
                System.out.println("\tClient-Cipher suite : "+sslSession.getCipherSuite());
                 
                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();
                 
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                 
                // Write data
                printWriter.print("Hello server, i am dood!\n");
                printWriter.println();
                printWriter.flush();
                
                // Send desired action to server (authenticate/ register)
                String line = null;
                String authStr = "";
                while((line = bufferedReader.readLine()) != null){
                    
                    if(!line.trim().isEmpty())
                        System.out.println("Server-Inut : "+line);
                    
                    if(line.trim().equals("Hello dood, how can i help?")){
                        authStr = getAction();
                        printWriter.println(authStr);
                        printWriter.println();
                        printWriter.flush();
                        break;
                    }
                    
                    if(line.equals("HTTP/1.1 504\r\n")){
                        System.out.println("Server timed out!");
                        sslSocket.close();
                        return;
                    }
                }
                
                boolean authenticate = authStr.equals("authenticate") ? true : false;
                
                //System.out.println(authenticate + "");
                
                if(authenticate) { // Authentication
                    while((line = bufferedReader.readLine()) != null){
                        
                        if(!line.trim().isEmpty())
                            System.out.println("Server-Inut : "+line);
                        
                        if(line.trim().equals("You want to authenticate!")){
                            printWriter.println(getCredentials());
                            printWriter.println();
                            printWriter.flush();
                        }
                        
                        if(line.trim().equals("Failed!")) {
                            printWriter.println(getCredentials());
                            printWriter.println();
                            printWriter.flush();
                        }
                        
                        if(line.trim().equals("HTTP/1.1 504")){
                            System.out.println("Server timed out!");
                            sslSocket.close();
                            return;
                        }
                    }
                } else { // Register
                    while((line = bufferedReader.readLine()) != null){
                        
                        if(!line.trim().isEmpty())
                            System.out.println("Server-Inut : "+line);
                        
                        if(line.trim().equals("You want to register!")){
                            printWriter.println(getUserData());
                            printWriter.println();
                            printWriter.flush();
                            break;
                        }
                        
                        if(line.trim().equals("HTTP/1.1 504")){
                            System.out.println("Server timed out!");
                            sslSocket.close();
                            return;
                        }
                    }

                }
                
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        private String getAction(){
            boolean correct = false;
            int i = -1;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            
            while(!correct){
                
                correct = true;
                
                System.out.print("Enter action number (0 - authenticate, 1 - register): ");
                try{
                    i = Integer.parseInt(br.readLine());
                }catch(NumberFormatException nfe){
                    System.err.println("Invalid Format!");
                    correct = false;
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                
                if(i < 0 || 1 < i) {
                    correct = false;
                }
            }
            
            if(i == 0)
                return "authenticate";
            
            return "register";
        }
        
        private String getCredentials(){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            String  username = "", 
                    password = "";
            
            boolean valid = false;
            
            while(!valid) {
                valid = true;
                System.out.print("Username: ");
                try{
                    username = br.readLine().trim();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                
                System.out.print("Password: ");
                try{
                    password = br.readLine().trim();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }            
            
                if(username.contains(",|\\s") || password.contains(",|\\s")){
                    valid = false;
                    System.out.println("Invalid characters used! (space or ,)");
                }
            }
            
            return username + ", " + password;
        }
        
        private String getUserData(){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            /*String title, 
                firstName,
                middleName,
                lastName,
                dateOfBirth,
                gender,
                maritalStatus,
                livingSituation,
                motherMaidenName,
                nationality,
                countryOfBirth,
                cityOfBirth,
                previousAddressPostcode,
                insuranceNumber,
                mobileNumber;
            */
            String[] data = new String[20];
            while(true) {
       
                try{
                    System.out.print("Title: ");
                    data[0] = br.readLine().trim();
                    
                    System.out.print("First name: ");
                    data[1] = br.readLine().trim();
                    
                    System.out.print("Middle name: ");
                    data[2] = br.readLine().trim();
                    
                    System.out.print("Last name: ");
                    data[3] = br.readLine().trim();
                    
                    System.out.print("Date of birth: ");
                    data[4] = br.readLine().trim();
                    
                    System.out.print("Gender: ");
                    data[5] = br.readLine().trim();
                    
                    System.out.print("Marital status: ");
                    data[6] = br.readLine().trim();
                    
                    System.out.print("Living situation: ");
                    data[7] = br.readLine().trim();
                    
                    System.out.print("Mother maiden name: ");
                    data[8] = br.readLine().trim();
                    
                    System.out.print("Nationality: ");
                    data[9] = br.readLine().trim();
                    
                    System.out.print("Country of birth: ");
                    data[10] = br.readLine().trim();
                    
                    System.out.print("City of birth: ");
                    data[11] = br.readLine().trim();
                    
                    System.out.print("Previous address postcode: ");
                    data[12] = br.readLine().trim();
                    
                    System.out.print("Insurance number: ");
                    data[13] = br.readLine().trim();
                    
                    System.out.print("Mobile number: ");
                    data[14] = br.readLine().trim();
                    
                    System.out.print("Are you a resident of the UK?: ");
                    data[15] = br.readLine().trim();
                    
                    System.out.print("Are you over 18?: ");
                    data[16] = br.readLine().trim();
                    
                    System.out.println("Security! ");
                    
                    while(true) {
                        System.out.print("Password: ");
                        String firstPass =  br.readLine().trim();
                        
                        try{
                            HTTPSServer.checkPasswordStrength(firstPass);
                        } catch (WeakPasswordException wp) {
                            System.out.println(wp.getMessage());
                            continue;
                        } catch (Exception ex){
                            ex.printStackTrace();
                            continue;
                        }
                        
                        System.out.print("Retype password: ");
                        data[17] = br.readLine().trim();
                        
                        if(firstPass.equals(data[17])) 
                            break;
                        
                        System.out.println("Passwords not mathing!");
                    }
                    
                    System.out.print("Security question: ");
                    data[18] = br.readLine().trim();
                           
                    while(true) {
                        System.out.print("Security answer: ");
                        String firstAns = br.readLine().trim();
                        
                        if(firstAns.equals("")){
                            System.out.print("Please enter a security answer!");
                            continue;
                        }
                        
                        System.out.print("Retype security answer: ");
                        data[19] = br.readLine().trim();
                        
                        if(firstAns.equals(data[19])) 
                            break;
                        
                        System.out.println("Answers not mathing!");
                    }
                    
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                    continue;
                }
                            
                for(String str : data)
                    if(str.contains(", ")){
                        System.out.println("Invalid characters used! (,)");
                        continue;
                    }
                    
                // Show your data
                System.out.println("Your data:");
                for(int i = 0; i < 17; i++)
                        System.out.println(data[i] + ",");
                
                boolean confirm = false;
                while(true) {
                    System.out.print("Confirm(yes/no): ");
                    String confirmStr = null;
                    try{
                        confirmStr =  br.readLine().trim();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                        continue;
                    }
                    
                    if(confirmStr.equals("yes")) {
                        confirm = true;
                        break;
                    } 
                    
                    if(confirmStr.equals("no")) {
                        confirm = false;
                        break;
                    }
                }
                
                
                if(!confirm) 
                    continue; 
                else
                    break;
            }
            String response = "";
            
            for(int i = 0; i < 19; i++) {
                response += data[i] + ", ";
            }
            response += data[19];
            
            return response;
        }
    }
}