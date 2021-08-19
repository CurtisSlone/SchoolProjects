/*
*Author: Curtis Slone
*File: EntryFrame.java
*Date: 12 Jul 2021
* Generates Log View in separate window, called by MainUseFrame
*/
package com.piip.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.piip.interfaces.AppViewListener;

public class EntryFrame extends JFrame implements AppViewListener {

    private JPanel cards;
    final protected static String MAINPANEL = "MAIN PANEL";
    final protected static String LOGPANEL = "LOG PANEL";
    final protected static String INITPANEL = "INIT PANEL";
    private static String currentView = INITPANEL;
    

    //Constructor
    public EntryFrame(){
        super("PII Protection");
        setPreferredSize(new Dimension(600,500));
         

        //CREATE VIEW STACK
        cards = new JPanel(new CardLayout());
        MainUsePanel mainView = new MainUsePanel();
        EventLog logView = new EventLog();

        //ADD CARDS TO STACK
        cards.add(mainView, MAINPANEL);
        cards.add(logView, LOGPANEL); 
        

        //ADD CARDS TO MAIN
        add(cards);

        //ADD VIEW EVENT LISTENER
        mainView.setViewListener(this);
        
        //
        // INIT FRAME
        //
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //INIT JFRAME
        this.setContentPane(cards); // Show the panel in the window.
        this.pack(); // Set window size based on the preferred sizes of its contents.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);// Don't let user resize window.

        //SCREEN INITIALIZATION
        this.setLocation( // Center window on screen.
                (screen.width - this.getWidth()) / 2,
                (screen.height - this.getHeight()) / 2);
        this.setVisible(true); // Open the window, making it visible on the screen.
    }// END CONSTRUCTOR

    //SWAP VIEW METHOD
    public void appViewEvent (AppViewListener avl, String card){
       CardLayout cl = (CardLayout)(cards.getLayout());
       cl.show(cards, card);
    }//END SWAP VIEW METHOD

    //GET CARDS 
    public JPanel returnCards(){
        return this.cards;
    } //END GET CARDS
}