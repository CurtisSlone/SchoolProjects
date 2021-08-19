/*
*Author: Curtis Slone
*File: FileDecryption.java
*Date: 10 Jul 2021
* Decrypt File using AES symmetric key, uses RSA private key decrypt AES Cipher & IV
* TODO: Add feature that allows selection of files that have .enc extension only
*/

package com.piip.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.URIReferenceException;

import com.piip.backend.Logger.LogEvents;

import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileDecryption {
	
    static public void doDecryptRSAWithAES(PublicKey pubKey, String inputFile)
	throws java.security.NoSuchAlgorithmException,
	       java.security.InvalidAlgorithmParameterException,
	       java.security.InvalidKeyException,
	       java.security.spec.InvalidKeySpecException,
	       javax.crypto.NoSuchPaddingException,
	       javax.crypto.BadPaddingException,
	       javax.crypto.IllegalBlockSizeException,
	       java.io.IOException
    {
	try (FileInputStream in = new FileInputStream(inputFile)) {
		SecretKeySpec skey = null;
		{
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.DECRYPT_MODE, pubKey);
		    byte[] b = new byte[256];
		    in.read(b);
		    byte[] keyb = cipher.doFinal(b);
		    skey = new SecretKeySpec(keyb, "AES");
		}

		byte[] iv = new byte[128/8];
		in.read(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);

		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
		if(inputFile.matches(".*([\\.enc]{2,}$)")){
			// CASE IN WHICH FILE HAS MORE THAN ONE .enc (FILE WAS ENCRYOPTED MORE THAN ONCE)
		}
		//REMOVES ALL .enc, Leaves content with regular file extension but still encrypted
        String decryptFile = inputFile.replace(".enc","");
		try (FileOutputStream out = new FileOutputStream(decryptFile)){
			ProcessFile.processFile(ci, in, out);
		    }
			//ERROR: DELETES FILE IF NOT ENCRYPTED
			Files.delete(Paths.get(inputFile));
	    }
			try{
			String event = Logger.getEvent(LogEvents.DECRYPTION, inputFile);
			Logger.logEvent(event);
		} catch (URISyntaxException uri) {
			uri.getMessage();
		} catch (SQLException sqle) {
			sqle.getMessage();
		} catch (ClassNotFoundException cnfe) {
			cnfe.getMessage();
		}
    }
} 