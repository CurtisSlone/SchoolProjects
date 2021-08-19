/*
*Author: Mueed Chaudhry
*File: ValidatingOS.java
*Date: 31 Jul 2021
* Grab OS gui
*/
package com.piip.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;


public class ValidatingOS extends JFrame{
	public ValidatingOS() {
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Validating Current Operating System");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblNewLabel.setBounds(97, 69, 415, 44);
		panel.add(lblNewLabel);
		
		JLabel lblPleaseWait = new JLabel("Please wait...");
		lblPleaseWait.setBounds(243, 125, 292, 16);
		panel.add(lblPleaseWait);
	}
}
