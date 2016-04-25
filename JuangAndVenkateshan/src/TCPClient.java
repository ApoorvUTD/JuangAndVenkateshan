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
		Logger.log(Process.myHost,"Sending Message to "+ node.getPID());
		if (Process.myHost.getMe().active==true){	 	
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

	 public static void buildSchedule(){
		    int nMessages = 0;
		    
		    while(nMessages <= ConfigReader.getMaxNumber()){
		    	 ArrayList<Node> subset = ConfigReader.getSubsetNeighbors();
		    	 while(subset.size() == 0){
		    		 subset = ConfigReader.getSubsetNeighbors();
		    	 }
		    	 int maxActive = ConfigReader.getMaxPerActive();
		    	 int subsetLength = subset.size();
		    	 int length = (maxActive > subsetLength ) ? subsetLength : maxActive;
		    	 
		    	 nMessages += length;
		    	 passiveAt.put(nMessages, true );
		    	 for(int i = 0; i < length; i++){
		    	 schedule.add(Process.getPIDAtIndex(i));
		    	 }
		    }
	 }
	 
	 public static void startREBProtocol(){
		 int index = sentCount  + 1 ;
		 while(passiveAt.get(index)  == null){
			 sendREBMessage(ConfigReader.getSubsetNeighbors().get(index++));
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
		
		ArrayList<Node> mySubsetNeighbors = ConfigReader.getSubsetNeighbors();
		
		if(mySubsetNeighbors == null){
			Logger.log(Process.myHost,"NULLLLLL");
			return;
		}
		String line = "My neighbors are :- ";
		for(int i = 0; i < Process.myHost.neighborList.size(); i++){
			line += Process.myHost.neighborList.get(i) + " ";
		}
		Logger.log(Process.myHost,line);
		line = "My subset neighbors are :- ";
		for(int j = 0; j < mySubsetNeighbors.size(); j++){
			line += mySubsetNeighbors.get(j).getPID() + " ";
		}
		Logger.log(Process.myHost, line);
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
		int i;
		for(i = 0; i < Process.myHost.neighborList.size(); i++){
			if(Process.writersMap.get(Process.getPIDAtIndex(i)) == null){
				Logger.log(Process.myHost, "NOT READY TO PARTICIPATE IN REB");
				break;
			}
			
		}
		
		if(i == Process.myHost.neighborList.size()){
			Logger.log(Process.myHost,"MEALS READY");
		}
		//looping thru subset of neighbor
		Logger.log(Process.myHost,"MaxPerActive : " + ConfigReader.getMaxPerActive() + ", My # of neighbors : " + ConfigReader.getSubsetNeighbors().size());
		int maxPerActive = ConfigReader.getMaxPerActive();
		int subsetCount = ConfigReader.getSubsetNeighbors().size();
		int count =  (maxPerActive <= subsetCount) ? maxPerActive : subsetCount;
		Logger.log(Process.myHost,"Count : " + count);
			if(Process.myHost.getMe().active){
		
				   startREBProtocol();
				}
			
		}

		
	}



