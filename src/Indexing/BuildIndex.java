package Indexing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

import Classes.Path;
import DataPreprocess.OriginReview;
import DataPreprocess.ReviewCollection;
import DataPreprocess.StopWordRemover;
import DataPreprocess.WordNormalizer;
import DataPreprocess.WordTokenizer;

public class BuildIndex {

	public BuildIndex(){
		
	}
	
	public void OriginReview() throws Exception{
		OriginReview or = new OriginReview();
		BufferedWriter bw = new BufferedWriter(new FileWriter(Path.preReview));
    
        int count=0;
		Map<String, String> doc = null;

		while((doc=or.nextReview())!=null){
			String docno = doc.keySet().iterator().next();
			bw.append(docno + "\n");
			bw.append(doc.get(docno));
			bw.append("\n");
			count++;
			if(count%10000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("preprocess totaly document count:  "+count);
		bw.close();
	}
	/*
	 * preprocess the review content before building index
	 * remove stop words, stemming, lower case
	 */
	public void PreProcess() throws Exception {
		// Loading the collection file and initiate the DocumentCollection class
		ReviewCollection corpus = new ReviewCollection();
		// initiate the BufferedWriter to output result
		FileWriter wr = new FileWriter(Path.preprocessed);;
		// process the corpus, document by document, iteractively
		int count=0;
				
		// loading stopword list and initiate the StopWordRemover and WordNormalizer class
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();

		// initiate a doc object, which can hold review number and review content
		Map<String, Object> doc = null;
	
		while ((doc = corpus.nextDocument()) != null) {
			// load document number of the document
			String docno = doc.keySet().iterator().next();
			System.out.println(doc.get(docno));
            
			// load document content
			char[] content = (char[]) doc.get(docno);
			
			// write docno into the result file
			wr.append(docno + "\n");

			// initiate the WordTokenizer class
			WordTokenizer tokenizer = new WordTokenizer(content);

			// initiate a word object, which can hold a word
			char[] word = null;

			// process the document word by word iteratively
			while ((word = tokenizer.nextToken()) != null) { 
				// each word is transformed into lowercase
				//System.out.println("word is:"+Arrays.toString(word));
				word = normalizer.lowercase(word);

				if (!stopwordRemover.isStopword(word)){
					//System.out.println("after stop"+Arrays.toString(word));
					wr.append(normalizer.stem(word) + " ");}
					//stemmed format of each word is written into result file	
			}
			wr.append("\n");// finish processing one document
			count++;
			if(count%10000==0)
				System.out.println("finish "+count+" docs");
		 }
		
		System.out.println("preprocess totaly document count:  "+count);
		wr.close();
	}
	
	public void WriteOriginIndex(String type) throws Exception{
		
		PreProcessedCorpusReader corpus = new PreProcessedCorpusReader(type);
		OriginIndex output = new OriginIndex();
		
		Map<String,String> doc=null;
		int count=0;
		while ((doc = corpus.nextReview()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			String content = doc.get(docno);
			// index this document
			output.index(docno, content); 

			count++;
			if(count%10000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("Origin totaly document count:  "+count);
		output.close();
		
	}
	public void WriteIndex(String type) throws Exception {
		// Initiate pre-processed collection file reader
		PreProcessedCorpusReader corpus=new PreProcessedCorpusReader(type);
		
		// initiate the output object
		MyIndexWriter output=new MyIndexWriter();
		
		// initiate a doc object, which can hold document number and document content of a document
		Map<String, String> doc = null;

		int count=0;
		// build index of corpus document by document
		while ((doc = corpus.nextReview()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			String content = doc.get(docno);
			// index this document
			output.index(docno, content); 
			
			count++;
			if(count%10000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("Normal document count:  "+count);
		output.close();
	}
	
	public void ReadIndex(String token) throws Exception {
		// Initiate the index file reader
		MyIndexReader ixreader=new MyIndexReader();
		
		// do retrieval
		int df = ixreader.DocFreq(token);
		long ctf = ixreader.CollectionFreq(token);
		System.out.println(" >> the token \""+token+"\" appeared in "+df+" documents and "+ctf+" times in total");
		if(df>0){
			int[][] posting = ixreader.getPostingList(token);
			for(int ix=0;ix<posting.length;ix++){
				int docid = posting[ix][0];
				int freq = posting[ix][1];
				String docno = ixreader.getDocno(docid);
				System.out.printf("    %20s    %6d    %6d\n", docno, docid, freq);
			}
		}
		ixreader.close();
	}
	
	public String queryProcess(String query) throws Exception{
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();
		StringBuilder sb = new StringBuilder();
		char[] content = query.toCharArray();
		// initiate the WordTokenizer class
		WordTokenizer tokenizer = new WordTokenizer(content);

		// initiate a word object, which can hold a word
		char[] word = null;

		// process the document word by word iteratively
		while ((word = tokenizer.nextToken()) != null) { 

			// each word is transformed into lowercase
			word = normalizer.lowercase(word);
            //remove stop words and stemming
			if (!stopwordRemover.isStopword(word))
				sb.append(normalizer.stem(word)+" ");	

		 }
		return sb.toString();
	}
}
