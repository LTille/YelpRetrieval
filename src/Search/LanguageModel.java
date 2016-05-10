package Search;

import Classes.Review;
import Indexing.MyIndexReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * A language retrieval model for ranking documents
 * -- INFSCI 2140: Information Storage and Retrieval Spring 2016
 */
public class LanguageModel {
	
	protected MyIndexReader indexReader;
	private int cREF = 0; //total number of terms in the whole collection
	
	public LanguageModel(MyIndexReader ixreader) {
		indexReader = ixreader;
	
	}
	/**
	 * Search for the topic information. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * TopN specifies the maximum number of results to be returned.
	 * 
	 * @param query The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @return
	 */
	
	public List<Review> retrieveQuery(String query, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the docs based on their relevance score, from high to low
	   
		List<Review> result = new ArrayList<>();//store result
		List<String> terms = new ArrayList<>();//store the term exist in the index
		HashMap<Integer,DocDetail> docList = new HashMap<>();//key is integer docId, DocDetail is the nested class
		HashMap<String, Double> tRefProb = new HashMap<>(); ////p(w|REF)
		
		int num=0;
		try{
			while(indexReader.docLength(num)!=0){
				cREF+=indexReader.docLength(num);
				num++;
			}
			
		}catch(Exception e){
			
		} 
		
	    String[] titleTerm = query.split(" ");//tokenize the query
		
		//remove terms that not appear in the whole collection
		for(String t: titleTerm){ 
			//if the term does not appear in the index, leave that term alone
			if(indexReader.DocFreq(t)==0)
				continue;
			terms.add(t);
		}

		//calculate the term probabiliy for each query in the documents that contain the searched term
		for(int i=0;i<terms.size();i++){
			String term = terms.get(i);
			int[][] posting = indexReader.getPostingList(term); //get the posting list for search term
			int length = indexReader.DocFreq(terms.get(i));//number of documents contain that term
			//iterate through the whole posting list to get the docid and term frequency
			for(int j=0;j<length;j++){
				int docid = posting[j][0];//get document id
				int tf = posting[j][1];//get term frequency in that document
				//if it is a newly appeared document, add the document to the docList
				if(!docList.containsKey(docid)){
					HashMap<String,Integer> map = new HashMap<>();
					map.put(term, tf);
					//docdetail to store the term, term frequency and document size for each document
					DocDetail dd = new DocDetail(map,indexReader.docLength(docid));
					docList.put(docid, dd);
				}
				//if document is already in the list, add the corresponding term frequency
				else{
					docList.get(docid).tdFreq.put(term, tf);
				}
				
			}
			//store the individual term probability in the whole collection for later smoothing
			double pwREF = (double)indexReader.CollectionFreq(term)/cREF; //p(w|REF)
			tRefProb.put(term, pwREF); //store term and p(w|REF);
		}
		
		//interate through the whole document list
		//smoothing  p(w|D) = c(w,D)+up(w|C)/|D|+u
		for(Integer id:docList.keySet()){
			
			double score = 1;
			int miu = 2000;
			int docLength = docList.get(id).docSize;// |D| document length
			for(int j=0;j<terms.size();j++){
				String term = terms.get(j);
				double pwREF = tRefProb.get(term); //p(w|REF)
				//if term not appears in one document, set it as 0
				int cwD=0;
				//if term in the document, get its frequency
				if(docList.get(id).tdFreq.containsKey(term))
					cwD = docList.get(id).tdFreq.get(term);//c(w,D)
				//query term likelihood from language statistical model of each doc
				double pwD = (double)(cwD + miu*pwREF)/(docLength+miu);
				//query likelihood by multiplying the probability for each term
				score*=pwD;
			}
			String docNo = indexReader.getDocno(id);
			int sign = docNo.indexOf('$');
			double star = Double.parseDouble(docNo.substring(sign+1));
			String docNumber = docNo.substring(0, sign);
			Review review = new Review(String.valueOf(id),docNumber,score,star);
			//docid, String docno, double score
			result.add(review);
		}
		
		//sort the search result
		Collections.sort(result, new retrieveTop());
		
		//in case the required one larger than the existing results, get the smaller one
		int resultNum = docList.size()>TopN?TopN:docList.size();
		//retrieve the top3
		return result.subList(0, resultNum);
	}
    
	/*This is the method to sort the score of each document*/
	class retrieveTop implements Comparator<Review>{

		@Override
		public int compare(Review o1, Review o2) {
			 if(o1.score() > o2.score()) {
                 return -1;
             }
             else if(o1.score() < o2.score()) {
                 return 1;
             }
			return 0;
		}
		
	}
	
	

}