package net.ddns.sharhar.tcp;

public interface TcpServerCallback {
	public void receivedData(int id, String data);
	public void connected(int id);
}
