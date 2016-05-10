package DataPreprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import Classes.Path;

public class OriginReview {

	  //Intialize FileInputStream and BufferedReader
	  private FileInputStream fis = null;
	  private BufferedReader reader = null;
	
	  public OriginReview() throws IOException {
			// This constructor should open the file in Path.DataWebDir
			// and also should make preparation for function nextDocument()
			// Do not load the whole corpus into memory!!!
			
			//read docset.trecweb
			 fis = new FileInputStream(Path.txtReview1);
			 reader = new BufferedReader(new InputStreamReader(fis));
		}
	

	  public Map<String, String> nextReview() throws IOException {
		String line = "";
		Map<String,String> map = null;
		StringBuilder sb = null;
		while((line=reader.readLine())!=null){
		   map = new HashMap<>();
		   int loc = line.indexOf("$");
		   String key = line.substring(0, loc);	
		   String value = line.substring(loc+1);
		   map.put(key, value);
		   return map;
		}
		 return null;
	  }

}
