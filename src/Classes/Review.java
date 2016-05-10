package Classes;

public class Review {
	
	protected String docid;
	protected String docno;
	protected double score;
	protected double star;
	
	public Review( String docid, String docno, double score,double star) {
		this.docid = docid;
		this.docno = docno;
		this.score = score;
		this.star = star;
	}
	
	public String docid() {
		return docid;
	}
	
	public String docno() {
		return docno;
	}
	
	public double score() {
		return score;
	}
	
	public double star() {
		return star;
	}
	
	public void setDocid( String docid ) {
		this.docid = docid;
	}
	
	public void setDocno( String docno ) {
		this.docno = docno;
	}
	
	public void setScore( double score ) {
		this.score = score;
	}
	
	public void setStar( double star ) {
		this.star = star;
	}
	
	
}
