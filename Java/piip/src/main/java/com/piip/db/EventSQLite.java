/*
*Author:Curtis Slone
*File: EventSQLite.java
*Date: 30 Jul 2021
* Handles all eventLog SQLite methods
*/
package com.piip.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.JDBC;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.File;

public class EventSQLite {
    private static Connection conn;
    private static boolean hasData = false;
    private static String fileSeparator = FileSystems.getDefault().getSeparator();

    //Display Event Log
    public static ResultSet displayEvent() throws ClassNotFoundException, SQLException{
        if(conn == null){
            getConnection();
        }

        Statement state = conn.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM eventLog");
        return res;
    } // END

    //GET CONNECTION
    private static void getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:EventLog.db");
        initialise();
    } //END

//INIT DB
    private static void initialise() throws SQLException{
        if(!hasData){
            hasData = true;
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='eventLog'");
            if (!res.next()){
                System.out.println("Building the EventLog");
                //BUILD TABLE
                Statement state2 = conn.createStatement();
                state2.execute("CREATE TABLE eventLog(id integer,"
                                + "date varchar(120),"
                                + "time varchar(120),"
                                + "event varchar(120),"
                                + "digSig varchar(120),"
                                + "primary key(id));");
                //INSERT DUMMY DATA
                PreparedStatement prep = conn.prepareStatement("INSERT INTO eventLog values(?,?,?,?,?);");
                prep.setString(2, "dd/MM/yyy");
                prep.setString(3,"HH:mm:ss");
                prep.setString(4,"<EVENT>");
                prep.setString(5,"<DIGITAL SIGNATURE>");
                prep.execute();
            }
        }
    }//END INIT

    //ADD EVENT
    public static void addEvent(String date, String time, String event, String digSig) throws ClassNotFoundException, SQLException {
        if (conn == null){
            getConnection();
        }
        PreparedStatement prep = conn.prepareStatement("INSERT INTO eventLog values(?,?,?,?,?);");

        prep.setString(2, date);
        prep.setString(3,time);
        prep.setString(4,event);
        prep.setString(5,digSig);
        prep.execute();
    } //END ADD EVENT

    //EXPORT LOG
    public static void export() throws IOException, URISyntaxException, SQLException, ClassNotFoundException, SQLException, ClassNotFoundException{
        String fileName = new File("").getAbsolutePath();
        fileName = fileName + fileSeparator + "eventLog.csv";
        String sqlSelect = "SELECT * FROM eventLog";
        if(conn == null){
            getConnection();
        }
        Statement state = conn.createStatement();
        ResultSet results = state.executeQuery(sqlSelect);

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
        // Add table headers to CSV file.
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
        .withHeader(results.getMetaData()).withQuoteMode(QuoteMode.ALL));

        // Add data rows to CSV file.
        while (results.next()) {
            csvPrinter.printRecord(
                    results.getInt(1),
                    results.getString(2),
                    results.getString(3),
                    results.getString(4),
                    results.getString(5));
        }

        // Close CSV file.
        csvPrinter.flush();
        csvPrinter.close();
        

    }//END EXPORT LOG
}