package chat_lib_pack;

public interface ConnectionListener {

	void stateChanged();
	

	void messageReceived(byte[] rcvData, String host, int port);
}
