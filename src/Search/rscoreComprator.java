package Search;

import java.util.Comparator;

public class rscoreComprator implements Comparator {

	@Override
	
	public int compare(Object arg0, Object arg1) {
		Rank r1=(Rank)arg0; 
		Rank r2=(Rank)arg1;
		if(r1.getScore_Count()[0] != r2.getScore_Count()[0])
			return r1.getScore_Count()[0]<r2.getScore_Count()[0]? 1:-1;
		else
			return 0;
	} 

}
