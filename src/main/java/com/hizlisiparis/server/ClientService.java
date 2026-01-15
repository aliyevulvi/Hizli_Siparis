package com.hizlisiparis.server;

import com.hizlisiparis.logger.*;
import java.net.*;
import java.io.*;
import com.hizlisiparis.protocol.*;
import com.hizlisiparis.database.*;

public class ClientService extends Thread {
	private Socket socket;
	private DataOutputStream dataWriter;
	private DataInputStream dataReader;
	private boolean running = true;

	public ClientService(Socket socket) {
		this.socket = socket;

		try {
			dataWriter = new DataOutputStream(this.socket.getOutputStream());
			dataReader = new DataInputStream(this.socket.getInputStream());
		} catch (Exception e) {
			Log.error("ClientService", e.toString(), e.getMessage());
		}
	}

	public void run() {
		try {
			while (running) {
				Packet packet = ProtocolHandler.receivePacket(dataReader);
				packetParser(packet);
			}
		} catch (Exception e) {
			Log.error("ClientService", e.toString(), e.getMessage());
		}
	}

	public void packetParser(Packet packet) {
		try {
			switch (packet.getType()) {
			case "LOGIN":
				clientLogin(packet);
				break;
			case "EXIT":
				exitClient();
				break;
			}
		} catch (Exception e) {
			Log.error("ClientService", e.toString(), e.getMessage());
		}
	}

	private void clientLogin(Packet packet) {
		try {
			if (!DB.isClientExists(packet.getPayload().get(0))) {
                ProtocolHandler.sendPacket(dataWriter, new Packet("Error", "1.0", "User_Not_Found"));
			} else {
			    ProtocolHandler.sendPacket(dataWriter, new Packet("OK", "1.0", "User_Logged_In"));
			}
			
		} catch (Exception e) {
			Log.error("ClientService", e.toString(), e.getMessage());
		}
	}

	public void exitClient() {
		try {
			Server.exitClient(this);
			socket.close();
			running = false;
		} catch (Exception e) {
			Log.error("ClientService", e.toString(), e.getMessage());
		}
	}
}