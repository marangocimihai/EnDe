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

public class Decrypt 
{
	Decrypt(){};
	
	public static String decrypt(String key, String initVector, String encrypted) 
	{
        try 
        {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
            
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        return null;
    }
	
	public static void decryptAndRename(Path path)
	{
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) 
	    {
	        for (Path entry : stream) 
	        {
	            if (Files.isDirectory(entry)) 
	            {
	                decryptAndRename(entry);
	            }
	            else
	            {
	            	String currentFilePath = entry.toAbsolutePath().toString();
	            	
	            	List<String> currentFileContent = encrypt.Files.readFromFile(currentFilePath);
	            	List<String> currentFileContentDecrypted = new ArrayList<String>();
	            	
	            	Iterator<String> cfc = currentFileContent.iterator();
	            	
	            	while (cfc.hasNext())
	            	{
	            		String decrypted = decrypt(Combined.key, Combined.iv, cfc.next().toString());
	            		currentFileContentDecrypted.add(decrypted);
	            		
	            		encrypt.Files.writeToFile(currentFileContentDecrypted, currentFilePath);    
	            	}     
	            	
	            	encrypt.Files.removeEncryptedTag(currentFilePath);
	            }       
	        }
	    } 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
}
