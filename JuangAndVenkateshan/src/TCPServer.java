import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.Messaging.SyncScopeHelper;

public class TCPServer implements Runnable {
	public TCPClient myClient;
	public Host myHost;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//System.out.println(myHost.getMe().getPort());
			@SuppressWarnings("resource")

			ServerSocket serverSock = new ServerSocket(myHost.getMe().getPort());
			ServerSock listenMessageQueue = new ServerSock(null,this,myHost);
			listenMessageQueue.setRole("MESSAGEQUEUEWATCHER");
			Thread listenMssageQueueThread = new Thread(listenMessageQueue);
			listenMssageQueueThread.start();

			while(true){
				Socket sock = serverSock.accept();
				ServerSock currentSock = new ServerSock(sock,this,myHost);
				currentSock.setRole("LISTENONPORT");
				Thread t=new Thread(currentSock);
				t.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}

	}
	public void setData(Host h){
		myHost = h;
	}
}
