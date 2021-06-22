package chat_lib_pack;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {
	public void send(byte[] data, String host, int port)
			throws UnknownHostException, IOException {
		Socket socket;
		socket = new Socket(host, port);
		OutputStream out = socket.getOutputStream();
		out.write(data);
		socket.close();
	}
}
