package chat_lib_pack;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Receiver {
	private List<ReceiverListener> listeners = new ArrayList<ReceiverListener>();
	private Thread thread = null;
	private int port = 0;
	private ServerSocket srvSocket = null;
	private boolean end = false;

	private boolean running = false;

	public Receiver(int port) {
		this.port = port;
		end = false;
		running = false;
	}

	public void start() {
		end = false;
		if (running == true)
			return;
		running = true;
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					srvSocket = new ServerSocket(port);
					while (end == false) {
						Socket inSocket = srvSocket.accept();
						InputStream is = inSocket.getInputStream();
						byte[] rcvData = is.readAllBytes();
						InetSocketAddress sockaddr = (InetSocketAddress)inSocket.getRemoteSocketAddress();
						listeners.forEach(
								(item) -> item.messageReceived(rcvData,
										sockaddr.getAddress().getHostAddress(),
										sockaddr.getPort()));
						inSocket.close();
						Thread.sleep(100);
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				running = false;
			}
		});
		thread.start();
	}

	public void stop() {
		end = true;
		thread.interrupt();
		try {
			if(srvSocket != null)
				srvSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isListening() {
		return running;
	}
	
	public void addListener(ReceiverListener lis) {
		listeners.add(lis);
	}

	public void removeListener(ReceiverListener lis) {
		listeners.remove(lis);
	}
}
