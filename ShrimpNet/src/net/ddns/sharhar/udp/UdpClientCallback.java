package net.ddns.sharhar.udp;

public interface UdpClientCallback {
	public void receivedData(byte[] data);
}
