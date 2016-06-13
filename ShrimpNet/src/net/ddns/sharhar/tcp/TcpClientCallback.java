package net.ddns.sharhar.tcp;

public interface TcpClientCallback {
	public void connected();
	public void recivedData(String data);
}
