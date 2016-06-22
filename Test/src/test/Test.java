package test;

import net.ddns.sharhar.udp.UdpClient;
import net.ddns.sharhar.udp.UdpClientCallback;
import net.ddns.sharhar.udp.UdpServer;
import net.ddns.sharhar.udp.UdpServerCallback;

public class Test implements UdpServerCallback, UdpClientCallback{
	public static void main(String[] args) {
		new Test();
	}
	
	UdpServer server;
	UdpClient client;
	
	public Test(){
		server = new UdpServer(5000, this);
		server.start();
		
		client = new UdpClient("localhost", 5000, this);
		client.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.sendData(("Ping").getBytes());
	}

	public void recivedData(int id, byte[] data) {
		System.out.println("Client " + id + " sent: " + new String(data).trim());
		server.clients.get(id).sendData(("Pong").getBytes());
	}
	
	public void recivedData(byte[] data) {
		System.out.println("Server sent: " + new String(data).trim());
	}
}
