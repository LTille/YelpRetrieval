package DataPreprocess;

/**
 * This file is for the assignment of INFSCI 2140 in 2016 Spring
 * 
 * TextTokenizer can split a sequence of text into individual word tokens, the delimiters can be any common punctuation marks(space, period etc.).
 */
public class WordTokenizer {
	//you can add any essential private method or variable
	 private String[] words=null;//Initiate a string array to store every word
	 int loc=0;//to note down the location of reading 

	// YOU MUST IMPLEMENT THIS METHOD
	public WordTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is the char array for a whole document)
		
		String s=String.valueOf(texts);//convert character array to string
		words = s.split("\\W+");//split string through various character
		// \W means all the demoninate except [a-zA-Z0-9]
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] nextToken() {
		// read and return the next word of the document
		if(loc<words.length){

			String res=words[loc];
			loc++;
			return res.toCharArray();
		}

		// or return null if it reaches the end of the document
		return null;
	}
	
}
