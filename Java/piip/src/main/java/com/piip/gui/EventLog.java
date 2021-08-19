/*
*Author: Mueed Chaudhry, Curtis Slone
*File: EventsLog.java
*Date: 12 Jul 2021
* Generates Log View in separate window, called by MainUseFrame
*/
package com.piip.gui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;

import com.piip.backend.Logger;




class EventLog extends JPanel implements ActionListener {

    //CLASS ATTRIBUTES
    private String[] logEvents;
    private DefaultTableModel model;
    private static int counter = 0;

    
    //Constructor
    public EventLog(){

        //LOAD LOG DATA
        try {
        refreshLog();
        // GENERATE LOG DETAILS FOR GUI
        generateLog();
        
        } catch (IOException ioe){
            ioe.getMessage();
        } catch (URISyntaxException uri) {
            uri.getMessage();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.getMessage();
        }
        
        

        JTable table = new JTable(model);
        // INIT TABLE
        table.setPreferredScrollableViewportSize(new Dimension(450,450));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        
        JButton btnCloseLog = new JButton("Close Log");
        btnCloseLog.addActionListener(this);
        add(btnCloseLog);
        add(scrollPane);
    }//END CONSTRUCTOR

    //
    //EVENT HANDLERS
    //

    //ACTION EVENT
    public void actionPerformed(ActionEvent ae){
        this.setVisible(false);
        remove(this);
    } // END ACTION EVENT

    
    //
    //METHODS
    //
    private void refreshLog() throws IOException, URISyntaxException, SQLException, ClassNotFoundException{
        this.logEvents = Logger.readLog();
    }

    private void generateLog(){
        //TABLE HEADERS
        String [] header ={"Date","Time","Event","Digital Signature"};
        //GENERATE 2-D ARRAY OF DATA
        Vector<String> logData = new Vector<String>();
        Collections.addAll(logData, logEvents);
        String[][] tmp = new String[logData.size()][4];
        logData.forEach((n)->{
           tmp[counter] = (n.split(","));
           EventLog.counter++;
        });
        //TABLE MODEL & TABLE
        model = new DefaultTableModel(tmp,header);
        //ITERATE THROUGH LOG EVENTS, TRANSFORM STRING INTO VECTOR AND ADD TO DEFAULT TABLE MODEL
        EventLog.counter = 0;
    }
}