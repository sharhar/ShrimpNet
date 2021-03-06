package net.ddns.sharhar.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpClient implements Runnable{
	DatagramSocket socket;
	Thread thread;
	boolean running = false;
	UdpClientCallback callback;
	InetAddress address;
	public int packetSize;
	int port;
	
	public UdpClient(String address, int port, UdpClientCallback callback) {
		this(address, port, callback, 1024);
	}
	
	public UdpClient(String address, int port, UdpClientCallback callback, int packetSize) {
		this.callback = callback;
		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		thread = new Thread(this);
		
		this.packetSize = packetSize;
	}
	
	public void start() {
		running = true;
		thread.start();
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			socket = new DatagramSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(running) {
			try {
				byte[] data = new byte[packetSize];
				DatagramPacket packet = new DatagramPacket(data, packetSize);
				socket.receive(packet);
				new Thread(() -> {
					callback.receivedData(packet.getData());
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
