package encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Files 
{
	public static final String encryptTag = "[E]";
	
	Files(){};
	
	public static List<String> readFromFile(String filePath)
	{
		List<String> currentFileContent = new ArrayList<String>();
		BufferedReader br = null;
		FileReader fr = null;

		try 
		{

			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String currentLine;

			br = new BufferedReader(new FileReader(filePath));

			while ((currentLine = br.readLine()) != null) 
			{
				currentFileContent.add(currentLine);
			}

		} 
		catch (IOException e) 
		{

			e.printStackTrace();

		} 
		finally 
		{
			try 
			{
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();

			}
		}
		
		return currentFileContent;
	}
	
	public static void writeToFile(List<String> currentFileContentEncrypted, String currentFilePath)
	{
		PrintWriter out = null;
		
		try 
		{
			out = new PrintWriter(currentFilePath);
			Iterator<String> cfc = currentFileContentEncrypted.iterator();
			
			while (cfc.hasNext())
			{
				String current = cfc.next().toString();
				out.println(current);		
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			out.close();
		}
	}
	
	public static void addEncryptedTag(String currentFilePath)
	{
		File oldFileNamePath = new File(currentFilePath);
		
	    String currentDirectoryName = currentFilePath.substring(0, currentFilePath.lastIndexOf("\\"));
	    String currentFileName = currentFilePath.substring(currentFilePath.lastIndexOf("\\") + 1, currentFilePath.length());
	    String newFileName = currentDirectoryName + "\\" + encryptTag + currentFileName;
	     
	    File newFileNamePath = new File(newFileName);

	    if(oldFileNamePath.renameTo(newFileNamePath)) 
	    {
	    	System.out.println("File name changed succesful");
	    } 
	    else 
	    {
	    	System.out.println("Rename failed");
	    } 
	}
	
	public static void removeEncryptedTag(String currentFilePath)
	{
		File oldFileNamePath = new File(currentFilePath);
		
	    String currentDirectoryName = currentFilePath.substring(0, currentFilePath.lastIndexOf("\\"));
	    String currentFileName = currentFilePath.substring(currentFilePath.lastIndexOf("\\") + 4, currentFilePath.length());
	    String newFileName = currentDirectoryName + "\\" + currentFileName;
	    
	    System.out.println("Here file name: " + newFileName);
	    
	    File newFileNamePath = new File(newFileName);

	    if(oldFileNamePath.renameTo(newFileNamePath)) 
	    {
	    	System.out.println("File name changed succesful");
	    } 
	    else 
	    {
	    	System.out.println("Rename failed");
	    }
	}
}
