import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that is used for reading data from a file and converting it so that 
 * the program is able to work with the data.
 * @author Matthew Kemp
 *
 */
public class ReadData {
	/**
	 * A method that reads data from a file at a specified path in the local directory.
	 * Returns the data in a array of Strings.
	 * @param path path of the data file in the local directory 
	 * @return an array of strings representing each data point in each index
	 */
	public static String[] readData(String path) {
	    Scanner scnr;
	    ArrayList<String> data = new ArrayList<String>();
		try {
			scnr = new Scanner(new File(System.getProperty("user.dir")+"\\"+path));
		} catch (FileNotFoundException e) {
			System.out.println("Data file does not exist");
			e.printStackTrace();
			return null;
		}
		while(scnr.hasNext()) {
			data.add(scnr.next());
		}
		scnr.close();
		return data.toArray(new String[data.size()]);
	}
	/**
	 * A meethod that reads data from a file at a specified path in the local directory.
	 * Returns the data formatted into a prefix tree (Trie).
	 * @param path path of the data file in the local directory
	 * @return A prefix tree (Trie) representing all of the data in the file
	 */
	public static Trie readTrie(String path) {
		Scanner scnr;
	    Trie trie = new Trie();
		try {
			scnr = new Scanner(new File(System.getProperty("user.dir")+"\\"+path));
		} catch (FileNotFoundException e) {
			System.out.println("Data file does not exist");
			e.printStackTrace();
			return null;
		}
		while(scnr.hasNext()) {
			trie.add(scnr.next());
		}
		scnr.close();
		return trie;
	}
}
