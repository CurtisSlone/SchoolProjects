/*
*Author: Curtis Slone
*File: PasswordCapture.java
*Date: 31 Jul 2021
* Password Capture for 2FA
*/
package com.piip.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.piip.interfaces.PasswordListener;

public class PasswordCapture extends JFrame implements ActionListener {
    private char[] password;  
    private JPasswordField passwd;
    private PasswordListener pl;

	public PasswordCapture() {
		super("PII Protection");
        setPreferredSize(new Dimension(600,500));
		JPanel panel = new JPanel();

        //
        // INIT FRAME
        //
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //INIT JFRAME
        this.setResizable(false);// Don't let user resize window.

        //SCREEN INITIALIZATION
        this.setLocation( // Center window on screen.
                (screen.width - this.getWidth()) / 2,
                (screen.height - this.getHeight()) / 2);

		
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lbl1 = new JLabel("Please enter a password to support 2-Factor Authentication");
		lbl1.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lbl1.setBounds(0, 69, 600, 44);
		panel.add(lbl1);
		
		passwd = new JPasswordField();
		passwd.setBounds(161, 125, 250, 16);
		panel.add(passwd);


		JButton button = new JButton("Submit");
		button.setBounds(161, 175, 112, 16);
        button.addActionListener(this);
		panel.add(button);
	}

    //PASSWORD LISTENER
    public void setPasswordListener(PasswordListener pl){
        this.pl = pl;
    }
    //ACTION PERFORMED
    public void actionPerformed(ActionEvent ae){
        password = passwd.getPassword();
        String tmp = password.toString();
        pl.passwordEvent(pl,tmp);
        this.setVisible(false);
        this.dispose();

    }

    //RETURN PASSWORD
    public char[] getPasswd(){
        return this.password;
    }
}
