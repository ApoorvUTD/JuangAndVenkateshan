import java.util.Comparator;

public class Event {
   int [] vector;
   public Event(int []v){
	   vector = new int[ConfigReader.getNumberOfNodes()];
	   for(int i = 0; i < v.length; i++){
		   vector[i] = v[i];
	   }
   }
   
   public static int compare(Event x, Event y){

		boolean lessThan = true;
		boolean greaterThan = true;
		for(int i = 0; i < x.vector.length ; i++){
			if(x.vector[i] > y.vector[i]){
				lessThan = false;
			   break;
			}
		}
		for(int i = 0; i < x.vector.length; i++){
			if(x.vector[i] < y.vector[i]){
				greaterThan = false;
			}
		}
		if(lessThan == false && greaterThan == false){
			return 0;
		}
		
		if(lessThan == true){
			return -1;
		}
		
       return 1;
		
	
   }
   
   
}

class EventComparator implements Comparator<Event>{
	public int compare(Event x, Event y){
		boolean lessThan = true;
		boolean greaterThan = true;
		for(int i = 0; i < x.vector.length ; i++){
			if(x.vector[i] > y.vector[i]){
				lessThan = false;
			   break;
			}
		}
		for(int i = 0; i < x.vector.length; i++){
			if(x.vector[i] < y.vector[i]){
				greaterThan = false;
			}
		}
		if(lessThan == false && greaterThan == false){
			System.out.println("NO! PROTOCOL DOES NOT WORK!");
			System.exit(0);
		}
		
		if(lessThan == true){
			return -1;
		}
		
        return 1;
		
	}
}
