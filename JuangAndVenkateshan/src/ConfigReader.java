import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class ConfigReader {
	public static Host myHost  = null;
	private static int numberOfNodes;
	private static int numberOfFailure;
	private static int maxNumber;
	private static int maxPerActive;
	private static int minSendDelay;
	private static int me;

	public static ArrayList<Node> subsetNeighbors = null;

	public static void setMe(int pid){
		me = pid;
	}

	public static int getNumberOfNodes() {
		return numberOfNodes;
	}

	public static void setNumberOfNodes(int numberOfNodes) {
		ConfigReader.numberOfNodes = numberOfNodes;
	}

	public static int getMinSendDelay(){
		return minSendDelay;
	}

	public static int getNumberOfFailure() {
		return numberOfFailure;
	}

	public static void setNumberOfFailure(int numberOfFailure) {
		ConfigReader.numberOfFailure = numberOfFailure;
	}

	public static int getMaxNumber() {
		return maxNumber;
	}

	public static void setMaxNumber(int maxNumber) {
		ConfigReader.maxNumber = maxNumber;
	}

	public static int getMaxPerActive() {
		return maxPerActive;
	}

	public static int getMe(){
		return me;
	}
	public static void setMaxPerActive(int maxPerActive) {
		ConfigReader.maxPerActive = maxPerActive;
	}

	public static HashSet<Node> getSubsetNeighbors(){
	
			HashSet<Node> set = new HashSet<Node>();
			
			Random randomGenerator = new Random();
			int randomInt = 0;
			//Logger.log(Process.myHost,"Value of the neighborList"+ Process.myHost.neighborList.size());
			while(randomInt == 0){
				randomInt = randomGenerator.nextInt(Process.myHost.neighborList.size());
					
			}
			//Logger.log(Process.myHost,"neighbor size : " + Process.myHost.neighborList.size() + " Random Int : " + randomInt);
	       
			while(set.size() < randomInt){	
				int randomSelector = randomGenerator.nextInt(Process.myHost.neighborList.size()-1);
				set.add(Process.myHost.neighbor.get(Process.myHost.neighborList.get(randomSelector)));
			}
		return set;
	}

	//reading the configuration File
	@SuppressWarnings({ "resource", "resource", "resource", "resource" })
	public static Host readFile(String filename) throws Exception{
		FileReader reader = new FileReader(filename);
		String line = null;
		BufferedReader file = new BufferedReader(reader);
		boolean paramsParsed = false;
		int hostsCount = 0;
		int currentHost = 0;
		int neighbourCount = 0;
		int failEvents = 0;
		HashMap<Integer,Node> hosts = new HashMap<Integer,Node>();
		HashMap<String,Integer> hostMap = new HashMap<String,Integer>();
		while ((line=file.readLine()) != null){
			if (line.trim().length()==0||(line.charAt(0)=='#')){
				continue;
			}
			Scanner sc = new Scanner(line);
			//read the first line of the configuration
			if(paramsParsed==false){
				numberOfNodes=sc.nextInt();
				numberOfFailure=sc.nextInt();
				maxNumber=sc.nextInt();
				maxPerActive=sc.nextInt();
				minSendDelay=sc.nextInt();
				paramsParsed=true;
				continue;
			}
			if(hostsCount < numberOfNodes){
				int pid = sc.nextInt();
				String hostname = sc.next();
				hosts.put(new Integer(pid), new Node(hostname,sc.nextInt(),pid));
				hostMap.put(hostname, pid);
				hostsCount++;
				continue;
			}
			if(myHost == null){
				myHost = new Host(me,hosts.get(me));
				myHost.hostMap=hostMap;
			}
			if(hostsCount==numberOfNodes && currentHost<numberOfNodes){
				if(currentHost==me){
					while( sc.hasNext()){
						int currentPID = sc.nextInt();
						myHost.addNeighborMember(currentPID,hosts.get(currentPID));

					}



				}
				currentHost++;
			}
			else{
				while(sc.hasNext()){
					int failId = sc.nextInt();
					int checkpoint = sc.nextInt();
					FailureEvent f = new FailureEvent(failId,checkpoint,failEvents - 1);
					if(failId == myHost.getMe().getPID()){
						myHost.myFailEventList.add(f);
					}
					myHost.failureEvents.put(failEvents++, f);
					myHost.nodeCheckpoint(failId, checkpoint);

				}
			}



		}

		myHost.numFailEvents = failEvents;
		file.close();
		return myHost;
	}
}

