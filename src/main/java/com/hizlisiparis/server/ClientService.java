package com.hizlisiparis.server;

import com.hizlisiparis.logger.*;
import java.net.*;
import java.io.*;
import java.util.*;
import com.hizlisiparis.protocol.*;
import com.hizlisiparis.database.*;
import com.hizlisiparis.product.*;
import com.google.gson.Gson;

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
		    case "GET_MENU": sendMenu();
		        break;
		    case "MENU_VERSION": sendLastMod();
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
	
	public void sendMenu() {
	    try {
	        ArrayList<Product> products = DB.getProducts();
	        Gson gson = new Gson();
	        for (Product product : products) {
	            ProtocolHandler.sendPacket(dataWriter, new Packet("MENU_RES", "1.0", gson.toJson(product)));
	            Log.info("ClientService", "One Product Sent");
	        }
	        ProtocolHandler.sendPacket(dataWriter, new Packet("DONE", "1.0"));
	        Log.info("ClientService", "All Products Sent");
	    } catch (Exception e) {
	        Log.error("ClientService", e.toString(), e.getMessage());
	    }
	}
	
	public void sendLastMod() {
	    try {
	        ProtocolHandler.sendPacket(dataWriter, new Packet("MENU_LAST_MOD", "1.0", DB.getLastMod()));
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