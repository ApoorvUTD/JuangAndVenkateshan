import java.util.*;

public class Protocol {
	 
	 public static int sent[] = new int[ConfigReader.getNumberOfNodes()];
	 public static int received[] = new int[ConfigReader.getNumberOfNodes()];
	 
	 
//	 public static boolean[] isSent =new boolean[ConfigReader.getNumberOfNodes()];
	 
	
//	 public static void messagesSent(){
//		 for(int i=0;i<ConfigReader.subsetNeighbors.size();i++){
//			 
//		 }
//	 }
	 
//	 public static void setBooleanSent(String type,int PID){
//		 if (type.equals("SENT")){
//isSent[PID]=true;			 
//		 }
//	 }
	
//	 public static void MessageSchedule(){
//		 
//		 if(sentCount==ConfigReader.getSubsetNeighbors().size()){
//			 Process.myHost.getMe().active=false;
//			 PassiveAt.put(sentCount, true);
//		 }
//		 
//		 
//		 //
//		 
//		 //if passive turn to active when not crossed maxNumber
//		 if(sentCount==ConfigReader.getMaxNumber()){
//			 Process.myHost.getMe().active=false;//marking it passive
//		 }
//		 else{
//			 Process.myHost.getMe().active=true;
//		 }
//	 }
	 
	 public static void intialize(){
		 for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
			 sent[i] = received[i] = 0;
		 }
	 }
	 public static ArrayList<State> checkpoints = new ArrayList<State>();
	public static void checkpoint(State s){
		Logger.log(Process.myHost, "Bitchpont," + s.toString() );
		checkpoints.add(s);
	}
	public static synchronized void increment(String type,int PID) {
		// TODO Auto-generated method stub
      if (type.equals("SENT")){
    	  sent[PID]++;
    	  
    	  
      }
      else{
    	  received[PID]++;
      }

	}
	
    
	public static synchronized boolean shouldFail(){
		if(Process.myHost.myFailEventList.size() == 0){
			return false;
		}
		
		FailureEvent myNextFailEvent = Process.myHost.myFailEventList.get(0);
		
		return Process.failureHasHappened.get(myNextFailEvent.precedingEventId);
		
	}
	
	public static synchronized void broadcastFailure(){
		
	}
	
	
	
	public static void rollback(){
		
	}
}
