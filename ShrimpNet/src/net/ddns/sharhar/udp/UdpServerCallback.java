package net.ddns.sharhar.udp;

public interface UdpServerCallback {
	public void receivedData(int id, byte[] data);
}
