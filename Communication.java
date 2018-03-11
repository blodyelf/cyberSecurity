import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;

import java.net.*;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class Communication{
    
    private static HTTPSServer server;
    private static HTTPSClient client;
    
    public Communication(){
        
    }
    
    public static void main(String args[]){
        
        Communication comm = new Communication();
        server = new HTTPSServer();
        client = new HTTPSClient();
        
        MyRunnable myClient = new MyRunnable(server);
        MyRunnable myServer = new MyRunnable(client);
        
        
        Thread tServer = new Thread(myServer);
        tServer.start();
        
        Thread tClient = new Thread(myClient);
        tClient.start();
    }
    
    private void doSmth() throws IOException{
        
       
    }
    
}

class MyRunnable implements Runnable {
        private HTTPSServer server;
        private HTTPSClient client;
        
        private boolean clientThread = true;
        
        public MyRunnable(HTTPSServer server){
            this.server = server;
            clientThread = false;
        }
        
        public MyRunnable(HTTPSClient client){
            this.client = client;
        }
        
        public void run(){
            if(clientThread) 
                client.run();
            else 
                server.run();
        }
    }