/*
*Author: Curtis Slone
*File: rsaGeneration.java
*Date: 10 Jul 2021
* Generate RSA(2048) key pair, export as Base 64 Encoded Files
* Perform OS independent Operations
* TODO: ADD EXCEPTION SO PROGRAM FAILS GRACEFULLY WHEN USER HITS CANCEL
*/

package com.piip.backend;

import java.security.KeyPairGenerator;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;

public class RSAGeneration {

    private String keyLocation;
    
    public RSAGeneration() throws FileNotFoundException, IOException, NoSuchAlgorithmException{
        
        //INITIALIZE CLASS ATTRIBUTES
        Base64.Encoder encoder = Base64.getEncoder(); 
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        Writer out;
        String[] pathNames = {"keys","priv.pem"};
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        byte[] encodedPublicKey = pub.getEncoded();
        Key pvt = kp.getPrivate();
        String fileSeparator = FileSystems.getDefault().getSeparator();
        String pubOutFile = String.join(fileSeparator,pathNames);
        
        //JFILECHOOSER SET TO DIRECTORY ONLY TO MAINTAIN priv.key FORMAT
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        
        //PERFORM JFILECHOOSER OPERATION TO OUTPUT PRIVATE KEY IN DISK OF CHOICE
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {

                // GET PATH IN SYSTEM INDEPENDENT METHOD
                String selectedPath = jfc.getSelectedFile().toString();
                String[] privPathNames = {selectedPath,"pub.pem" };
                String privOutFile = String.join(fileSeparator,privPathNames);
                keyLocation = privOutFile;
                System.out.println(keyLocation);
                out = new FileWriter(privOutFile);

                //BEGIN WRITE OPERATIONS
                out.write("-----BEGIN RSA KEY-----\n");
                out.write(encoder.encodeToString(encodedPublicKey));
                out.write("\n-----End RSA KEY-----\n");

                //CLOSE FILE
                out.close();
            }
        }
        System.out.println("Happened");
            
        // WRITE PUBLIC KEY TO BASE 64 ENCODED FILE
        out = new FileWriter(pubOutFile);

        //BEGIN WRITE OPERATIONS
        out.write("-----BEGIN RSA KEY-----\n");
        out.write(encoder.encodeToString(pvt.getEncoded()));
        out.write("\n-----End RSA KEY-----\n");

        //CLOSE FILE
        out.close();
    }

    //RETURN KEY LOCATION
    public String getKeyLocation(){
        return keyLocation;
    }
}
