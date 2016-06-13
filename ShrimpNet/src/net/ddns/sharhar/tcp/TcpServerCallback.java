package net.ddns.sharhar.tcp;

public interface TcpServerCallback {
	public void recivedData(int id, String data);
	public void connected(int id);
}
