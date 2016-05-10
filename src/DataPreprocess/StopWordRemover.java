package DataPreprocess;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import Classes.Path;

/**
 * This file is for the assignment of INFSCI 2140 in 2016 Spring
 *
 * StopWordRemover is a class takes charge of judging whether a given word
 * is a stopword by calling the method <i>isStopword(word)</i>.
 */
public class StopWordRemover {
	//you can add essential private methods or variables
	Set<String> set = new HashSet<>();//Use hash set as the data structure to store all the stop words
	public StopWordRemover() throws IOException {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		// address of stopword.txt should be Path.StopwordDir
		FileInputStream fis = new FileInputStream(Path.StopwordDir);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while((line=reader.readLine())!=null){
			set.add(line);//add stop word in each line in to hashset
		}
		fis.close();
		reader.close();
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public boolean isStopword( char[] word ) {
		// return true if the input word is a stopword, or false if not
		return set.contains(String.valueOf(word));
		
	}
}
