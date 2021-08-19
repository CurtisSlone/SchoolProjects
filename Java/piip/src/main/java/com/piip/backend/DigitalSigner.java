/*
*Author: Curtis Slone
*File: Logger.java
*Date: 14 Jul 2021
* Manage Application Log Events 
*/
package com.piip.backend;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DigitalSigner {

    
    private static LoadRSAKeyFromFile lrsa;
    

    //SIGNATURE MAKER
    public static byte[] makeDigitalSignature(byte[] hash) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
       
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, lrsa.getPrivateKey());
        byte[] sigAsBin = cipher.doFinal(hash);
        return sigAsBin;
    } //END SIGNATURE MAKER

    //VERIFY SIGNATURE
    public static boolean verifySignature(byte[] sig, byte[] hash) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, lrsa.getPublicKey());
        byte[] hashAsBin = cipher.doFinal(sig);
        return hashAsBin.equals(hash);
    }//END VERIFY SIGNATURE

    //RECEIVE LOADED KEY OBJECT
    public static void recieveKeyLoaderObject(LoadRSAKeyFromFile kl){
        lrsa = kl;
    }
}