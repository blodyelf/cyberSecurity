import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Encryptor {
    
    // Encryptor data
    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "/secret/private.key";
    public static final String PUBLIC_KEY_FILE = "/public/public.key";
    
    // Generate keys
    public static void generateKeys(){
        
    }
    
}