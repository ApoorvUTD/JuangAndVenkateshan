import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
public class Protocol {
	 
	 public static int sent[] = new int[ConfigReader.getNumberOfNodes()];
	 public static int received[] = new int[ConfigReader.getNumberOfNodes()];
	 public static HashMap<Integer,Boolean> failureHasHappened = new HashMap<Integer,Boolean>();
	 public static int numFailEvents;
	 public static HashMap<Integer,Boolean> hasHappened = new HashMap<Integer,Boolean>();
	 public static HashMap<Integer,Integer> nodeCheck = new HashMap<Integer,Integer>();
	 public  static ArrayList<FailureEvent> myFailEventList = new ArrayList<FailureEvent>();
	 public static HashMap<Integer,FailureEvent> failureEvents = new HashMap<Integer,FailureEvent>();
	 public static String mode = "REB";
	 public static Event previousEvent = null;
	 public static int sentCount = 0;
	 public static HashMap<Integer,Boolean> passiveAt = new HashMap<Integer,Boolean>();
	 public static ArrayList<Integer> schedule= new ArrayList<Integer>();//schedule to keep messages
	 public static boolean active;
		
	 public static void printSchedule(){
			String line = "My Schedule once active :- ";
			
			for(int i = 0; i < schedule.size(); i++){
				line += schedule.get(i) + " ";
				if(passiveAt.get(i) != null){
					line += "sleep" + " ";
				}
				
			}
			Logger.log(Process.myHost,line);
			
		}

		 public static void buildSchedule(){
			    int nMessages = 0;
			    
			    while(nMessages <= ConfigReader.getMaxNumber()){
			    	 HashSet<Node> subset = ConfigReader.getSubsetNeighbors();
			    	 int maxActive = ConfigReader.getMaxPerActive();
			    	 int subsetLength = subset.size();
			    	 int length = (maxActive > subsetLength ) ? subsetLength : maxActive;
			    	 
			    	 nMessages += length;
			    	 int j = 0;
			    	 Iterator<Node> i = subset.iterator();
			    	 passiveAt.put(nMessages, true );
			    	 while(i.hasNext() && j < length){	 
			    	 schedule.add(i.next().getPID());
			    	 j++;
			    	 }
			    	 
			    }
		 }

	 public synchronized static void setMode(String s){
		 mode = s;
	 }
	 public synchronized static String getMode(){
		 return mode;
	 }
	 public static void intialize(){
		 for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
			 sent[i] = received[i] = 0;
		 }
	 }
	 public static void checkpointAt(int failId,int checkpoint){
	    	nodeCheck.put(failId, checkpoint);
	    }
	 public synchronized static void addToFailList(FailureEvent f){
		 Protocol.myFailEventList.add(f);
	 }
	 public static void initialize(int numFailEvents){
			for(int i = 0; i < numFailEvents; i++){
				 failureHasHappened.put(i, false);
			}
			active = Process.myHost.getMe().active;
			 for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
				 sent[i] = received[i] = 0;
			 }
		}
	 public static ArrayList<State> checkpoints = new ArrayList<State>();
	public static void checkpoint(){
		
		State s = new State(active,ConfigReader.getMaxNumber(),vectorClock,Protocol.received,Protocol.sent);
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
	
    
//	public static synchronized boolean shouldFail(){
//		if(Protocol.myFailEventList.size() == 0){
//			return false;
//		}
//		
//		FailureEvent myNextFailEvent = Protocol.myFailEventList.get(0);
//		
//		return Protocol.failureHasHappened.get(myNextFailEvent.precedingEventId);
//		
//	}
	public static boolean shouldFail(){
		return false;
	}
	
	public static synchronized void broadcastFailure(){
		
	}
	
	
	
	public static void rollback(){
		
	}
	
	/************************Clock ************/

    public static Integer value = 0;
    public static int vectorClock[]=new int[ConfigReader.getNumberOfNodes()];
    
    //increment the clock
    public synchronized static boolean isActive(){
    	   return active;
    }
    public synchronized static int getSentCount(){
    	return sentCount;
    }
    public synchronized static void setActive(boolean b){
    	active = b;
    }
    public synchronized static void incrClock(Node node){
  	 
  	    vectorClock[ConfigReader.getMe()]++;
  		PrintWriter currentWriter = Process.writersMap.get(node.getPID());
		currentWriter.println("REB~" + Process.myHost.getMe().getPID() + "~" + Protocol.getVectorClock());
		currentWriter.flush();
		//Protocol.increment("SENT",node.getPID());
		sent[node.getPID()]++;
		
		Protocol.checkpoint();
	  Event currentEvent = new Event(vectorClock);
	  	  
	  	  if(previousEvent != null){
	  		  int value = Event.compare(previousEvent,currentEvent);
	  		  if(value >= 0){
	  			  Logger.log(Process.myHost,"TOO BAD! BAD RECEIVE EVENT SYNCHRONIZATION");
	  		  }
	  	  }
		if(Protocol.shouldFail()){
			 //START RECOVERY
			 Protocol.setMode("RECOVERY");
			 return;
		 }

		sentCount++;
		if(passiveAt.get(sentCount) != null){
			Protocol.active = false;
		}
    }
    
    
    public static int getValue(){
  	  return vectorClock[ConfigReader.getMe()];
    }
    
    //update the clock
	  public synchronized  static void updateVectorClock(String vectorClockReceived[])
    {
	
  	  for(int i=0;i<ConfigReader.getNumberOfNodes();i++)
  	  {
  		  if(i==ConfigReader.getMe())
  		  vectorClock[i]=maximum(vectorClock[i],Integer.parseInt(vectorClockReceived[i]))+1;
  		  else
  		  vectorClock[i]=maximum(vectorClock[i], Integer.parseInt(vectorClockReceived[i]));
  	  }
  	   
		  
    }
	  public synchronized  static void updateVectorClock(String vectorClockReceived[],String [] tokens)
	    {
		
	  	  for(int i=0;i<ConfigReader.getNumberOfNodes();i++)
	  	  {
	  		  if(i==ConfigReader.getMe())
	  		  vectorClock[i]=maximum(vectorClock[i],Integer.parseInt(vectorClockReceived[i]))+1;
	  		  else
	  		  vectorClock[i]=maximum(vectorClock[i], Integer.parseInt(vectorClockReceived[i]));
	  	  }
	  	  
	  	  Event currentEvent = new Event(vectorClock);
	  	  
	  	  if(previousEvent != null){
	  		  int value = Event.compare(previousEvent,currentEvent);
	  		  if(value >= 0){
	  			  Logger.log(Process.myHost,"TOO BAD! BAD RECEIVE EVENT SYNCHRONIZATION");
	  		  }
	  	  }
	  	  
	  		  previousEvent = currentEvent;
	  	      checkpoint();
	  	  
	  	  int pid = Integer.parseInt(tokens[1]);
	  	Logger.log(Process.myHost, "Received message from " + pid);
	  	  
	  	  Message incomingMessage = new Message(vectorClock[pid],pid,tokens[0]);
	  	    
	  //	Message incomingMessage  = new Message(Protocol.returnClockValue(tokens, Integer.parseInt(tokens[1])),Integer.parseInt(tokens[1]),tokens[0]);
 	//	messageQueue.add(incomingMessage);
 	
 		//take a checkpoint here!
 		
			  
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
