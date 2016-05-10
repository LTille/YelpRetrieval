package DataPreprocess;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.tribes.util.Arrays;

import Classes.Path;

public class BusinessScore {

	Map<String,String> scoreMap = new HashMap<>();
	public BusinessScore() throws IOException {
		
		FileInputStream fis = new FileInputStream(Path.businessFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		line=reader.readLine();
		while((line=reader.readLine())!=null){
			//System.out.println(line);
			String[] score = line.split("=");
			scoreMap.put(score[0], score[1]);
		}
		fis.close();
		reader.close();
	}
	
	public String getScore(String name) {
		//System.out.println("-----"+scoreMap);
        //String bname = "\""+name+"\"";
		return scoreMap.get(name);
		
	}
}
