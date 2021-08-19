/*
*Author: Curtis Slone, Mueed Chaudhry
*File: MainUseFrame.java
*Date: 12 Jul 2021
* Main JFrame For File Management
*/
package com.piip.gui;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.piip.backend.FileManagement;
import com.piip.backend.LoadRSAKeyFromFile;
import com.piip.backend.DigitalSigner;
import com.piip.backend.FileDecryption;
import com.piip.backend.FileEncryption;
import com.piip.backend.Logger;
import com.piip.db.EventSQLite;
import com.piip.interfaces.AppViewListener;

public class MainUsePanel extends JPanel implements ActionListener, ListSelectionListener {
    

    //CLASS ATTRIUBUTES
    private JTextField selectedFileField;
    private JTextField encryptionStatusField;
    private JButton addNewFile;
    private JButton delNewFile;
    private JButton decFile;
    private JButton enFile;
    private JButton viewLog;
    private JButton btnOpenFile; 
    private JButton exportLog;
    private JButton closeApp;
    private LoadRSAKeyFromFile keyLoader;
    protected JList<File> filesList;
    private JScrollPane scrollView;
    private ListSelectionModel lsm;
    private AppViewListener avl;


    //DEFAULT CONSTRUCTOR
	public MainUsePanel() {
        
       //
        //Key Loader
        //
        keyLoader = new LoadRSAKeyFromFile();
        DigitalSigner.recieveKeyLoaderObject(keyLoader);
		//
        //Panel Instation
        //
        
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		//
        //Label instantiation
        //
        
        //ACTIONS LABEL
        JLabel actionLabel = new JLabel("Actions");
        actionLabel.setBounds(55,21,200,25);
        actionLabel.setFont(new Font("Lucida Grande", Font.BOLD, 30));
		this.add(actionLabel);
		
         //SELECTED FILE LABEL
         JLabel selectedFileLabel = new JLabel("Selected File:");
         selectedFileLabel.setBounds(70, 300, 91, 16);
         this.add(selectedFileLabel);
 
         //ENCRYPTION STATUS LABEL
         JLabel fileStatusLabel = new JLabel("Encryption Status:");
         fileStatusLabel.setBounds(58, 370, 120, 16);
         this.add(fileStatusLabel);

        //
        //Button instantiation
        //

        //ADD NEW FILE BUTTON
        addNewFile = new JButton("Add New File");
        addNewFile.setBackground(Color.PINK);
        addNewFile.setBounds(6, 60, 216, 29);
        this.add(addNewFile);

        //DELETE FILE BUTTON
        delNewFile = new JButton("Delete File");
        delNewFile.setBackground(Color.PINK);
        delNewFile.setBounds(6, 88, 216, 29);
        this.add(delNewFile);

        //DECRYPT FILE BUTTON
        decFile = new JButton("Decrypt File");
        decFile.setBackground(Color.PINK);
        decFile.setBounds(6, 116, 216, 29);
        this.add(decFile);

        //ENCRYPT FILE BUTTON
        enFile = new JButton("Encrypt File");
        enFile.setBackground(Color.PINK);
        enFile.setBounds(6, 144, 216, 29);
        this.add(enFile);

        //OPEN FILE BUTTON
        btnOpenFile = new JButton("Open file");
		btnOpenFile.setBackground(Color.PINK);
		btnOpenFile.setBounds(6, 172, 216, 29);
        this.add(btnOpenFile);

        // VIEW LOG BUTTON
        viewLog = new JButton("View Log");
        viewLog.setBackground(Color.PINK);
        viewLog.setBounds(6, 200, 216, 29);
        this.add(viewLog);

        // EXPORT LOG BUTTON
        exportLog = new JButton("Export Log");
        exportLog.setBackground(Color.PINK);
        exportLog.setBounds(6, 228, 216, 29);
        this.add(exportLog);

        //CLOSE APPLICAITON BUTTON
        closeApp = new JButton("Exit App");
        closeApp.setBackground(Color.PINK);
        closeApp.setBounds(6, 256, 216, 29);
        this.add(closeApp);

        //
        //Button Action Listeners
        //
        addNewFile.addActionListener(this);
        delNewFile.addActionListener(this);
        decFile.addActionListener(this);
        enFile.addActionListener(this);
        viewLog.addActionListener(this);
        exportLog.addActionListener(this);
        closeApp.addActionListener(this);
        

        //
        //Field Instantiation
        //
        
        //SELECTED FILE FIELD
        selectedFileField = new JTextField();
        selectedFileField.setBounds(6, 330, 216, 30);
        this.add(selectedFileField);
		selectedFileField.setColumns(10);

        //ENCRYPTION STATUS FIELD
        encryptionStatusField = new JTextField();
        encryptionStatusField.setBounds(6, 400, 216, 30);
        this.add(encryptionStatusField);
		encryptionStatusField.setColumns(10);

        //ITIALIZE JLIST
        FileManagement.updateFileList();
        filesList = new JList<File>(FileManagement.getList());
        lsm = filesList.getSelectionModel();
        lsm.setSelectionMode(0);
        lsm.addListSelectionListener(this);
        filesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        //INITIALIZE JSCROLLPANE
        scrollView = new JScrollPane(filesList);
        scrollView.setBounds(306, 18, 281, 420);
        scrollView.setViewportView(filesList);
        this.add(scrollView);
	} //END CONSTRUCTOR

    //
    //EVENT HANDLERS
    //

