import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.Review;
import DataPreprocess.BusinessScore;
import Indexing.BuildIndex;
import Indexing.MyIndexReader;
import Indexing.OriginReviewReader;
import Search.RetrievalModel;


public class Process {

	public static void main(String[] args) throws Exception {
		
		 BuildIndex process = new BuildIndex(); 
		BusinessScore bs = new BusinessScore();
		 process.PreProcess(); //preprocess txt data before building index
		/* process.OriginReview();
		
		 process.WriteOriginIndex("origin");//get the origin index file
		 process.PreProcess(); //preprocess txt data before building index
		 process.WriteIndex("process"); //build index
		 process.WriteOriginIndex("origin");//get the origin index file
		 //process.ReadIndex("park");
		 */

		
		String query = "good parking"; 
		
		MyIndexReader ixreader = new MyIndexReader();
		RetrievalModel model = new RetrievalModel(ixreader);
		//System.out.println(ixreader.getDocno(-1));
		OriginReviewReader oir = new OriginReviewReader();
		
		List<Review> rreviews=model.retrieveReview(process.queryProcess(query), 1000);
		String[] rbusinesses=model.retrieveBusiness(rreviews, query, 20);
		
		if (rbusinesses != null) {
			for (int i=0; i<rbusinesses.length;i++) {
				System.out.println(bs.getScore(rbusinesses[i]));
				//System.out.println(bs);
			/*	for(String r: model.getReviewidList(rbusinesses[i])){
					System.out.println(r+" "+oir.getReviewContent(Integer.parseInt(r))+"\n");
				}*/
				
			}
		}
		
		
	}
	
	
}
