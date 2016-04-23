import java.util.PriorityQueue;
import java.io.*;
import java.util.*;
public class Process {
	class ServerState{
		 public PriorityQueue<Message> waitingQueue = new PriorityQueue<Message>(1000,new MessageComparator());
	};
	public static Host myHost;
	public static Node getNodeAtIndex(int index){
		return Process.myHost.neighbor.get(Process.myHost.neighborList.get(index));
	}
	public static int getPIDAtIndex(int index){
		return Process.myHost.neighborList.get(index);
	}
	public static HashMap<Integer,PrintWriter> writersMap = new HashMap<Integer,PrintWriter>();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 ConfigReader.setMe(Integer.parseInt(args[1]));
		 Host h = ConfigReader.readFile(args[0]);
		 
		 Process.myHost = h;
		 TCPServer server = new TCPServer();
		// server.myHost = h;
//		 client.setData(server,h);
		 server.setData( h);
		 Thread serverThread = new Thread(server);
		 TCPClient client = new TCPClient();
		 Thread clientThread = new Thread(client);
		 serverThread.start();
		 clientThread.start();
		 

	}

}
