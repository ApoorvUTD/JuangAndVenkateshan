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
	//public static PriorityQueue<Message> waitingQueue = new PriorityQueue<Message>(ConfigReader.getNumberOfNodes()*Config.getNumberOfRequests(), new MessageComparator());
	public static Map<Integer, PrintWriter> mapNodeWriter = new HashMap<Integer,PrintWriter>();
	public String role;
	public static int numberOfMessagesSent;
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

	public static void onRecieveMessage(Message incomingMessage){
		if (!myHost.getMe().active && numberOfMessagesSent>=ConfigReader.getMaxNumber()){
			//do nothing
		}
		if (!myHost.getMe().active && numberOfMessagesSent<ConfigReader.getMaxNumber()){
			myHost.getMe().active=true;
		}
		

	}

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
					Clock.updateVectorClock(Clock.readVector(tokens));
					//Message-->clock,pid,type
					Message incomingMessage  = new Message(Integer.parseInt(tokens[2]),Integer.parseInt(tokens[1]),tokens[0]);
					messageQueue.add(incomingMessage);


				}
			}
			catch(Exception e){

			}
			//send REB message
//			if(ConfigReader.getMaxPerActive()<ConfigReader.getSubsetNeighbors().size() && myHost.getMe().active){
//			for (int i=0;i<ConfigReader.getMaxPerActive();i++){
//				try {
//					Thread.sleep(ConfigReader.getMinSendDelay());
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					
//				}
//				sendREBMessage(ConfigReader.getSubsetNeighbors().get(i));//need to fix it
//			}
//			}if(ConfigReader.getMaxPerActive()>ConfigReader.getSubsetNeighbors().size() && myHost.getMe().active){
//				for (int i=0;i<ConfigReader.getMaxPerActive();i++){
//					try {
//						Thread.sleep(ConfigReader.getMinSendDelay());
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						
//					}
//					sendREBMessage(ConfigReader.getSubsetNeighbors().get(i));//need to fix it
//				}
//			}

		}
		else {
			while(true){
			Message currentMessage=null;
			try {
				currentMessage=messageQueue.take();
				Logger.log(Process.myHost,"Received message from----> "+ currentMessage.getPID());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
			}if(myHost.getMe().active==false ){
				
			}  
			}
			

		}

	}
}
