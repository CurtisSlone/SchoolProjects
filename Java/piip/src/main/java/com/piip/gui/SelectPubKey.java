/*
*Author: Curtis Slone
*File: SelectPubKey.java
*Date: 31 Jul 2021
* Select PubKey for Hashing
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

public class SelectPubKey extends JFrame {
    public SelectPubKey(){
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
		
		JLabel lbl1 = new JLabel("Please choose your public key");
		lbl1.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lbl1.setBounds(0, 69, 600, 44);
		panel.add(lbl1);
    }
}
