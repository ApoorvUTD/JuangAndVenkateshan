import java.io.PrintWriter;
import java.util.*;

public class TCPClient implements Runnable{

	private int pid;
	public TCPServer myServer;
	public Host myHost;
	public State currentState;
	public ArrayList<HostPing> connections = new ArrayList<HostPing>();
	public ArrayList<ClientSock> clientRequests = new ArrayList<ClientSock>();
	
	 
	
	public static void sendREBMessage(){
		    int sentCount = Protocol.getSentCount();
		    Node node = Process.getNodeByPID(Protocol.schedule.get(sentCount));
			Protocol.incrClock(node);
		    
		
	}
	
		 
	 public static void startREBProtocol(){
		 
		 while(Protocol.isActive() && Protocol.getMode().equals("REB")){
			   sendREBMessage();
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
				Logger.log(Process.myHost,"THREADEXCEPTION");
			}
			
		  
		}
		
		
		Logger.log(Process.myHost,"CLIENTREADY");
         Protocol.setCanSend(true);
		//looping thru subset of neighbor
		int maxPerActive = ConfigReader.getMaxPerActive();
		int subsetCount = ConfigReader.getSubsetNeighbors().size();
		int count =  (maxPerActive <= subsetCount) ? maxPerActive : subsetCount;
		Logger.log(Process.myHost,"Count : " + count);
		Protocol.buildSchedule();
		Protocol.printSchedule();
		if(Protocol.isActive()){
	 	   startREBProtocol();
			   
				}
			
		}

		
	}



