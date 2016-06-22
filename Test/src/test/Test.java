package test;

import net.ddns.sharhar.tcp.TcpClient;
import net.ddns.sharhar.tcp.TcpClientCallback;
import net.ddns.sharhar.tcp.TcpServer;
import net.ddns.sharhar.tcp.TcpServerCallback;
import net.ddns.sharhar.udp.UdpClient;
import net.ddns.sharhar.udp.UdpClientCallback;
import net.ddns.sharhar.udp.UdpServer;
import net.ddns.sharhar.udp.UdpServerCallback;

public class Test implements TcpClientCallback, TcpServerCallback, UdpClientCallback, UdpServerCallback{
	public static void main(String[] args) {
		new Test();
	}
	
	TcpClient tcpClient;
	TcpServer tcpServer;
	UdpClient udpClient;
	UdpServer udpServer;
	
	public Test(){
		tcpServer = new TcpServer(5000, this);
		tcpClient = new TcpClient("localhost", 5000, this);
		udpServer = new UdpServer(5001, this);
		udpClient = new UdpClient("localhost", 5001, this);
		
		tcpServer.start();
		udpServer.start();
		
		try {
			Thread.sleep(100);//Give servers time to initialize
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		tcpClient.start();
		udpClient.start();
		
		try {
			Thread.sleep(100);//Give clients time to initialize
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		tcpClient.sendData("Ping");
		udpClient.sendData(("Ping").getBytes());
		
		try {
			Thread.sleep(100);//let everything run
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(0);//stop everything
		
		//function for stopping clients and servers without exiting will come later 
	}
	
	public void receivedData(int id, byte[] data) {//UdpServer received data
		System.out.println("UdpServer>> Client " + id + " sent: " + new String(data).trim());
		udpServer.get(id).sendData(("Pong").getBytes());
	}
	
	public void receivedData(byte[] data) {//UdpClient received data
		System.out.println("UdpClient>> Server sent: " + new String(data).trim());
	}
	
	public void receivedData(int id, String data) {//TcpServer received data
		System.out.println("TcpServer>> Client " + id + " sent: " + data);
		tcpServer.get(id).sendData("Pong");
	}
	
	public void connected(int id) {//TcpServer connected to client
		System.out.println("TcpServer>> Client " + id + " connected!");
	}
	
	public void connected() {//TcpClient connected to server
		System.out.println("TcpClient>> Connected to server!");
	}
	
	public void receivedData(String data) {//TcpClient received data
		System.out.println("TcpClient>> Server sent: " + data);
	}
}
