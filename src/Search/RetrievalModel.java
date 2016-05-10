package Search;

import Classes.Review;
import Indexing.MyIndexReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;


/**
 * A language retrieval model for ranking documents
 * -- INFSCI 2140: Information Storage and Retrieval Spring 2016
 */
public class RetrievalModel {
	
	protected MyIndexReader indexReader;
	private int cREF = 324373; //total number of terms in the whole collection
	HashMap<String, BusinessDetail> businessScore;
	
	public RetrievalModel(MyIndexReader ixreader) {
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
	
	public List<Review> retrieveReview(String query, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the docs based on their relevance score, from high to low
	   
		List<Review> result = new ArrayList<>();//store result
		List<String> terms = new ArrayList<>();//store the term exist in the index
		HashMap<Integer,DocDetail> docList = new HashMap<>();//key is integer docId, DocDetail is the nested class
		HashMap<String, Double> tRefProb = new HashMap<>(); ////p(w|REF)
//		ArrayList<String> reviewidList = new ArrayList<String>();
		
		
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

	public String[] retrieveBusiness(List<Review> rreviews, String originquery, int i) {
		businessScore = new HashMap<>();
		//HashSet<String> businessName = new HashSet<String>();
		String [] queryword = originquery.split(" ");
		for(Review rreview : rreviews){
//			int sign = rreview.docno().indexOf('$');
//			System.out.println(rreview.docno());
//			String bname = rreview.docno().substring(0, sign);
			String bname = rreview.docno();
			double sumRscore=rreview.score(); 
			double sumRStar = rreview.star(); //sum of stars of related reviews
			if(!businessScore.containsKey(bname)){				
				int countR = 1; //number of related reviews
				ArrayList<String> reviewidList = new ArrayList<String>(); //list of docid of related reviews
				reviewidList.add(rreview.docid());
				BusinessDetail newbusiness = new BusinessDetail(sumRscore, sumRStar, countR, reviewidList);
				businessScore.put(bname, newbusiness);
				//record the business whose name contains one of the query word
			/*	for(String word : queryword){
					if(bname.contains(word)){
						if(!businessName.contains(bname)){
							businessName.add(bname);
						}
					}
				}*/
			}else{
				BusinessDetail existbusiness = businessScore.get(bname);
				existbusiness.setSumRscore(existbusiness.getSumRscore()+sumRscore);
				existbusiness.setSumRStar(existbusiness.getSumRStar()+sumRStar);
				existbusiness.setCountR(existbusiness.getCountR()+1);
				existbusiness.getReviewidList().add(rreview.docid());
				
			}
		}
		
		
		//calculate each sub score
		int bsize = businessScore.size();
		Rank[] rankingList = new Rank[bsize];
		int position = 0;
		for (Entry<String, BusinessDetail> entry : businessScore.entrySet()) {
		    String bname = entry.getKey();
		    BusinessDetail bdetail = entry.getValue();
		    double rscore = bdetail.getSumRscore()/bdetail.getCountR();
		    double rstar = bdetail.getSumRStar()/bdetail.getCountR();
		    double count = (double)bdetail.getCountR();
		    double[] score_count = {rscore, rstar, count};
		    Rank rank = new Rank(bname, score_count);
		    rankingList[position] = rank;
		    position++;
		}
		
		//rank 1. avarage(rStar); 2.count(reviews) for each business
		HashMap<String, double[]> subScoreList = new HashMap<String, double[]>();//double[log(N/R1), log(N/R2), finalScore]
		Arrays.sort(rankingList, new rstarComprator());
		int rstarRank = 1;
		for(Rank rank : rankingList){
			double[] temp = {Math.log10(bsize/rstarRank), 0.0,0.0};
			subScoreList.put(rank.getBusinessName(),temp);
			rstarRank++;
		}
		
		Arrays.sort(rankingList, new rstarComprator());
		int countRank = 1;
		for(Rank rank : rankingList){
			String bn = rank.getBusinessName();
			double[] temp = subScoreList.get(bn);
			temp[1]=Math.log10(bsize/countRank);
			subScoreList.put(bn,temp);
			countRank++;
		}
		
		for(Rank rank : rankingList){
			String bn = rank.getBusinessName();
			double score1 = rank.getScore_Count()[0];
			double score2 = subScoreList.get(bn)[0];
			double score3 = subScoreList.get(bn)[1];
			double finalScore = score1*score2*score3*rank.getScore_Count()[0];//finalScore calculation
			double[] temp = subScoreList.get(bn);
			temp[2] = finalScore;
			subScoreList.put(bn,temp);
		}
		
		Arrays.sort(rankingList, new finalComprator());
		
		String[] returnBusiness = new String[i];
		int b = 0;
		for(b = 0; b<i;b++){
			returnBusiness[b]= rankingList[b].getBusinessName();
		}
		
		return returnBusiness;
	}
	
	public List<String> getReviewidList(String bname){
		return businessScore.get(bname).getReviewidList();
	}
	

}