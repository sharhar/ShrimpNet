package net.ddns.sharhar.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UdpServer implements Runnable{
	
	public class UdpServerClient{
		DatagramSocket socket;
		InetAddress address;
		int port;
		int num;
		boolean running = false;
		UdpServer server;
		
		public UdpServerClient(UdpServer server, InetAddress address, int port, int num) {
			this.address = address;
			this.port = port;
			this.num = num;
			this.server = server;
		}
		
		public void recivedData(byte[] data) {
			new Thread(() -> {
				server.callback.recivedData(num, data);
			}).start();
		}
		
		public void sendData(byte[] data) {
			if(data.length > server.packetSize) {
				System.err.println("Message longer than server max: " + data.length + " bytes");
			}
			
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	volatile public Map<Integer, UdpServerClient> clients = new HashMap<Integer, UdpServerClient>();
	volatile public Map<String, Integer> clientIDs = new HashMap<String, Integer>();
	public UdpServerCallback callback;
	Thread thread;
	int port;
	boolean running = false;
	DatagramSocket socket;
	public int packetSize;
	volatile int clientNum = 0;
	
	public UdpServer(int port, UdpServerCallback callback) {
		this(port, callback, 1024);
	}
	
	public UdpServer(int port, UdpServerCallback callback, int packetSize) {
		this.port = port;
		this.thread = new Thread(this);
		this.packetSize = packetSize;
		this.callback = callback;
	}
	
	public void start() {
		running = true;
		thread.start();
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized int getId() {
		clientNum++;
		return clientNum-1;
	}
	
	public void proccesMessage(DatagramPacket packet) {
		new Thread(() -> {
			InetAddress address = packet.getAddress();
			String rawAddress = address.getHostAddress() + ":" + packet.getPort();
			UdpServerClient client = clients.get(clientIDs.get(rawAddress));
			if(client != null) {
				client.recivedData(packet.getData());
			} else {
				int idTemp = getId();
				UdpServerClient temp = new UdpServerClient(this, address, packet.getPort(), idTemp);
				clients.put(idTemp, temp);
				clientIDs.put(rawAddress, idTemp);
				temp.recivedData(packet.getData());
			}
		}).start();
	}
	
	public void run() {
		try {
			socket = new DatagramSocket(port);
		} catch(BindException e) {
			System.err.println("Socket already boud!");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (running) {
			try {
				byte[] data = new byte[packetSize];
				DatagramPacket packet = new DatagramPacket(data, packetSize);
				socket.receive(packet);
				proccesMessage(packet);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
