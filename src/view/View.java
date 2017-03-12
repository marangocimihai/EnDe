package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import encrypt.Combined;
import encrypt.Decrypt;
import encrypt.Encrypt;

public class View 
{
	public static JFrame frame = null;
	private static view.JTreeModel treeModel = null;
	
	public View(){};
	
	public static void mainWindow()
	{
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("EnDe");

		JPanel west = new JPanel();
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		JPanel south = new JPanel();
		JLabel north = new JLabel("Browse - select file / folder to encrypt       Favorite - encrypt / decrypt favorite folder");
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		center.setBorder(BorderFactory.createEtchedBorder());
		west.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel browseButtonPanel = new JPanel();
		JPanel favoriteButtonPanel = new JPanel();
		JPanel northPanel = new JPanel();
		
		northPanel.setLayout(new FlowLayout());
		northPanel.add(north);
		
		JButton browseButton = new JButton("Browse  ");
		JButton favoriteButton = new JButton("Favorite");
		
		browseButton.setToolTipText("Choose folder to encrypt / decrypt.");
		favoriteButton.setToolTipText("Press this button to quickly encrypt / decrypt the default chosen folder.");

		browseButtonPanel.add(browseButton);
		favoriteButtonPanel.add(favoriteButton);
		
		west.add(browseButton);
		west.add(favoriteButton);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setBorder(BorderFactory.createTitledBorder(""));
		
		
		browseButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				frame.remove(center);
				
				if (treeModel != null)
				{
					frame.remove(treeModel);
				}
				else
				{
					frame.remove(center);
				}
				
				//frame.remove(center);
				frame.add(fileChooser, BorderLayout.CENTER);
				frame.revalidate();	
				frame.repaint();
			}
		});
		
		favoriteButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				((AbstractButton)e.getSource()).setEnabled(false);
				
				StringBuilder favorite = new StringBuilder("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\Conturi Personale\\");
				Path favoritePath = Paths.get(favorite.toString());
				
				if (Encrypt.isEncrypted(favoritePath))
				{
					Decrypt.decryptAndRename(favoritePath);
					
					if (JOptionPane.showConfirmDialog(null, 
	 						  						  "The favorite folder has been successfully decrypted. Would you like to open it ?", 
	 						  						  "Open",
	 						  						  JOptionPane.YES_NO_OPTION) 
							  						  == 
							  						  JOptionPane.YES_OPTION)
					{
						try 
						{
							Runtime.getRuntime().exec("explorer.exe /select," + favorite);
						} 
						catch (IOException ex) 
						{
							ex.printStackTrace();
						}
					}	
				}
				else
				{
					Encrypt.encryptAndRename(favoritePath);
					
					if (JOptionPane.showConfirmDialog(null, 
							 						  "The favorite folder has been successfully encrypted. Would you like to open it ?", 
							 						  "Open",
							 						  JOptionPane.YES_NO_OPTION) 
													  == 
													  JOptionPane.YES_OPTION)
					{
						try 
						{
							Runtime.getRuntime().exec("explorer.exe /select," + favorite);
						} 
						catch (IOException ex) 
						{
							ex.printStackTrace();
						}
					}
				}
				
				((AbstractButton)e.getSource()).setEnabled(true);
			}
		});
		
		
		
		fileChooser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{	
				if (actionEvent.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
			    {
					south.removeAll();
					
					JTreeModel.currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			        treeView();
					
					Path path = Paths.get(JTreeModel.currentFilePath);
					
					JButton encryptButton = new JButton("Encrypt");
					JButton decryptButton = new JButton("Decrypt");
					
					if (Encrypt.isEncrypted(path))
					{
						JPanel decryptButtonPanel = new JPanel();
						decryptButtonPanel.add(decryptButton);
						south.add(decryptButtonPanel);
						
						decryptButton.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent actionEvent)
							{
								Decrypt.decryptAndRename(Paths.get(JTreeModel.currentFilePath));
								
								frame.remove(treeModel);
								frame.remove(south);
								
								frame.revalidate();

								if (JOptionPane.showConfirmDialog(null, 
																 "The file / folder has been successfully decrypted. Would you like to open it ?", 
																 "Open",
																 JOptionPane.YES_NO_OPTION) 
																 == 
																 JOptionPane.YES_OPTION) 
								{
									try 
									{
										Runtime.getRuntime().exec("explorer.exe /select," + JTreeModel.currentFilePath);
									} 
									catch (IOException e) 
									{
										e.printStackTrace();
									}
								}
							}
						});
					}
					else
					{
						JPanel encryptButtonPanel = new JPanel();
						encryptButtonPanel.add(encryptButton);
						south.add(encryptButtonPanel);
						
						encryptButton.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent actionEvent)
							{
								Encrypt.encryptAndRename(Paths.get(JTreeModel.currentFilePath));
								
								frame.remove(treeModel);
								frame.remove(south);		
								frame.revalidate();
								
								if (JOptionPane.showConfirmDialog(null, 
										 						  "The file / folder has been successfully encrypted. Would you like to open it ?", 
										 						  "Open",
										 						  JOptionPane.YES_NO_OPTION) 
										 						  == 
										 						  JOptionPane.YES_OPTION) 
								{
									try 
									{
										Runtime.getRuntime().exec("explorer.exe /select," + JTreeModel.currentFilePath);
									} 
									catch (IOException e) 
									{
										e.printStackTrace();
									}
								}
							}
						});
					}
		
			        frame.remove(fileChooser);
			        frame.add(south, BorderLayout.SOUTH);
			        frame.revalidate();
			    }
				else if (actionEvent.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
			    {
			        frame.remove(fileChooser);
			        frame.revalidate();
			        frame.repaint();
			    }
				
				frame.add(center, BorderLayout.CENTER);
				frame.revalidate();
			}
		});
	
		frame.add(center, BorderLayout.CENTER);
		frame.add(east, BorderLayout.EAST);
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(west, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	
	public static void treeView()
	{
		 treeModel = new view.JTreeModel();

		 JTreeModel.expandAll();
		 
		 frame.getContentPane().add(treeModel, BorderLayout.CENTER);
		 frame.revalidate();
	}  
	
	public static void startApp()
	{	
	    JLabel label = new JLabel("Password: ");
		JPasswordField password = new JPasswordField(24);
	    Box box = Box.createVerticalBox();
	    
	    box.add(label);
	    box.add(password);
	    
		try 
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} 
		catch (ClassNotFoundException | 
			   InstantiationException | 
			   IllegalAccessException | 
			   UnsupportedLookAndFeelException e1) 
		{
			e1.printStackTrace();
		}
	        
	    while (true)
	    {
		    int option = JOptionPane.showConfirmDialog(null, box, "Password", JOptionPane.OK_CANCEL_OPTION);
		    
		    if (option == JOptionPane.OK_OPTION)
		    {
		    	if (password != null)
				{
					if (new String(password.getPassword()).equals(Combined.password))
					{
						mainWindow();
						break;
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
		    }
		    else
		    {
		    	break;
		    }
	    }
	}
}
