//----------------------------------------------------------------------
// MainGUI.java         by Curtis Slone                
//
// Graphical interface for binary tree creating program
// 
//----------------------------------------------------------------------

import program.app.*;
import program.exception.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Stack;


public class MainGUI
{
    //JFrame Attributes
    private static JTextField enterTreeText;
    private static JLabel enterTreeLabel;
    private static JLabel outputLabel;
    private static JTextField outputText;
    

    //Error Frame
    private static JFrame err;
    private static String errMessage = null;

    // Event Handler
    // -- Handles all categorization and display events
    private static class ActionHandler implements ActionListener
    {
        BinaryTree<Character> newTree = new BinaryTree<Character>();
       
         public void actionPerformed(ActionEvent event)
        {
            
            if (event.getActionCommand().equals("Make Tree"))
            {
                
                try {
                    Stack<Character> stack = new Stack<Character>();
                    newTree.clearNumNodes();
                    String treePhrase = enterTreeText.getText();
                    BinaryTree.TreeNode<Character> subRoot = new BinaryTree.TreeNode<Character>();
                    BinaryTree.TreeNode<Character> newNode;
                    Character currChar;
                    int currCharIndex = 0;
                    int lastCharIndex = treePhrase.length();
                    while(currCharIndex < lastCharIndex)
                    {
                        newNode = new BinaryTree.TreeNode<Character>();
                        currChar = treePhrase.charAt(currCharIndex);
                        if (currChar == '(')
                        {
                            stack.push(currChar);
                        }
                        else if (currChar == ')')
                        {   
                            if (!stack.isEmpty())
                            {
                            stack.pop();
                            newNode = subRoot.getParent();
                            subRoot = newNode;
                            }
                            else
                                throw new InvalidTreeSyntax();
                        }
                        else
                        {
                            newTree.numNodes++;
                            newNode.setValue(currChar);
                            if (newTree.root == null)
                            {
                                newTree.root = newNode;
                                subRoot = newTree.root;
                            }
                            else
                            if (subRoot.getLeft() == null)
                                subRoot.setLeft(newNode);
                            else
                                subRoot.setRight(newNode);
                            newNode.setParent(subRoot);
                            subRoot = newNode;
                        }
                        currCharIndex++;
                    }
                    if(!stack.isEmpty())
                        throw new InvalidTreeSyntax();
                } catch(InvalidTreeSyntax its)
                {
                    errMessage = its.getMessage();
                    JOptionPane.showMessageDialog(err,errMessage);
                }
            } 
            else
            if (event.getActionCommand().equals("is Balanced?"))
            {
                outputText.setText(newTree.isBalanced(newTree.root));
            }
            else
            if (event.getActionCommand().equals("is Full?"))
            {
                outputText.setText(newTree.isFull());
            }
            else
            if (event.getActionCommand().equals("is Proper?"))
            {
                newTree.isProper(newTree.root);
                outputText.setText(newTree.getProper());
            }
            else
            if (event.getActionCommand().equals("Height"))
            {
                outputText.setText(Integer.toString(newTree.getHeight(newTree.root)));
            }
            else
            if (event.getActionCommand().equals("Nodes"))
            {
                outputText.setText(Integer.toString(newTree.getNumNodes()));
            }
            else
            if (event.getActionCommand().equals("Inorder"))
            {
                newTree.clearQueue();
                newTree.inOrder(newTree.root);
                outputText.setText(newTree.returnInOrder());
                newTree.clearQueue();

            }
        }
    }

    public static void main(String args[]) throws InvalidTreeSyntax
    {
        
        //Set JFRame
        JFrame displayFrame = new JFrame();
        displayFrame.setTitle("Binary Tree Categorizer");
        displayFrame.setPreferredSize(new Dimension(750,200));
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set Enter Tree box and Output Box
        enterTreeText = new JTextField("");
        enterTreeLabel = new JLabel("Enter Tree: ",JLabel.LEFT);
        outputLabel = new JLabel("Output: ",JLabel.LEFT);
        outputText = new JTextField("");
        outputText.setEditable(false);

        //Buttons
        JButton makeTree = new JButton("Make Tree");
        JButton isBalanced = new JButton("is Balanced?");
        JButton isFull = new JButton("is Full?");
        JButton isProper = new JButton("is Proper?");
        JButton height = new JButton("Height");
        JButton nodes = new JButton("Nodes");
        JButton inorder = new JButton("Inorder");

        //Action Handler & Actions
        ActionHandler action = new ActionHandler();
        makeTree.addActionListener(action);
        isBalanced.addActionListener(action);
        isFull.addActionListener(action);
        isProper.addActionListener(action);
        height.addActionListener(action);
        nodes.addActionListener(action);
        inorder.addActionListener(action);

        //Frame Container
        Container contentPane = displayFrame.getContentPane();
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel outputPanel = new JPanel();

        //Layouts
        //expression Panel
        inputPanel.setLayout(new GridLayout(1,2));
        inputPanel.add(enterTreeLabel);
        inputPanel.add(enterTreeText);
        inputPanel.setBorder(new EmptyBorder(10,10,10,10));

        //button panel
        buttonPanel.setLayout(new GridLayout(1,7));
        buttonPanel.add(makeTree);
        buttonPanel.add(isBalanced);
        buttonPanel.add(isFull);
        buttonPanel.add(isProper);
        buttonPanel.add(height);
        buttonPanel.add(nodes);
        buttonPanel.add(inorder);
        buttonPanel.setBorder(new EmptyBorder(20,10,20,10));

        //result panel
        outputPanel.setLayout(new GridLayout(1,2));
        outputPanel.add(outputLabel);
        outputPanel.add(outputText);
        outputPanel.setBorder(new EmptyBorder(10,10,10,10));

        //add panels to pane
        contentPane.add(inputPanel,"North");
        contentPane.add(buttonPanel,"Center");
        contentPane.add(outputPanel,"South");

        //display GUI
        displayFrame.pack();
        displayFrame.setVisible(true);
    }
}