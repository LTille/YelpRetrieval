package Search;

import java.util.ArrayList;

public class BusinessDetail {
	private double sumRscore; //sum of LM score of related reviews
	private double sumRStar; //sum of stars of related reviews
	private int countR; //number of related reviews
	private ArrayList<String> reviewidList; //list of docid of related reviews
	
	public BusinessDetail(double sumRscore, double sumRStar, int countR, ArrayList<String> reviewidList) {
		this.sumRscore = sumRscore;
		this.sumRStar = sumRStar;
		this.countR = countR;
		this.reviewidList = reviewidList;
	}
	
	public double getSumRscore(){
		return sumRscore;
	}
	
	public void setSumRscore(double newSumRscore){
		sumRscore = newSumRscore;
	}
	
	public double getSumRStar(){
		return sumRStar;
	}
	
	public void setSumRStar(double newSumRStar){
		sumRStar = newSumRStar;
	}
	
	public int getCountR(){
		return countR;
	}
	
	public void setCountR(int newCountR){
		countR = newCountR;
	}
	
	public ArrayList<String> getReviewidList(){
		return reviewidList;
	}
	
	public void setReviewidList(ArrayList<String> newReviewidList){
		reviewidList = newReviewidList;
	}
	

}
