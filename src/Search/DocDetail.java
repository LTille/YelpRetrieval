package Search;

import java.util.HashMap;


/*This is a nested class to store document detail */
public class DocDetail{
	HashMap<String,Integer> tdFreq;//(token, c(token,doc))
	int docSize; //document length
	
	//constructor
	public DocDetail(HashMap<String, Integer> tdFreq, int docSize){
		this.tdFreq = tdFreq; //term frequency
		this.docSize = docSize;
		
	}
}

