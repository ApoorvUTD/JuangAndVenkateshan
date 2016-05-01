import java.util.Comparator;
public class BufferedState{
   int [] vectorClock;
   int sequenceNumber;
   public BufferedState(int n, int [] v){
	   sequenceNumber = n;
	   vectorClock = v;
   }
   
   public int compare(BufferedState x, BufferedState y){
	    if(x.sequenceNumber <= y.sequenceNumber){
	    	return -1;
	    }
	    return 1;
   }
}

class BufferedStateComparator implements Comparator<BufferedState>{
	
	   public int compare(BufferedState x, BufferedState y){
		    if(x.sequenceNumber <= y.sequenceNumber){
		    	return -1;
		    }
		    return 1;
	   }
	}

