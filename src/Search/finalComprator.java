package Search;

import java.util.Comparator;

public class finalComprator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Rank r1=(Rank)o1; 
		Rank r2=(Rank)o2;
		if(r1.getScore_Count()[2] != r2.getScore_Count()[2])
			return r1.getScore_Count()[2]<r2.getScore_Count()[2]? 1:-1;
		else
			return 0;
	}

}
