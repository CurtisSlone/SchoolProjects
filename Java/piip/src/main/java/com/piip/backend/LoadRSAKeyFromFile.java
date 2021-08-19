/*
*Author: Curtis Slone
*File: loadRSAKeyFromFile.java
*Date: 11 Jul 2021
* Choose key files and extract public & private key in correct formats
* Perform OS independent Operations
*/

package com.piip.backend;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.nio.file.FileSystems;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;


public class LoadRSAKeyFromFile {
    // OBJECT ATTRIBUTES
    private PrivateKey privKey;
    private PublicKey pubKey;
    
    // Constructor
    public LoadRSAKeyFromFile() {
        //PATH ATTRIBUTES
        final String KEY_START = "-----BEGIN RSA KEY-----";
        final String KEY_END = "-----End RSA KEY-----";

        try {
        //KEY ATTRIBUTES
        KeyFactory kf = KeyFactory.getInstance("RSA");

        //File Separator For System Independent Directory Traversal
        String fileSeparator = FileSystems.getDefault().getSeparator();

        
        
        // Private Key Extraction - From Base64 to PKCS8
        String[] privPathName = {"keys","priv.pem"};
        String privKeyPath = String.join(fileSeparator,privPathName);
        File privKeyFile = new File(privKeyPath);
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(privKeyFile.getAbsolutePath()))); 
        privateKeyContent = privateKeyContent.replaceAll("\\n","").replace(KEY_START, "").replace(KEY_END,"");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        this.privKey = kf.generatePrivate(keySpecPKCS8);

        // //JFILECHOOSER INIT
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());
        jfc.setDialogTitle("Choose your public key");
        
        //PUBLIC KEY EXTRACTION
        //PERFORM JFILECHOOSER OPERATION TO RETRIEVE PRIVATE KEY INFO
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isFile()) {

                //Public Key Info
                String publicKeyContent = new String(Files.readAllBytes(Paths.get(jfc.getSelectedFile().getAbsolutePath())));
                publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace(KEY_START, "").replace(KEY_END, "");

                //Public Key Extraction - From Base64 to X509 standard
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
                this.pubKey = kf.generatePublic(keySpecX509);
            }
        }

        } catch( Exception e) {
            e.getMessage();
        }
       
    } //End Constructor

    public PublicKey getPublicKey(){
        return this.pubKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privKey;
    }
}