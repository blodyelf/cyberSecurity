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
                    
                    if(line.trim().equals("HTTP/1.1 504\r\n")){
                        System.out.println("Server timed out!");
                        break;
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
                            printWriter.println("LUL");
                            printWriter.flush();
                            break;
                        }
                        
                        if(line.trim().equals("HTTP/1.1 504\r\n")){
                            System.out.println("Server timed out!");
                            break;
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
                        
                        if(line.trim().equals("HTTP/1.1 504\r\n")){
                            System.out.println("Server timed out!");
                            break;
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
            
                if(username.contains(":|\"") || password.contains(":|\"")){
                    valid = false;
                    System.out.println("Invalid characters used! (\" or :)");
                }
            }
            
            return "Username: \"" + username + "\", Password: \"" + password + "\"";
        }
        
        private String getUserData(){
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
            
                if(username.contains(":|\"") || password.contains(":|\"")){
                    valid = false;
                    System.out.println("Invalid characters used! (\" or :)");
                }
            }
            
            return "Username: \"" + username + "\", Password: \"" + password + "\"";
        }
    }
}