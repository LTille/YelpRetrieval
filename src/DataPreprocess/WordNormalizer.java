package DataPreprocess;
import java.util.Arrays;

import Classes.Stemmer;

/**
 * This file is for the assignment of INFSCI 2140 in 2016 Spring
 *
 * This class is used for extract the stem of certain word by calling stemmer
 */
public class WordNormalizer {
	//you can add essential private methods or variables
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] lowercase( char[] chars ) {
		//transform the uppercase characters in the word to lowercase
		String w = String.valueOf(chars);//convert character array to string
		return w.toLowerCase().toCharArray(); //return char array;
	}
	
	public String stem(char[] chars)
	{
		Stemmer s = new Stemmer();
		s.add(chars, chars.length);
		s.stem();
		//use the stemmer in Classes package to do the stemming on input word, and return the stemmed word
		String str="";
		return str+s;
	}
	
}
