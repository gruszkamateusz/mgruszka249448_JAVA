package chat_lib_pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main implements ConnectionListener {
	public static void main(String[] args) {		
		Main m = new Main();
		if(args.length < 2) {
			System.out.println("Give 2 ports as arguments.");
			return;
		}
		int portSend = Integer.parseInt(args[0]);
		int portReceive = Integer.parseInt(args[1]);
		SecureConnection secureConn = new SecureConnection(portReceive);
		secureConn.addListener(m);
		secureConn.start();
		try {
			secureConn.connect("127.0.0.1", portSend);
		} catch (IOException e) {
			secureConn.disconnect();
			e.printStackTrace();
		}
		
		System.out.println("Send x to exit");
		while(true) {
			BufferedReader reader =
	                   new BufferedReader(new InputStreamReader(System.in));
			try {
		        String data = reader.readLine();
		        if(data.charAt(0) == 'x')
		        	break;
		        secureConn.send(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		secureConn.stop();
		System.out.println("Bye!");
	}

	@Override
	public void messageReceived(byte[] rcvData, String host, int port) {
		StringBuffer sb = new StringBuffer("Received: ");
		sb.append(new String(rcvData));
		sb.append("\n");
		System.out.print(sb.toString());	
	}

	@Override
	public void stateChanged() {
	}

}
