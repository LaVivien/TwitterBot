import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

//Implement iterator to read file line by line
public class FileLineIterator implements Iterator<String> {
	FileReader fr;
	BufferedReader br ;
	String strLine = "";

	public FileLineIterator(String filePath) {
		try {     
			fr = new FileReader(filePath);			
			br = new BufferedReader(fr);
            strLine = br.readLine();
            if( strLine == null && br != null ) {
            	br.close();
            }      
		} catch (FileNotFoundException e) {
			 System.err.println("FileNotFoundException: " + e);
		} catch( IOException ex ) {
        	System.out.println("error");
        }
	}

	@Override
	public boolean hasNext() {			 
		return strLine != null;
	}

	@Override
	public String next() {
		String  returnString = strLine;
		try {
            strLine = br.readLine();
            if( strLine == null && br != null ) {
            	br.close();
            }
            return returnString;
        } catch( Exception ex ) {
        	throw new NoSuchElementException( "Exception caught in FileLineIterator.next() " + ex );
        }
	 }
	
	 public static void main(String[] args) {		
			FileLineIterator li = new FileLineIterator("files/simple_test_data.csv");
			while(li.hasNext()) 		
				System.out.println(li.next());
	 }
}
