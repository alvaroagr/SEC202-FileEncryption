import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class FileEncrypterDecrypter {
	
	/**
	 * PBKDF2 implementation based on what the Public-Key Cryptography Standards say on the subject. <br>
	 * It will use HMAC-SHA512 as its pseudo-random function.
	 * @param password password from which a derived key is generated.
	 * @param salt sequence of bits. In this case, it receives a byte array.
	 * @param c number of desired iterations.
	 * @param length desired bit-length of the generated key.
	 * @return generated key as a byte array. 
	 * @author Alvaro A. Gomez Rey
	 */
	public static byte[] PBKDF2(char[] password, byte[] salt, int c, int length) {
		try {
			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, c, length);
			SecretKey key = kf.generateSecret(spec);
			return key.getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Ciphers a file utilizing a given 128-bit key.
	 * @param key Key used for the AES algorithm
	 * @param fileInputPath path in which the target file is located.
	 * @param fileOutputPath path in which the ciphered file will be written.
	 */
	public static void encrypt(byte[] key, String fileInputPath, String fileOutputPath) 
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher
		KeySpec ks = new SecretKeySpec(key, "AES");
		Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cf.init(Cipher.ENCRYPT_MODE, (SecretKeySpec) ks);
		
		// Initialize the Input and Output streams
		FileInputStream fis = new FileInputStream(fileInputPath);
		FileOutputStream fos = new FileOutputStream(fileOutputPath);
		
		// Determine the size of the buffer
		int bufferBytes = Math.min(fis.available(), 64);
		byte[] buffer = new byte[bufferBytes];
		
		// While remaining bytes still fit in a 64byte buffer.
		while(buffer.length == 64) {
			fis.read(buffer);
			byte[] encryptedBuffer = cf.update(buffer);
			fos.write(encryptedBuffer);
			bufferBytes = Math.min(fis.available(), 64);
			System.out.println(bufferBytes+"");
			buffer = new byte[bufferBytes];
		}
		// Last portion of data
		fis.read(buffer);
		byte[] encryptedBuffer = cf.doFinal(buffer);
		fos.write(encryptedBuffer);
		
		// Close the Input and Output streams
		fis.close();
		fos.close();
	}
	
	/**
	 * Deciphers a file utilizing a given 128-bit key.
	 * @param key Key used for the AES algorithm.
	 * @param fileInputPath path in which the target file is located.
	 * @param fileOutputPath path in which the deciphered file will be written.
	 */
	public static void decrypt(byte[] key, String fileInputPath, String fileOutputPath) 
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher
		KeySpec ks = new SecretKeySpec(key, "AES");
		Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cf.init(Cipher.DECRYPT_MODE, (SecretKeySpec) ks);
		
		// Initialize the Input and Output streams
		FileInputStream fis = new FileInputStream(fileInputPath);
		FileOutputStream fos = new FileOutputStream(fileOutputPath);
		
		// Determine the size of the buffer
		int bufferBytes = Math.min(fis.available(), 64);
		byte[] buffer = new byte[bufferBytes];
		
		// While remaining bytes still fit in a 64byte buffer.
		while(buffer.length == 64) {
			fis.read(buffer);
			byte[] encryptedBuffer = cf.update(buffer);
			fos.write(encryptedBuffer);
			bufferBytes = Math.min(fis.available(), 64);
			System.out.println(bufferBytes+"");
			buffer = new byte[bufferBytes];
		}
		// Last portion of data
		fis.read(buffer);
		byte[] encryptedBuffer = cf.doFinal(buffer);
		fos.write(encryptedBuffer);
		
		// Close the Input and Output streams
		fis.close();
		fos.close();
	}
	/**
	 * Encrypts a file utilizing a given 128-bit key.
	 * @param key Key used for the AES algorithm
	 * @param fileInputPath path in which the target file is located.
	 * @param out path in which the encrypted file will be written.
	 */
	public static void encrypt(byte[] key, File in, File out) 
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher
		KeySpec ks = new SecretKeySpec(key, "AES");
		Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cf.init(Cipher.ENCRYPT_MODE, (SecretKeySpec) ks);
		
		// Initialize the Input and Output streams
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		
		// Determine the size of the buffer
		int bufferBytes = Math.min(fis.available(), 64);
		byte[] buffer = new byte[bufferBytes];
		
		// While remaining bytes still fit in a 64byte buffer.
		while(buffer.length == 64) {
			fis.read(buffer);
			byte[] encryptedBuffer = cf.update(buffer);
			fos.write(encryptedBuffer);
			bufferBytes = Math.min(fis.available(), 64);
			System.out.println(bufferBytes+"");
			buffer = new byte[bufferBytes];
		}
		// Last portion of data
		fis.read(buffer);
		byte[] encryptedBuffer = cf.doFinal(buffer);
		fos.write(encryptedBuffer);
		
		// Close the Input and Output streams
		fis.close();
		fos.close();
	}
	
	/**
	 * Decrypts a file utilizing a given 128-bit key.
	 * @param key Key used for the AES algorithm.
	 * @param in path in which the target file is located.
	 * @param out path in which the decrypted file will be written.
	 */
	public static void decrypt(byte[] key, File in, File out) 
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		// Initialize the cipher
		KeySpec ks = new SecretKeySpec(key, "AES");
		Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cf.init(Cipher.DECRYPT_MODE, (SecretKeySpec) ks);
		
		// Initialize the Input and Output streams
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		
		// Determine the size of the buffer
		int bufferBytes = Math.min(fis.available(), 64);
		byte[] buffer = new byte[bufferBytes];
		
		// While remaining bytes still fit in a 64byte buffer.
		while(buffer.length == 64) {
			fis.read(buffer);
			byte[] encryptedBuffer = cf.update(buffer);
			fos.write(encryptedBuffer);
			bufferBytes = Math.min(fis.available(), 64);
			System.out.println(bufferBytes+"");
			buffer = new byte[bufferBytes];
		}
		// Last portion of data
		fis.read(buffer);
		byte[] encryptedBuffer = cf.doFinal(buffer);
		fos.write(encryptedBuffer);
		
		// Close the Input and Output streams
		fis.close();
		fos.close();
	}
}
