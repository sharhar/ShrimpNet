package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.ddns.sharhar.udp.UdpServer;
import net.ddns.sharhar.udp.UdpServerCallback;

public class Test implements UdpServerCallback{
	public static void main(String[] args) {
		new Test();
	}
	
	UdpServer server;
	
	public Test(){
		server = new UdpServer(5000, this);
		server.start();
		
		InetAddress address = null;
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		
		try {
			address = InetAddress.getLocalHost();
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		packet = new DatagramPacket(("hello").getBytes(), 5, address, 5000);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		socket.close();
		
		try {
			address = InetAddress.getLocalHost();
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		packet = new DatagramPacket(("hello").getBytes(), 5, address, 5000);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		socket.close();
		
		try {
			address = InetAddress.getLocalHost();
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		
		packet = new DatagramPacket(("hello").getBytes(), 5, address, 5000);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		socket.close();
	}

	public void recivedData(int id, byte[] data) {
		System.out.println("Client " + id + " sent: " + new String(data).trim());
	}
}
