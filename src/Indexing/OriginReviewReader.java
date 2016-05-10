package Indexing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Classes.Path;

public class OriginReviewReader {

	private String dir = null;
	
	public OriginReviewReader(){
		dir = Path.originReview;
	}
	
	// Retrieve the docno given the integer docid
	@SuppressWarnings("resource")
	public String getReviewContent( int docid ) {

		String line=null;
		try {
			FileInputStream fis = new FileInputStream(dir);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			while((line=reader.readLine())!=null){
				if(line.contains("[")){ //locate position since mapping between docid and docno will contain []
					String target = line.substring(line.indexOf('[')+1, line.indexOf(']'));//retrieve what between bracket
					if(Integer.parseInt(target)==docid){
						String content=line.substring(line.indexOf(']')+2);//obtain remaining docno
						return content;
					}
				}
			}
			fis.close();
			reader.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
