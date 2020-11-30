import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * @author Juan David Carvajal
 * @author Alvaro Andres Gomez Rey
 * @author Juan David López
 * @author Julian Mabesoy
 */
public class Hello {

	public final static String SALT = "1234";
	public final static int ITERATIONS = 10000;
	public final static int KEY_LENGTH = 128;

	
	/*
	public static void main(String[] args) {	
		// PBKDF2 Settings
		String password = "mynameisandres";
        String salt = "1234";
        int iterations = 10000;
        int keyLength = 128;
        
        // Generate Key from Password
		byte[] key= FileEncrypterDecrypter.PBKDF2(password.toCharArray(), salt.getBytes(), iterations, keyLength);
		
		// Test
		try {
			FileEncrypterDecrypter.encrypt(key, new File("C:\\Users\\usuario\\Desktop\\test.docx"));
			FileEncrypterDecrypter.decrypt(key, new File("C:\\Users\\usuario\\Desktop\\test.docx.cif"), new File("C:\\Users\\usuario\\Desktop\\test-decif.docx"), new File("C:\\Users\\usuario\\Desktop\\test.docx.hash"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	*/

}
