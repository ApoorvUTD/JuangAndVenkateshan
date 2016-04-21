import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {
	public TCPClient myClient;
	public Host myHost;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSock = new ServerSocket(myHost.getMe().getPort());
			while(true){
				Socket sock = serverSock.accept();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
