import java.util.*;

public class Protocol {
	 
	 public static int sent[] = new int[ConfigReader.getNumberOfNodes()];
	 public static int received[] = new int[ConfigReader.getNumberOfNodes()];
	 public static HashMap<Integer,Boolean> failureHasHappened = new HashMap<Integer,Boolean>();
	 public static int numFailEvents;
	 public static HashMap<Integer,Boolean> hasHappened = new HashMap<Integer,Boolean>();
	 public static HashMap<Integer,Integer> nodeCheck = new HashMap<Integer,Integer>();
	 public  static ArrayList<FailureEvent> myFailEventList = new ArrayList<FailureEvent>();
	 public static HashMap<Integer,FailureEvent> failureEvents = new HashMap<Integer,FailureEvent>();
		
	 public static void intialize(){
		 for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
			 sent[i] = received[i] = 0;
		 }
	 }
	 public synchronized static void checkpointAt(int failId,int checkpoint){
	    	nodeCheck.put(failId, checkpoint);
	    }
	 public synchronized static void addToFailList(FailureEvent f){
		 Protocol.myFailEventList.add(f);
	 }
	 public static void initialize(int numFailEvents){
			for(int i = 0; i < numFailEvents; i++){
				 failureHasHappened.put(i, false);
			}
		}
	 public static ArrayList<State> checkpoints = new ArrayList<State>();
	public static void checkpoint(){
		
		State s = new State(Process.myHost.getMe().active,ConfigReader.getMaxNumber(),Protocol.getClock(),Protocol.received,Protocol.sent);
		Logger.log(Process.myHost, "Checkpoint," + s.toString() );
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
		if(Protocol.myFailEventList.size() == 0){
			return false;
		}
		
		FailureEvent myNextFailEvent = Protocol.myFailEventList.get(0);
		
		return Protocol.failureHasHappened.get(myNextFailEvent.precedingEventId);
		
	}
	
	public static synchronized void broadcastFailure(){
		
	}
	
	
	
	public static void rollback(){
		
	}
	
	/************************Clock ************/

    public static Integer value = 0;
    public static int vectorClock[]=new int[ConfigReader.getNumberOfNodes()];
    
    //increment the clock

    public  static void incrClock(){
  	  synchronized(vectorClock){
  	      vectorClock[ConfigReader.getMe()]++;
    }
    }
    
    
    public static int getValue(){
  	  return vectorClock[ConfigReader.getMe()];
    }
    
    //update the clock
	  public  static void updateVectorClock(String vectorClockReceived[])
    {
		  synchronized(vectorClock){
  	  for(int i=0;i<ConfigReader.getNumberOfNodes();i++)
  	  {
  		  if(i==ConfigReader.getMe())
  		  vectorClock[i]=maximum(vectorClock[i],Integer.parseInt(vectorClockReceived[i]))+1;
  		  else
  		  vectorClock[i]=maximum(vectorClock[i], Integer.parseInt(vectorClockReceived[i]));
  	  }
		  }
    }
	  
	  
    public  static String getVectorClock()
    {
  	  synchronized(vectorClock){
  	  String vectorClockAppend=new String();
  	  for(int i=0;i<ConfigReader.getNumberOfNodes();i++)
  	  {
  		if(i!=ConfigReader.getNumberOfNodes()-1)  
  	  vectorClockAppend+=(vectorClock[i]+"~");
  		else
  	  vectorClockAppend+=(vectorClock[i]);
  	  }
  	  return vectorClockAppend;
  	  }
    }
    public static int [] getClock(){
  	  synchronized(vectorClock){
  		  return vectorClock;
  	  }
    }
    public static  int maximum(int a, int b)
    {
  	  if(a<b)
  		  return b;
  	  else
  		  return a;
    }
    
   
    public static String[] readVector(String tokens []){
  	   return  Arrays.copyOfRange(tokens,2,tokens.length);
    }
    public static int returnClockValue(String tokens[],int index)
    {
  	  String [] clockArray=Clock.readVector(tokens);
  	  return Integer.parseInt(clockArray[index]);
    }
    /**********************Clock ******************/

}
