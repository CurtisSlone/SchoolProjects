/*
*Author: Curtis Slone
*File: Hasher.java
*Date: 14 Jul 2021
* Create MD5 hash
*/
package com.piip.backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;




public class Hasher {
    
    public static String makeHash (String input) throws NoSuchAlgorithmException {
       
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            return hash;
    }

    public static boolean verifyHash (String testableInput,String comparableHash) throws NoSuchAlgorithmException {
        String hashedTestable;
        hashedTestable = makeHash(testableInput);
        return hashedTestable.equals(comparableHash);
    }
}