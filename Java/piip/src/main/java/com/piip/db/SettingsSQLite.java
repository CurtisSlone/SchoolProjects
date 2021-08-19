/*
*Author:Curtis Slone
*File: SettingsSQLite.java
*Date: 30 Jul 2021
* Handles all settings SQLite methods
*/
package com.piip.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.JDBC;

public class SettingsSQLite {
    private static Connection conn;
    private static boolean hasData = false;

    //Display Event Log
    public static ResultSet displayEvent() throws ClassNotFoundException, SQLException{
        if(conn == null){
            getConnection();
        }

        Statement state = conn.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM settings");
        return res;
    } // END

    //GET CONNECTION
    private static void getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:Settings.db");
        initialise();
    } //END

//INIT DB
    private static void initialise() throws SQLException{
        if(!hasData){
            hasData = true;
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='settings'");
            if (!res.next()){
                System.out.println("Building the EventLog");
                //BUILD TABLE
                Statement state2 = conn.createStatement();
                state2.execute("CREATE TABLE settings(id integer,"
                                + "Initialized integer,"
                                + "OS varchar(120),"
                                + "pubKeyLoc varchar(120),"
                                + "password varchar(128),"
                                + "pubKeyHash varchar(120),"
                                + "privKeyHash varchar(120),"
                                + "primary key(id));");
                //INSERT DUMMY DATA
                PreparedStatement prep = conn.prepareStatement("INSERT INTO settings values(?,?,?,?,?,?,?);");
                
                prep.setString(2, "0");
                prep.setString(3, "<OS>");
                prep.setString(4,"<PubKey Location");
                prep.setString(5,"<Password>");
                prep.setString(6,"<PubKey Hash>");
                prep.setString(7,"<PrivKey Hash>");
                prep.execute();
            }
        }
    }//END INIT

    //INITIAL UPDATE
    public static void updateSettings(String os, String pubKeyLoc, String password, String pubKeyHash, String privKeyHash ) throws ClassNotFoundException, SQLException {
        if (conn == null){
            getConnection();
        }
        PreparedStatement prep = conn.prepareStatement("UPDATE settings SET Initialized = ?,"
                                                        + "OS = ?,"
                                                        + "pubKeyLoc = ?,"
                                                        + "password = ?,"
                                                        + "pubKeyHash = ?,"
                                                        + "privKeyHash = ?"
                                                        + "WHERE id = 1");
        prep.setInt(1,1);
        prep.setString(2, os);
        prep.setString(3,pubKeyLoc);
        //TODO: MAKE PASS HASH AND ENCRYPT HASH TO SUPPORT 2FA
        prep.setString(4,password);
        prep.setString(5,pubKeyHash);
        prep.setString(6,privKeyHash);
        prep.execute();
    } //END INITIAL UPDATE

    //VERIFY INITIALIZED
    public static boolean verifyInit() throws ClassNotFoundException, SQLException {
        if (conn == null){
            getConnection();
        }

        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery("SELECT Initialized FROM settings WHERE id = 1");
        System.out.println(result.getInt(1));
        System.out.println(result.getInt(1) == 1);
        return result.getInt(1) == 1;
    }
    //VERIFY PASSWORD
    //VERIFY PUB KEY
    //VERIFY PRIV KEY
    //VERIFY PUB KEY LOCATION
    //VERIFY OS

}
