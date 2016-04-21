import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ConfigReader {
	public static Host myHost;
	private static int numberOfNodes;
	private static int numberOfFailure;
	private static int maxNumber;
	private static int maxPerActive;
	private static int me;
	public static ArrayList<Node> randomShandom = new ArrayList<Node>();


	public static void setMe(int pid){
		me = pid;
	}

	public static int getNumberOfNodes() {
		return numberOfNodes;
	}

	public static void setNumberOfNodes(int numberOfNodes) {
		ConfigReader.numberOfNodes = numberOfNodes;
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

	public static void setMaxPerActive(int maxPerActive) {
		ConfigReader.maxPerActive = maxPerActive;
	}
	
	public static ArrayList randomNeighbor(){
		Random randomGenerator = new Random();
		int randomInt=randomGenerator.nextInt(myHost.neighborList.size());
		for (int i=0;i<randomInt;i++){
			randomShandom.add(myHost.neighbor.get(i));
		}
		return randomShandom;
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
		HashMap<Integer,Node> hosts = new HashMap<Integer,Node>();
		HashMap<String,Integer> hostMap = new HashMap<String,Integer>();
		while (line==file.readLine()){
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
			Host rhost = new Host(me,hosts.get(me));
			rhost.hostMap=hostMap;
			if(hostsCount==numberOfNodes && currentHost<numberOfNodes){
				if(currentHost==me){
					while( sc.hasNext()){
						int currentPID = sc.nextInt();
						rhost.addNeighborMember(currentPID,hosts.get(currentPID));

					}




					file.close();
					return rhost;
				}
				currentHost++;
			}
			while(sc.hasNext()){
				int failId = sc.nextInt();
				int checkpoint = sc.nextInt();
				rhost.nodeCheckpoint(failId, checkpoint);
			}



		}
		return null;
	}
}

