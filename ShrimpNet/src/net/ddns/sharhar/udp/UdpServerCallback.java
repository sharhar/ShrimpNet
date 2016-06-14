package net.ddns.sharhar.udp;

public interface UdpServerCallback {
	public void recivedData(int id, byte[] data);
}
