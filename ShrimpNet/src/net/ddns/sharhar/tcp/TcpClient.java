package net.ddns.sharhar.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient implements Runnable{
	Socket socket;
	Thread thread;
	boolean running = false;
	PrintWriter out;
	BufferedReader in;
	TcpClientCallback callback;
	public String address;
	public int port;
	
	public TcpClient(String address, int port, TcpClientCallback callback) {
		this.callback = callback;
		this.address = address;
		this.port = port;
		thread = new Thread(this);
	}
	
	public void start() {
		running = true;
		thread.start();
	}
	
	public void sendData(String data) {
		out.println(data);
	}
	
	public void run() {
		try {
			socket = new Socket(address, port);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new
		            InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		callback.connected();
		
		while(running) {
			try {
				String input = in.readLine();
				callback.receivedData(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
