/*
*Author: Curtis Slone
*File: Logger.java
*Date: 14 Jul 2021
* Manage Application Log Events 
*/
package com.piip.backend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.DatatypeConverter;

import com.piip.db.EventSQLite;

import java.sql.ResultSet;
import java.sql.SQLException;



public class Logger {

    //EVENT CATEGORIES ENUM
    public static enum LogEvents {
        ENCRYPTION,
        DECRYPTION,
        APPLICATION_LAUNCH,
        APPLICATION_CLOSE,
        FILE_ADD,
        FILE_DELETE,
        INITIALIZAITON,
        GENERATE_LOG,
        KEY_GENERATE
    } //END ENUM

    //EVENT STATIC ATTRIBUTES
    public static LogEvents eventType;
    public static String eventString;
    private static SimpleDateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    private static DateTimeFormatter hms = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String fileSeparator = FileSystems.getDefault().getSeparator();
    private static String logFilePath = "operations" + fileSeparator + "log.log";
    private static String selectedPath;

    

    //
    // CLASS METHODS
    //

    //BUILD LOG
    //Argument: Event as Strings
    public static void logEvent(String eventAsString) throws NoSuchAlgorithmException, IOException, URISyntaxException, NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException, SQLException, ClassNotFoundException {
        //FUNCTION ATTRIBUTES
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        String fullEntry = (Logger.dmy.format(date)).toString() + (hms.format(now)).toString() + eventAsString;
        
        //BUILD LOG STRING TO HASH AND ENCRYPT USING PRIVATE KEY TO CREATE DIGITAL SIGNATURE
        String entryHash = Hasher.makeHash(fullEntry);
        byte[] hashBin = DatatypeConverter.parseHexBinary(entryHash);
        byte[] digitalSig = DigitalSigner.makeDigitalSignature(hashBin);
        

        //FINAL DIGITAL SIGNATURE
        String sigAsText = DatatypeConverter.printHexBinary(digitalSig);

        EventSQLite.addEvent(Logger.dmy.format(date).toString(), (hms.format(now)).toString(), eventAsString, sigAsText);
        
        
    } // END BUILD LOG

    //GET EVENT
    //Arguments: LogEvents Enum and File as string or empty string
    public static String getEvent(LogEvents e, String str) {
        String event = "";
        switch(e){
            case ENCRYPTION:
                event = "File Encryption: ";
                break;
            case DECRYPTION:
                event = "File Decryption: ";
                break;
            case APPLICATION_LAUNCH:
                event = "Application Launch";
                break;
            case APPLICATION_CLOSE:
                event = " Application Close";
                break;
            case FILE_ADD:
                event = "Added File: ";
                break;
            case FILE_DELETE:
                event = "Deleted File: ";
                break;
            case INITIALIZAITON:
                event = "Initialized Application";
                break;
            case GENERATE_LOG:
                event = "Generated Log As Text";
                break;
            case KEY_GENERATE:
                event = "Generated Keys";
                break;
        }
        return  event + str;
    } // END GET EVENT

    // READ JSON LOG
    public static String[] readLog() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        //READ FILE AS BYTE STREAM & CONVERT TO STRING
        ResultSet resSet = EventSQLite.displayEvent();
        String logContent = "";
        while(resSet.next()){
            logContent = logContent + resSet.getString("date") + ","
                        + resSet.getString("time") + ","
                        + resSet.getString("event") + ","
                        + resSet.getString("digSig") + ":::";

        }
        //SEPARATE ELEMENTS BY \n
        return logContent.split(":::");
    }
}