package chat_lib_pack;

public interface ReceiverListener {

	void stateChanged();

	void messageReceived(byte[] rcvData, String host, int port);
}
