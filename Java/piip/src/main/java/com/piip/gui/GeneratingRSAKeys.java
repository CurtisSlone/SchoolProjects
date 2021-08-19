/*
*Author: Mueed Chaudhry
*File: GeneratingRSAKeys.java
*Date: 31 Jul 2021
* Generating keys JFRAME
*/
package com.piip.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GeneratingRSAKeys extends JFrame{
	public GeneratingRSAKeys() {
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
		
		JLabel lbl1 = new JLabel("Generating RSA keys");
		lbl1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lbl1.setBounds(97, 69, 334, 44);
		panel.add(lbl1);
		
		JLabel lbl2 = new JLabel("Please choose where to save your public key.");
		lbl2.setBounds(161, 125, 112, 16);
		panel.add(lbl2);

		JLabel lbl3 = new JLabel("Hint: Save it to a USB disk");
		lbl3.setBounds(161, 175, 112, 16);
		panel.add(lbl3);
	}
}
