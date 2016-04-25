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
	public int numFailEvents;
	public int eventID;
	public  ArrayList<FailureEvent> myFailEventList = new ArrayList<FailureEvent>();
	public static HashMap<Integer,FailureEvent> failureEvents = new HashMap<Integer,FailureEvent>();
	public HashMap<Integer,Integer> nodeCheck = new HashMap<Integer,Integer>();
	public HashMap<Integer,Boolean> hasHappened = new HashMap<Integer,Boolean>();

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

	@Override
	public String toString() {
		return "Host [myname=" + myname + ", neighborList=" + neighborList + ", myNode=" + myNode
				+ ", numberOfNeighborMemebr=" + numberOfNeighborMemebr + ", neighbor=" + neighbor + ", hostMap="
				+ hostMap + ", checkPoint=" + checkPoint + ", nodeCheck=" + nodeCheck + ", whoami()=" + whoami()
				+ ", getMe()=" + getMe() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
    
    

}

class Node{
	boolean active = true; //marking all the Node as active initially
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
//        if(pid % 2 == 0){
//        	active = true;
//        }
//        else{
//        	active = false;
//        }
	}
	
}
