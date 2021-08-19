/*
*Author: Mueed Chaudhry
*File: GeneratingRSAKeys.java
*Date: 31 Jul 2021
* Thank you Initialization
*/
package com.piip.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ThankyouInitialization extends JFrame{
	public ThankyouInitialization() {
		
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


		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Thank you for your initialization!");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblNewLabel.setBounds(97, 69, 415, 44);
		panel.add(lblNewLabel);
		
		JLabel lblPleaseWait = new JLabel("Application closing. Please start again.");
		lblPleaseWait.setBounds(136, 125, 292, 16);
		panel.add(lblPleaseWait);
	}
}
