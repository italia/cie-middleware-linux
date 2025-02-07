package it.ipzs.cieid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ProxyInfoManager {
	private Cipher aesCipher;
	private byte[] key;
	private byte[] iv;
	
	public ProxyInfoManager()
	{
		String hostName;
		try {
			hostName = InetAddress.getLocalHost().getHostName();

			String uuid = UUID.nameUUIDFromBytes(hostName.getBytes()).toString();
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			iv = "9/\\~V).A,lY&=t2b".getBytes(StandardCharsets.UTF_8);
			key = digest.digest(uuid.getBytes(StandardCharsets.UTF_8));		
			
			aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String encrypt(String plaintext)
	{
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        try {
			aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
			return Base64.getEncoder()
	                .encodeToString(aesCipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "";
	}

	
	public String decrypt(String ciphertext)
	{
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        try {
			aesCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
			return new String(aesCipher.doFinal(Base64.getDecoder().decode(ciphertext)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "";
	}
}
