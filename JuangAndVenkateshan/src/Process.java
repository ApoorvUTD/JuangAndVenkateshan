
public class Process {
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 ConfigReader.setMe(Integer.parseInt(args[1]));
		 Host h = ConfigReader.readFile(args[0]);
		 TCPServer server = new TCPServer();
		 Thread serverThread = new Thread(server);
		 serverThread.start();

	}

}
