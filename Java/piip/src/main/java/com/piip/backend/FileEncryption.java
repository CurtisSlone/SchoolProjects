/*
*Author: Curtis Slone
*File: FileEncryption.java
*Date: 10 Jul 2021
* Encrypt File using AES symmetric key, uses RSA private key encrypt AES Cipher & IV
* TODO: Add feature that deletes unencrypted files in an OS independent way
*/

package com.piip.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.piip.backend.Logger.LogEvents;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileEncryption {
    private static SecureRandom srandom = new SecureRandom();

    static public void doEncryptRSAWithAES(PrivateKey privKey, String inputFile)
	throws java.security.NoSuchAlgorithmException,
	       java.security.InvalidAlgorithmParameterException,
	       java.security.InvalidKeyException,
	       java.security.spec.InvalidKeySpecException,
	       javax.crypto.NoSuchPaddingException,
	       javax.crypto.BadPaddingException,
	       javax.crypto.IllegalBlockSizeException,
	       java.io.IOException
    { 
	System.out.println(inputFile);
	KeyGenerator kgen = KeyGenerator.getInstance("AES");
	kgen.init(256);
	SecretKey skey = kgen.generateKey();

	byte[] iv = new byte[128/8];
	srandom.nextBytes(iv);
	IvParameterSpec ivspec = new IvParameterSpec(iv);

	try (FileOutputStream out = new FileOutputStream(inputFile + ".enc")) {
		{
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, privKey);
		    byte[] b = cipher.doFinal(skey.getEncoded());
		    out.write(b);
		    System.err.println("AES Key Length: " + b.length);
		}

		out.write(iv);
		System.err.println("IV Length: " + iv.length);

		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
		try (FileInputStream in = new FileInputStream(inputFile)) {
			ProcessFile.processFile(ci, in, out);
		    }
	    }
		Files.delete(Paths.get(inputFile));
		String event = Logger.getEvent(LogEvents.ENCRYPTION, inputFile);
		try{
        Logger.logEvent(event);
		} catch (URISyntaxException uri){
			uri.getMessage();
		} catch (SQLException sqle) {
			sqle.getMessage();
		} catch (ClassNotFoundException cnfe) {
			cnfe.getMessage();
		}
    }
	
}