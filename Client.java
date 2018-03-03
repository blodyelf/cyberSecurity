import java.net.*;
import java.io.*;
import java.security.KeyStore;

public class Client{
    
    public static void main(String args[]) throws IOException {
        
        String https_url = "https://localhost:443";
        URL url;
        
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            
            
        }
    }
    
    private SSLSocketFactory createSslSocketFactory() throws IOException {
        
        try{        
            // load up the key store
            String STORETYPE = "JKS";
            String KEYSTORE = "keystore.jks";
            String STOREPASSWORD = "storepassword";
            String KEYPASSWORD = "keypassword";

            KeyStore ks = KeyStore.getInstance(STORETYPE);
            ks.load(MattermostClient.class.getResourceAsStream(KEYSTORE), STOREPASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, KEYPASSWORD.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            //		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            sslContext.init(null, null, null); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates

            return sslContext.getSocketFactory();// (SSLSocketFactory) SSLSocketFactory.getDefault();
            
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
    
    
}