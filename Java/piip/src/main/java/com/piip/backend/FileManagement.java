/*
*Author: Curtis Slone
*File: FileManagement.java
*Date: 10 Jul 2021
* Manages User Files in Application Directory
*TODO: Change full path to name only
*/

package com.piip.backend;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.stream.Collectors;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.DefaultListModel;

import com.piip.backend.Logger.LogEvents;

import java.io.Writer;
import java.net.URISyntaxException;
import java.io.FileWriter;
import java.util.regex.Pattern;


public class FileManagement {

    private static String fileSeparator = FileSystems.getDefault().getSeparator();
    private static String homeDir = "docs" + fileSeparator;
    private static File homeAbsolute = new File(homeDir);
    private static List<File> allFiles;
    private static DefaultListModel<File> listModel;

    //Add Files
    public static void addFiles(File file) throws NoSuchAlgorithmException, IOException, URISyntaxException, BadPaddingException, NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException{
                String tmp = file.toString();
                String[] splitted;
                if (fileSeparator == "\\") {
                    splitted = tmp.split(Pattern.quote("\\"));
                } else {
                    splitted = tmp.split(fileSeparator);
                } 
                tmp = homeDir + splitted[splitted.length - 1];
                try {
                    String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    File temp = new File(tmp);
                    Writer out = new FileWriter(temp);
                    out.write(fileContent);
                    out.close();
                } catch (IOException e) {
                    e.getMessage();
                }
                try {
                    String event = Logger.getEvent(LogEvents.FILE_ADD, file.getAbsolutePath());
                    Logger.logEvent(event);
                } catch (NoSuchAlgorithmException nsae){
                    nsae.getMessage();
                } catch (IOException ioe) {
                    ioe.getMessage();
                } catch (URISyntaxException uri) {
                    uri.getMessage();
                } catch (SQLException sqle) {
                    sqle.getMessage();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.getMessage();
                }
                updateFileList();
    }//END ADDFILES


    // UPDATE FILE LIST
    public static void updateFileList(){
        try {
            allFiles = Files.list(Paths.get(homeDir))
                        .map(Path::toFile)
                        .collect(Collectors.toList());
            listModel = new DefaultListModel<File>();
            allFiles.forEach(listModel::addElement);
        } catch (IOException e) {
            e.getMessage();
        }
    }// END FILE LIST
    
    //DELETE FILE FROM DIRECTORY
    public static void removeFile(File file) throws NoSuchAlgorithmException, IOException, URISyntaxException, NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException,BadPaddingException, SQLException, ClassNotFoundException{
            try{
                Files.delete(Paths.get(file.getAbsolutePath()));
            } catch (IOException e) {
                e.getMessage();
            }
            
            String event = Logger.getEvent(LogEvents.FILE_DELETE, file.getAbsolutePath());
            Logger.logEvent(event);
            updateFileList();
    }//END METHOD

    // Return Home Absolute Path
    public static File getAbsolute(){
        return homeAbsolute;
    } //End Method
 
    public static DefaultListModel<File> getList(){
        return listModel;
    }
}