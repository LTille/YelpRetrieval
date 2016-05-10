package Search;

public class Rank {
	private String businessName;
	private double [] score_count;
	
	public Rank(String businessName, double [] score_count) {
		this.businessName = businessName;
		this.score_count = score_count;
	}
	
	public String getBusinessName(){
		return businessName;
	}
	
	public void detBusinessName(String newName){
		businessName = newName;
	}
	
	public double [] getScore_Count(){
		return score_count;
	}
	
	public void detScore_Count(double [] newArray){
		score_count = newArray;
	}
	
}
