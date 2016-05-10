package Indexing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import Classes.Path;

/**
 * Please construct your code efficiently, otherwise, your memory cannot hold
 * the whole corpus...
 *
 * Class for Assignment 2 of INFSCI2140, 2016 Spring.
 */
public class OriginIndex {
	private BufferedWriter bw = null;	
	private List<String> docIndex = new ArrayList<>();// a list to store the map
	// between docid and
														// docno
	private int docid = 0;// initialize docid to 0

	/**
	 * This constructor should initiate the FileWriter to output your index
	 * files remember to close files if you finish writing the index
	 */
	public OriginIndex() throws IOException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Path.originReview)));
	}

	public void index(String docno,String content) throws IOException {
        
            docIndex.add(docid++, content);// store the mapping between dociId and
										// docno
	}

	/**
	 * close the index writer, and you should output all the buffered content
	 * (if any). and if you write your index into several files, you need to
	 * fuse them here.
	 */
	public void close() throws IOException {

		// write all the docid and docno mapping
		for (int i = 0; i < docIndex.size(); i++) {
			bw.append("[").append(String.valueOf(i)).append("]").append(" ").append(docIndex.get(i)).append("\n");
		}
		bw.close(); // close the writer
	}

}
