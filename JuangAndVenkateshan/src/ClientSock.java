import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSock implements Runnable {
	public static TCPClient myClient;
	TCPServer myServer;
	public Socket socket;
	public Node neighborMember=null;
	
	public ClientSock(Node n){
		  neighborMember = n;
	}
	// BufferedReader reader;
		public static Host myHost;
		public  boolean hostPing = false;
		PrintWriter writer = null;
		BufferedReader reader = null;
		public static HashMap<Integer,Boolean> whoMessaged = new HashMap<Integer,Boolean>();
//		public static ArrayList<Node> randomShandom = new ArrayList<Node>();
		
		boolean isConnectionEstablished(){
			return hostPing;
		}
		public static LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
		public static HashMap<Integer,PrintWriter> writerMap = new HashMap<Integer,PrintWriter>();
		
//		public static ArrayList randomNeighbor(){
//			Random randomGenerator = new Random();
//			int randomInt=randomGenerator.nextInt(myHost.neighborList.size());
//			for (int i=0;i<randomInt;i++){
//				randomShandom.add(myHost.neighbor.get(i));
//			}
//			return randomShandom;
//		}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		hostPing = false;
		while(!hostPing){
			try {
				socket = new Socket(neighborMember.getHostName()+ ".utdallas.edu",neighborMember.getPort());
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				Process.writersMap.put(neighborMember.getPID(), writer);
				hostPing = true;
				return;
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//Logger.log(Process.myHost,"UnknownHostException");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//Logger.log(Process.myHost,"IOException");
			}
			
		}
		
		Logger.log(Process.myHost,"Established socket with " + neighborMember.getPID());
		if (Process.myHost.getMe().active){
			
			try {
				writerMap.put(neighborMember.getPID(),new PrintWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
