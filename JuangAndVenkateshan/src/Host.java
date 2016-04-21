import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Host {

	private String myname;
	public ArrayList<Integer> neighborList = new ArrayList<Integer>();
	public Node myNode;
	public int numberOfNeighborMemebr;
	public HashMap<Integer,Node> neighbor = new HashMap<Integer,Node>();
	public HashMap<String,Integer> hostMap = new HashMap<String,Integer>();
	//public void readFile(String filename) throws Exception{}
	private int checkPoint;
	private HashMap<Integer,Integer> nodeCheck = new HashMap<Integer,Integer>();
	
    public Host(int pid,Node node)throws Exception{
    	myname = InetAddress.getLocalHost().getHostName();	
    	myNode = node;
    }
    
    public String whoami(){
    	return myname;
    }
    public void addNeighborMember(int pid, Node node){
    	neighbor.put(pid, node);
    	neighborList.add(pid);
    	numberOfNeighborMemebr++;
    }
    
    public void nodeCheckpoint(int failId,int checkpoint){
    	nodeCheck.put(failId, checkpoint);
    }
    
    public Node getMe(){
    	return myNode;
    }

}

class Node{
	boolean active =true; //marking all the Node as active initially
	private String hostname;
	private int port;
	private int pid;
	public String getHostName(){
		return hostname;
	}
	public int getPort(){
		return port;
	}
	public int getPID(){
		return pid;
	}
	public Node(String h, int p, int pid){
		hostname = h;
		port = p;
		this.pid = pid;
	}
	
}
