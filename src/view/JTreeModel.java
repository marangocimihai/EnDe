package view;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;

public class JTreeModel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	public static String currentFilePath;
	static JTree tree;
	private DefaultMutableTreeNode root;
	
	public JTreeModel() 
	{
		root = new DefaultMutableTreeNode("root", true);
	    getList(root, new File(currentFilePath));
	    setLayout(new BorderLayout());
	    tree = new JTree(root);
	    tree.setRootVisible(false);
	    add(new JScrollPane((JTree)tree), BorderLayout.NORTH);
    }
	
	public static void expandAll()
    {
        int r = 0;
        
        while (r < tree.getRowCount())
        {
            tree.expandRow(r);
            r++;
        }
    }

	public void getList(DefaultMutableTreeNode node, File f) 
	{
		if(!f.isDirectory()) 
		{
			System.out.println("FILE  -  " + f.getName());
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
			node.add(child);	
		}
		else 
		{
			System.out.println("DIRECTORY  -  " + f.getName());
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
			node.add(child);
			File fList[] = f.listFiles();
			
			for(int i = 0; i  < fList.length; i++)
			{
				getList(child, fList[i]);
			}
		}
	}
}

