
public class Clock {
	  public static Integer value = 0;
      public  static void incrClock(){
   	      value++;
   	   
     }
     public static int getValue(){
   	  return value;
     }
	 public static void updateClock(int value) {
		  if(Clock.value < value){
			  Clock.value = value + 1;
		  }
		  else{
			  Clock.value = Clock.value + 1;
		  }
		 
	}

}