    //ACTION EVENT
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == addNewFile){
            try{
                this.addFile();
                } catch (IOException ioe){
                    ioe.getMessage();
                } catch (NoSuchAlgorithmException nsae){
                    nsae.getMessage();
                } catch (BadPaddingException bpe){
                    bpe.getMessage();
                } catch (NoSuchPaddingException nspe) {
                    nspe.getMessage();
                } catch (InvalidKeyException ike) {
                    ike.getMessage();
                } catch (IllegalBlockSizeException ibse) {
                    ibse.getMessage();
                } catch (URISyntaxException uri) {
                    uri.getMessage();
                }
                this.refreshList();
        } else if(ae.getSource() == delNewFile) {
            try {
                this.removeFile();
            } catch (IOException ioe){
                ioe.getMessage();
            } catch (NoSuchAlgorithmException nsae) {
                nsae.getMessage();
            } catch (URISyntaxException uri) {
                uri.getMessage();
            } catch (NoSuchPaddingException nspe) {
                nspe.getMessage();
            } catch (BadPaddingException bpe) {
                bpe.getMessage();
            } catch (InvalidKeyException ike) {
                ike.getMessage();
            } catch (IllegalBlockSizeException ibse) {
                ibse.getMessage();
            } catch (SQLException sqle) {
                sqle.getMessage();
            } catch (ClassNotFoundException cnfe) {
                cnfe.getMessage();
            }
            this.refreshList(); 
        } else if(ae.getSource() == decFile) {
            try{
                
                FileDecryption.doDecryptRSAWithAES(keyLoader.getPublicKey(), selectedFileField.getText());
                this.encryptionStatusField.setText("Unencrypted");
                this.encryptionStatusField.setBackground(Color.GREEN);
            } catch (Exception e) {
                e.getMessage();
            }
            this.refreshList();
        } else if(ae.getSource() == enFile) {
            try{
                FileEncryption.doEncryptRSAWithAES(keyLoader.getPrivateKey(), selectedFileField.getText());
                this.encryptionStatusField.setText("Encrypted");
                this.encryptionStatusField.setBackground(Color.RED);
            } catch (Exception e) {
                e.getMessage();
            }
            this.refreshList();
        } else if (ae.getSource() == viewLog) {
            
            //SIGNAL TO PARENT COMPONENT TO SWITCH VIEWS
        	avl.appViewEvent(avl, EntryFrame.LOGPANEL);
            
        } else if (ae.getSource() == exportLog) {
            try{
            EventSQLite.export();
            } catch (URISyntaxException uri) {
                uri.getMessage();
            } catch (IOException ioe) {
                ioe.getMessage();
            } catch (SQLException sqle) {
                sqle.getMessage();
            } catch (ClassNotFoundException cnfe) {
                cnfe.getMessage();
            }
        } else if (ae.getSource() == closeApp) {

        }
    }//end ACTION PERFORMED

    //LIST SELECTION EVENT
    public void valueChanged(ListSelectionEvent e){
        // METHOD ATTRIBUTES
        //     String fileType = "";
        ListSelectionModel source = (ListSelectionModel)e.getSource();
        int index = source.getAnchorSelectionIndex();
        String file = (FileManagement.getList().getElementAt(index)).getPath().toString();
        String[] typeArr = file.split("\\.");
        this.selectedFileField.setText((FileManagement.getList().getElementAt(index)).getAbsolutePath().toString());

           try { 
               //SET FILE STATUS TEXT & COLOR
                if (typeArr[typeArr.length - 1] == "enc") {
                    this.encryptionStatusField.setText("Encrypted");
                this.encryptionStatusField.setBackground(Color.RED);
                } else if (typeArr[typeArr.length - 1] != "enc"){
                    this.encryptionStatusField.setText("Unencrypted");
                    this.encryptionStatusField.setBackground(Color.GREEN);
                } 
        } catch (Exception exc){
            System.out.println("value change exception");
        }
       
    }

    //
    //LISTENERS
    //

    //VIEW LISTENER
    public void setViewListener(AppViewListener avl){
        this.avl = avl;
    }// END VIEW LISTENER

    //
    //CLASS METHODS
    //

   //ADDFILE METHOD
   protected String addFile() throws IOException,NoSuchAlgorithmException,BadPaddingException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,URISyntaxException{
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    String path = "";
    jfc.setDialogTitle("Choose File");
    int returnValue = jfc.showOpenDialog(null);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        if (jfc.getSelectedFile().isFile()) {
            FileManagement.addFiles(jfc.getSelectedFile());
            path = new String(Files.readAllBytes(Paths.get(jfc.getSelectedFile().getAbsolutePath())));
        }
    }
    return path;
} //END METHOD

    //REMOVE METHOD
    protected void removeFile() throws IOException,SQLException, ClassNotFoundException, NoSuchAlgorithmException,URISyntaxException,NoSuchPaddingException,InvalidKeyException,IllegalBlockSizeException,BadPaddingException{
        JFileChooser jfc = new JFileChooser(FileManagement.getAbsolute());
        jfc.setDialogTitle("Choose File");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            FileManagement.removeFile(jfc.getSelectedFile());
        }
    }//END METHOD

    //REFRESH FILE LIST
    public void refreshList() {
        //REMOVE,REVALIDATE, REPAINT
        this.remove(scrollView);
        this.revalidate();
        this.repaint();

        //REFRESH LIST
        FileManagement.updateFileList();
        filesList = new JList<File>(FileManagement.getList());
        lsm = filesList.getSelectionModel();
        lsm.setSelectionMode(0);
        lsm.addListSelectionListener(this);
        //REINITIALIZE JScrollPane
        scrollView = new JScrollPane(this.filesList);
        scrollView.setBounds(306, 18, 281, 420);
        scrollView.setViewportView(this.filesList);

        //READD,REVALIDATE,REPAINT
        add(scrollView);
        this.revalidate();
        this.repaint();
    }
}//END CLASS