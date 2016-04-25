import java.io.PrintWriter;
import java.util.*;

public class TCPClient implements Runnable{

	private int pid;
	public TCPServer myServer;
	public Host myHost;
	public State currentState;
	public ArrayList<HostPing> connections = new ArrayList<HostPing>();
	public ArrayList<ClientSock> clientRequests = new ArrayList<ClientSock>();
	static int sentCount = 0;
	public static ArrayList<Integer> schedule= new ArrayList<Integer>();//schedule to keep messages
	 public static HashMap<Integer,Boolean> passiveAt = new HashMap<Integer,Boolean>();
	
	public static void sendREBMessage(Node node){
		
		if (Process.myHost.getMe().active==true){	 	
			Logger.log(Process.myHost,"Sending Message to "+ node.getPID());
			Clock.incrClock();
			PrintWriter currentWriter = Process.writersMap.get(node.getPID());
			currentWriter.println("REB~" + Process.myHost.getMe().getPID() + "~" + Clock.getVectorClock());
			currentWriter.flush();
			Protocol.increment("SENT",node.getPID());
			sentCount++;
			if(passiveAt.get(sentCount) != null){
				Process.myHost.getMe().active = false;
			}
			//Protocol.isSent(node.getPID())
			//Logger.log(Process.myHost,"Sending Message to "+ node.getPID());

		}
	}
	
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
	 
	 public static void startREBProtocol(){
		 int index = sentCount;
		 while(passiveAt.get(sentCount)  == null){
			 sendREBMessage(Process.getNodeAtIndex(schedule.get(index++)));
			 Protocol.checkpoint(new State(Process.myHost.getMe().active,ConfigReader.getMaxNumber(),Clock.vectorClock,Protocol.received,Protocol.sent));
				try {
					Thread.sleep(ConfigReader.getMinSendDelay());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					
				}
			
		 }
	 }
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		

		for(int k = 0; k < Process.myHost.neighborList.size(); k++){
			Node neighbor = Process.getNodeAtIndex(k);
			ClientSock c = new ClientSock(neighbor);
			Thread t  = new Thread(c);
			t.start();
			try{
			t.join();
			}
			catch(Exception e){
				
			}
			
		  
		}

		//looping thru subset of neighbor
		int maxPerActive = ConfigReader.getMaxPerActive();
		int subsetCount = ConfigReader.getSubsetNeighbors().size();
		int count =  (maxPerActive <= subsetCount) ? maxPerActive : subsetCount;
		Logger.log(Process.myHost,"Count : " + count);
		buildSchedule();
		printSchedule();
		if(Process.myHost.getMe().active){
					
				   startREBProtocol();
				}
			
		}

		
	}



