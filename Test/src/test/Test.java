package test;

import net.ddns.sharhar.tcp.TcpClient;
import net.ddns.sharhar.tcp.TcpClientCallback;
import net.ddns.sharhar.tcp.TcpServer;
import net.ddns.sharhar.tcp.TcpServerCallback;

public class Test implements TcpServerCallback, TcpClientCallback{
	public static void main(String[] args) {
		new Test();
	}
	
	TcpServer server;
	TcpClient client;
	
	public Test() {
		server = new TcpServer(5000, this);
		client = new TcpClient("localhost", 5000, this);
		server.start();
		client.start();
	}
	
	public void recivedData(int id, String data) {
		System.out.println("SERVER>> Client " + id + " sent: " + data);
	}
	
	public void connected(int id) {
		System.out.println("SERVER>> new client " + id);
		server.clients.get(id).sendData("PING");
	}
	
	public void connected() {
		System.out.println("CLIENT>> connected to server!");
	}
	
	public void recivedData(String data) {
		System.out.println("CLIENT>> recived: " + data);
		client.sendData("PONG");
	}
}
