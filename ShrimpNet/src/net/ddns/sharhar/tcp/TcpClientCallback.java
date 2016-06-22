package net.ddns.sharhar.tcp;

public interface TcpClientCallback {
	public void connected();
	public void receivedData(String data);
}
