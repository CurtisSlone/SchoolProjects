//----------------------------------------------------------------------
// BinaryTree.java         by Curtis Slone                
//
// Creates Binary tree and related methods for categorization and display
// 
//----------------------------------------------------------------------
package program.app;

import java.util.Arrays;

public class BinaryTree<T>
{

    public TreeNode<T> root;
    char[] queue = new char[256];
    int queueIndex = 0;
    public int numNodes;
    public String proper;
    //
    // Constructors
    //
    public BinaryTree()
    {
        root = null;
    }  

    //
    // Static Nested Class
    //
    public static class TreeNode<T>
    {
        protected TreeNode<T> parent;
        protected TreeNode<T> left;
        protected TreeNode<T> right;
        protected T value;

        public TreeNode()
        {
            this.value = value;
            this.parent = null;
            this.left = null;
            this.right = null;

        }

        public TreeNode(T value)
        {
            this.value = value;
            this.parent = null;
            this.left = null;
            this.right = null;

        }

        public void setValue(T value)
        {
            this.value = value;
        }

        public T getValue()
        {
            return this.value;
        }

        public void setRight(TreeNode<T> link)
        {
            this.right = link;
        }

        public TreeNode<T> getRight()
        {
            return this.right;
        }

        public void setLeft(TreeNode<T> link)
        {
            this.left = link;
        }

        public TreeNode<T> getLeft()
        {
            return this.left;
        }

        public void setParent(TreeNode<T> link)
        {
            this.parent = link;
        }

        public TreeNode<T> getParent()
        {
            return this.parent;
        }
    }

    //
    // Class Methods
    //

    public String isBalanced(TreeNode<T> rooted)
    {
        int leftCount = 0, rightCount = 0;
        if(rooted.getLeft() != null)
            leftCount = getHeight(rooted.getLeft());
        if(rooted.getRight() != null)
            rightCount = getHeight(rooted.getRight());
        return (Math.abs(leftCount - rightCount) > 1)? "true" : "false";
        
    }

    public String isFull()
    {
        return getMaxNumNodes(getHeight(this.root)) == getNumNodes() ? "true" : "false";
    }

    public void isProper(TreeNode<T> rooted)
    {
        String proper = " placeholder";
        if (rooted.getLeft() != null && rooted.getRight() != null)
        {
            isProper(rooted.getLeft());
            isProper(rooted.getRight());
        }
        else
        if (rooted.getLeft() == null && rooted.getRight() == null)
        {
            this.proper = "true";
        }
        else
        if ( (rooted.getLeft() != null && rooted.getRight() == null) || (rooted.getLeft() == null && rooted.getRight() != null))
        {
            this.proper = "false";
        }
    }

    public int getHeight(TreeNode<T> rooted)
    {
        int leftCount = 0, rightCount = 0;
        if(rooted.getLeft() != null)
            leftCount = getHeight(rooted.getLeft());
        if(rooted.getRight() != null)
            rightCount = getHeight(rooted.getRight());
        return 1 + Math.max(leftCount,rightCount);
    }

    public int getNumNodes()
    {   
        return numNodes;
    }

    public int getMaxNumNodes(int n)
    {
        return n <= 1 ? 1 : getMaxNumNodes(n-1) + (int)(Math.pow(2,n));
    }

    public void inOrder(TreeNode<T> node) 
  {
    if (node != null)
    {
        queue[queueIndex] = '(';
        queueIndex++;
        inOrder(node.getLeft());
        queue[queueIndex] = ((Character)node.getValue());
        queueIndex++;
        inOrder(node.getRight());
        queue[queueIndex] = ')';
        queueIndex++;
    }
  }

  public String returnInOrder()
  {

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < queue.length; i++) 
    {
        stringBuilder.append(queue[i]);
    }
      return stringBuilder.toString();
  }

  public void clearQueue()
  {
      this.queue = new char[50];
  }

  public void clearNumNodes()
  {
      this.numNodes = 0;
  }
    public boolean isEmpty()
    {
        return (this.root == null);
    }

    public String getProper()
    {
        return this.proper;
    }

}