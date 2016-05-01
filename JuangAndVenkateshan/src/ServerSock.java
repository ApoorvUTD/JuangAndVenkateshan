import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSock implements Runnable {

	Socket socket;
	TCPServer myServer;
	public static Host myHost;
	String message;
	BufferedReader reader;
	PrintWriter writer;
	String incomingHost;
	int incomingPID;
	ConfigReader conf;
	public static ArrayList<Node> subsetNeighbor=new ArrayList<Node>();
	public static LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message> ();
	public static LinkedBlockingQueue<Message> protocolMessageQueue = new LinkedBlockingQueue<Message>();
	//public static PriorityQueue<Message> waitingQueue = new PriorityQueue<Message>(ConfigReader.getNumberOfNodes()*Config.getNumberOfRequests(), new MessageComparator());
	public static Map<Integer, PrintWriter> mapNodeWriter = new HashMap<Integer,PrintWriter>();
	public String role;
	public static boolean participation = true;
	
	
	public synchronized static boolean shouldParticipate(){
		return participation;
	}
	
	public synchronized static void setParticipation(boolean b){
		participation = b;
	}
	public void setRole(String r){
		role = r;

	}


	public ServerSock(Socket s,TCPServer server, Host h){
		socket = s;
		myServer = server;
		myHost = h;
		if(socket != null){
			incomingHost = socket.getInetAddress().getHostName().split("[.]")[0];
			incomingPID = myHost.hostMap.get(incomingHost);
		}
	}

//	public static void onReceiveMessage(Message incomingMessage){
//		if(Protocol.getMode().equals("RECOVERY")){
//			messageQueue.removeAll(messageQueue);
//		}
//		 Logger.log(Process.myHost,"Receive message from " + incomingMessage.getPID());
//		if(incomingMessage.getMessageType().equals("REB")){
//			Protocol.increment("RECIEVED",incomingMessage.getPID());
//			Protocol.checkpoint();
//		   
//		if (!myHost.getMe().active && TCPClient.sentCount >= ConfigReader.getMaxNumber()){
//			//do nothing
//			Logger.log(Process.myHost,"NOT GONNA TURN ACTIVE NO MODE!");
//		}
//		if (!myHost.getMe().active && TCPClient.sentCount < ConfigReader.getMaxNumber()){
//			myHost.getMe().active = true;
//			Logger.log(Process.myHost,"TURING ACTIVE!");
//			TCPClient.startREBProtocol();
//		}
//		}
//		
//	}

//	public static void sendREBMessage(Node node){
//		Logger.log(myHost,"Sending Message to "+ node.getPID());
//		if (myHost.getMe().active==true){
//			Clock.incrClock();
//			PrintWriter currentWriter = mapNodeWriter.get(node.getPID());
//			currentWriter.println("REB~" + myHost.getMe().getPID() + "~" + Clock.getValue());
//			currentWriter.flush();
//		}
//
//	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(role.equals("LISTENONPORT")){//some condition need to go here man
			try {
				
				Logger.log(Process.myHost, "LISTENPORT-----> "+Process.myHost.getMe().getPID());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				mapNodeWriter.put(incomingPID, writer);
				while(true){
					
					
					Logger.log(Process.myHost,"Waiting for a new message");
					message = reader.readLine();
					Logger.log(Process.myHost,"Got message from " + incomingPID);
					String []tokens = message.split("[~]");
                     if(Protocol.getMode().equals("RECOVERY")){
                    	 switch(tokens[0]){
                    	  
                    	 case "REB" : break; 
                    	 
                    	 case "FAIL" : break; //i know this already!
                    	         
                    	 case "ROLLBACK" : 
                    		   
                    		    // i am going to do something about this
                    	 
                    	 }
                    	 
                     }
                     else{
                    	 
                    	 switch(tokens[0]){
                    	 case "REB" : 
                    		 		//receive event logged!
                    		 		int sentCount = Protocol.getSentCount();
                    		 		int maxNum = ConfigReader.getMaxNumber();
                    		 		boolean active = Protocol.isActive();
                    		 		if(!active && sentCount >= maxNum){
                    		 			
                    		 		}
                    		        if(!active && sentCount < maxNum ){
                    		        	Protocol.setActive(true);
                    		        	Logger.log(Process.myHost,"TURING ACTIVE!");
                    		        	Protocol.updateVectorClock(Protocol.readVector(tokens),tokens);
                    		        	TCPClient.startREBProtocol();
                    		        }
                    		 		
                    		 		break;
                    	 case "FAIL" : //abort participating in reb and flood to neighbors
                    		            break;
                    		            
                    	 case "ROLLBACK" : //shouldn't come here! let's see about that
                    		                 Logger.log(myHost,"Shouldn't have come till here!");
                    	 }
                    	
                     }
					

				}
			}
			catch(Exception e){

			}


		}
		else {
			while(true){
			
			Message currentMessage=null;
			try {
				currentMessage=messageQueue.take();
				//onReceiveMessage(currentMessage);
				Logger.log(Process.myHost,"Receive message from " + currentMessage.getPID());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
			}if(myHost.getMe().active==false ){
				
			}  
			}
			

		}

	}
}
