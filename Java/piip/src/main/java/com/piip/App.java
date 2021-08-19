/*
*Author: Curtis Slone
*File: Main.java
*Date: 31 Jul 2021
* Entry Point For application
*/

package com.piip;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.FileSystems;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

import com.piip.interfaces.PasswordListener;
import com.piip.gui.EntryFrame;
import com.piip.db.SettingsSQLite;
import com.piip.gui.GeneratingRSAKeys;
import com.piip.gui.ThankyouInitialization;
import com.piip.gui.ValidatingOS;
import com.piip.gui.PasswordCapture;
import com.piip.gui.SelectPubKey;
import com.piip.backend.RSAGeneration;
import com.piip.backend.Hasher;




public class App implements PasswordListener {
 
private static String[] settingsValues = new String[5];
private static String fileSeparator = FileSystems.getDefault().getSeparator();
private static Register register;
    //INITIALIZATION CHECK
    private void checkInit() {
        
        //DETERMINE FIRST VIEW
        try {
            if (SettingsSQLite.verifyInit() == true){
                EntryFrame entryFrame = new EntryFrame();
            } else {
            	register = new Register();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //END CHECKINIT

    //INITIALIZE
    static void doInit() throws FileNotFoundException, IOException, NoSuchAlgorithmException{
    	register.window.setVisible(false); //you can't see me!
    	register.window.dispose();
        System.out.println("Doing init now");
        //Generate RSA KEYS
        GeneratingRSAKeys keyFrame = new GeneratingRSAKeys();
        keyFrame.pack();
        keyFrame.setVisible(true);
        RSAGeneration rsag = new RSAGeneration();
        settingsValues[0] = rsag.getKeyLocation();
        keyFrame.setVisible(false);
        keyFrame.dispose();

        // GRAB PASSWORD
        settingsValues[1] = register.getValidPassword_();
        
        //GRAB OS
        ValidatingOS grabOs = new ValidatingOS();
        grabOs.pack();
        grabOs.setVisible(true);
        String os = System.getProperty("os.name");
        settingsValues[2] = os ;
        grabOs.setVisible(false);
        grabOs.dispose();

        // GRAB PUBLIC KEY FOR HASHING
        SelectPubKey selectKey = new SelectPubKey();
        selectKey.pack();
        selectKey.setVisible(true);


        //MAKE HASH OF PRIV KEY
        String[] privPathName = {"keys","priv.pem"};
        String privKeyPath = String.join(fileSeparator,privPathName);
        File privKeyFile = new File(privKeyPath);
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(privKeyFile.getAbsolutePath()))); 
        String privKeyHash = Hasher.makeHash(privateKeyContent);
        settingsValues[3] = privKeyHash;

        //JFILECHOOSER - RETURN PUB FILE CONTENTS FOR HASHING
        // //JFILECHOOSER INIT
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());
        jfc.setDialogTitle("Choose your public key");

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isFile()) {

                //Public Key Info
                String publicKeyContent = new String(Files.readAllBytes(Paths.get(jfc.getSelectedFile().getAbsolutePath())));
                String pubKeyHash = Hasher.makeHash(publicKeyContent);
                settingsValues[4] = pubKeyHash;  
            }
        }
        selectKey.setVisible(false);
        selectKey.dispose();
        
        //FINISH INITIALIZATION
        ThankyouInitialization initFinal = new ThankyouInitialization();
        selectKey.pack();
        selectKey.setVisible(true);
        try{
            SettingsSQLite.updateSettings(settingsValues[2], settingsValues[0], settingsValues[1], settingsValues[4], settingsValues[3]);
        } catch (SQLException sqle){
            sqle.getStackTrace();
        } catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        selectKey.setVisible(false);
        selectKey.dispose();
        EntryFrame entryFrame = new EntryFrame();

    } //END INITIALIZE

    //PASSWORD LISTENER EVENT
    public void passwordEvent(PasswordListener pl, String pass){
        settingsValues[1] = pass;
    } // END EVENT

    //MAIN METHOD
    public static void main(String[] args){
    	App main = new App();
        main.checkInit();
    }

    private class Register extends JFrame {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JFrame window;
    	String[] availableGenders = { "Male", "Female" };
    	JLabel lblId = new JLabel("ID");
    	JLabel lblPassword = new JLabel("PASSWORD");
    	JLabel lblConfirmPassword = new JLabel("CONFIRM PASSWORD");
    	JTextField idTextField = new JTextField();
    	JPasswordField passwordField = new JPasswordField();
    	JPasswordField confirmPasswordField = new JPasswordField();
    	JButton registerButton = new JButton("REGISTER");
    	JButton resetButton = new JButton("RESET");
    	private boolean validated;
    	private String validId, validPassword_;
    	public boolean isValidPassword(String password,String regex)
        {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
    	public void makeGUI()
    	{
    		window = new JFrame();
    		window.setTitle("Init");
    		window.setBounds(40, 40, 380, 278);
    		window.getContentPane().setBackground(Color.pink);
    		window.getContentPane().setLayout(null);
    		window.setVisible(true);
    		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		window.setResizable(false);
    		lblId.setBounds(18, 0, 40, 70);
    		lblPassword.setBounds(18, 48, 100, 70);
    		lblConfirmPassword.setBounds(20, 107, 140, 70);
    		idTextField.setBounds(180, 23, 165, 23);
    		passwordField.setBounds(180, 71, 165, 23);
    		confirmPasswordField.setBounds(180, 130, 165, 23);
    		
    		registerButton.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				String password = "";
    				String confPassword = "";
    				password = passwordField.getText().toString();
    				confPassword = confirmPasswordField.getText().toString();
    				String id = idTextField.getText().toString();
    				if(id == "" || !id.matches("-?\\d+(\\.\\d+)?")) {
    					JOptionPane.showMessageDialog(null, "Invalid ID. Your ID must be a numeric value");
    					idTextField.setText("");
        				passwordField.setText("");
        				confirmPasswordField.setText("");
    					return;
    				}
    				if(!password.equals(confPassword))
    				{
    					JOptionPane.showMessageDialog(null, "Passwords do not match");
    					idTextField.setText("");
        				passwordField.setText("");
        				confirmPasswordField.setText("");
    				}
    				else
    				{
    					
    					String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    			        boolean validPassword = isValidPassword(password,regex);
    			        if(validPassword) {
    			        	JOptionPane.showMessageDialog(null, "Registration successful");
    			        	validated = true;
    			        	validId = id;
    			        	validPassword_ = password;
    			        	try {
								doInit();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (NoSuchAlgorithmException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    			        }
    			        else {
    			        	JOptionPane.showMessageDialog(null, "Registration Unsuccessful"
    			        			+ "\n\n Password must have the following:\n"
    			        			+ "1. Must have at least one numeric character\n" + 
    			        			"2. Must have at least one lowercase character\n" + 
    			        			"3. Must have at least one uppercase character\n" + 
    			        			"4. Must have at least one special symbol among @#$%\n" + 
    			        			"5. Password length should be between 8 and 20");
    			        	idTextField.setText("");
            				passwordField.setText("");
            				confirmPasswordField.setText("");
    			        }
    				}
    			}
    		});
    		registerButton.setBounds(146, 189, 100, 35);
    		resetButton.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				idTextField.setText("");
    				passwordField.setText("");
    				confirmPasswordField.setText("");
    			}
    		});
    		resetButton.setBounds(245, 189, 100, 35);
    		window.getContentPane().add(lblId);
    		window.getContentPane().add(lblPassword);
    		window.getContentPane().add(lblConfirmPassword);
    		window.getContentPane().add(idTextField);
    		window.getContentPane().add(passwordField);
    		window.getContentPane().add(confirmPasswordField);
    		window.getContentPane().add(registerButton);
    		window.getContentPane().add(resetButton);
    	}
    	public String getValidId() {
			return validId;
		}
		public String getValidPassword_() {
			return validPassword_;
		}
		public Register() {
    		validated = false;
    		makeGUI();
    	}
    	public boolean getValidated() {
    		return validated;
    	}
    }
    
}
