import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	public static LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message> ();
	//public static PriorityQueue<Message> waitingQueue = new PriorityQueue<Message>(ConfigReader.getNumberOfNodes()*Config.getNumberOfRequests(), new MessageComparator());
	public static Map<Integer, PrintWriter> mapNodeWriter = new HashMap<Integer,PrintWriter>();


	
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
		
	}
	 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(true){//some condition need to go here man
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mapNodeWriter.put(incomingPID, writer);
			while(true){

				try {
					message = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String []tokens = message.split("[~]");
				Clock.updateClock(Integer.parseInt(tokens[2]));
				Message incomingMessage  = new Message(Integer.parseInt(tokens[2]),Integer.parseInt(tokens[1]),tokens[0]);
					messageQueue.add(incomingMessage);
			
				
			}
		}
		
	}

}
