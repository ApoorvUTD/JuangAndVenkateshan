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
	 public static boolean failureAware = false;
	 public static int roundsCompleted = 0;
	 public static int rollbacksSent = 0;
	 public static int rollbacksReceived = 0;
	 public static int failsSent = 0;
	 public static int failsReceived = 0;
	 public static int round = 0;
	 public static boolean rollbackAware = false;
	 
	 
	 public static HashMap<Integer,PriorityQueue<BufferedState>> rebBufferedMessages = new HashMap<Integer,PriorityQueue<BufferedState>>();	
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
		 failureAware = true;
		 announce();
	 }
	 public synchronized static boolean isFailureAware(){
		 return failureAware;
	 }
	 public synchronized static boolean isRollbackAware(){
		 return rollbackAware;
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
			 failureHasHappened.put(-1, true);
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
	
    
	public static  boolean shouldFail(){
		if(Protocol.myFailEventList.size() == 0){
			return false;
		}
		
		FailureEvent myNextFailEvent = Protocol.myFailEventList.get(0);
		
		return Protocol.failureHasHappened.get(myNextFailEvent.precedingEventId);
		
	}
	
	public static synchronized void broadcastFailure(){
		
	}
	
	
	
	public static void rollback(){
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(checkpoints.size()-1);
	    checkpoints = (ArrayList<State>)checkpoints.subList(0, randomInt);
	}
	public static void sendRollbacks(){
		
		for(int i = 0; i < Process.myHost.neighborList.size(); i++){
			
			//send rollback message;
			
		}
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
    public static void announce(){
    	FailureEvent event = Protocol.myFailEventList.get(0);
    	
    	for(int i = 0; i < Process.myHost.neighborList.size(); i++){
    		//send ANNOUNCE MESSAGE!
    		int currentPID = Process.myHost.neighborList.get(i);
    	      PrintWriter writer = Process.writersMap.get(currentPID);
    	      writer.println("FAIL~" + (event.precedingEventId + 1));
    	      writer.flush();
    	      failsSent++;
    	}
    }
    
    public synchronized static void incrFailsReceived(){
    	failsReceived++;
        
    }
    public synchronized static void round(){
   
    }

    public synchronized static boolean failsProcessed(){
    	int size = Process.myHost.neighborList.size();
    	if(failsSent == size && failsReceived == size){
    		return true;
    	}
    	return false;
    }
    public synchronized static boolean rollbacksProcessed(){
    	int size = Process.myHost.neighborList.size();
    	if(rollbacksSent == size && rollbacksReceived == size){
    		return true;
    	}
    	return false;
    	
    }
    public synchronized static int getRound(){
    	return round;
    }
    public synchronized static void incrRound(){
    	round++;
    }
    public synchronized static void incrClock(Node node){
  	 
    	if(mode.equals("RECOVERY")){
    		return;
    	}
  	    vectorClock[ConfigReader.getMe()]++;
  	    int pid  = node.getPID();
  		PrintWriter currentWriter = Process.writersMap.get(node.getPID());
  		currentWriter.println("REB|" + sent[pid] + "~" + Process.myHost.getMe().getPID() + "~" + Protocol.getVectorClock());
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
			rollback(); 
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
	  
	  public synchronized  static void updateVectorClock(String vectorClockReceived[],String [] tokens,int sequenceNumber)
	    {
		  
		    if(mode.equals("RECOVERY")){
		    	return;
		    }
			int pid = Integer.parseInt(tokens[1]);
			Logger.log(Process.myHost,"PID is " + pid);
			int [] vectorReceived = new int [ConfigReader.getNumberOfNodes()];
			for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
				vectorReceived[i] = Integer.parseInt(vectorClockReceived[i]);
			}
			PriorityQueue<BufferedState> bufferedMessages = rebBufferedMessages.get(pid);
			if( bufferedMessages == null){
				bufferedMessages = new PriorityQueue<BufferedState>(1000,new BufferedStateComparator());
				rebBufferedMessages.put(pid,bufferedMessages);
			}
		    
		  	if(sequenceNumber > received[pid]){
		  		bufferedMessages.add(new BufferedState(sequenceNumber, vectorReceived));
		  		Logger.log(Process.myHost,"Received out of order message! Buffering!");
		  		return;
		  		
		  	}
		
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
	  		
	  	    	
	  		received[pid]++;
	  	    
	  		
	  	      checkpoint();
	  	      if (shouldFail()){
	  	    	  rollback();
	  	    	  setMode("RECOVERY");
	  	    	  Logger.log(Process.myHost,"DISCARDING BUFFERED MESSAGES!, TIME TO FAIL!");
	  	    	  for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
	  	    		  rebBufferedMessages.put(i, null);
	  	    	  }
	  	    	  
	  	    	  return;
	  	      }
	  	    	// Logger.log(Process.myHost,"CHECKING w.r.t + " + pid + ": " + received[pid] + " " + bufferedMessages.peek().sequenceNumber);
		  	     
	  	       if(!bufferedMessages.isEmpty() && received[pid] == bufferedMessages.peek().sequenceNumber){
	  	    	 Logger.log(Process.myHost,"INGA IRUKEN!");
	  	    	  while(!bufferedMessages.isEmpty() && received[pid] == bufferedMessages.peek().sequenceNumber){
	  	    		 BufferedState currentState = bufferedMessages.peek();
	  	    		 for(int j = 0; j < ConfigReader.getNumberOfNodes(); j++){
	  	    			 vectorClock[j] = maximum(vectorClock[j],currentState.vectorClock[j]);
	  	    		 }
	  	    		 vectorClock[Process.myHost.getMe().getPID()]++;
	  	    		 received[pid]++;
	  	    		 checkpoint();
	  	    		  if (shouldFail()){
	  		  	    	  rollback();
	  		  	    	  setMode("RECOVERY");
	  		  	    	  Logger.log(Process.myHost,"DISCARDING BUFFERED MESSAGES!, TIME TO FAIL!");
	  		  	    	  for(int i = 0; i < ConfigReader.getNumberOfNodes(); i++){
	  		  	    		  rebBufferedMessages.put(i, null);
	  		  	    	  }
	  		  	    	  
	  		  	    	  return;
	  		  	      }
	  	    		 
	  	    		 bufferedMessages.remove();
	  	    	  }
	  	    	 Logger.log(Process.myHost,"Done checkpointing buffered messages uptill now w.r.t pid = " + pid );
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
