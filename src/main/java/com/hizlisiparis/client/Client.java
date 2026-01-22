package com.hizlisiparis.client;

import com.hizlisiparis.logger.Log;
import com.hizlisiparis.protocol.*;
import com.hizlisiparis.product.*;
import com.google.gson.Gson;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	private static DataOutputStream dataWriter;
	private static DataInputStream dataReader;
	private static BufferedReader console;
	private static boolean loginSuccessed = false;
	private static ArrayList<Product> menu = new ArrayList<>();

	public static void main(String[] args) {
		startProgram();
	}

	public static void startProgram() {
		try {
			Socket serverSocket = new Socket("localhost", 5001);
			Log.info("Client", "-----------------------------------");
			Log.info("Client", "Client Connected to Server");
			dataWriter = new DataOutputStream(serverSocket.getOutputStream());
			dataReader = new DataInputStream(serverSocket.getInputStream());

			//int trial = 0;
//			while (trial < 3) {
//				if (clientLogin()) {
//					loginSuccessed = true;
//					break;
//				} else {
//					trial++;
//				}
//			}
            loginSuccessed = true;
			if (loginSuccessed) {
				getMenu();
			}

			Packet exitPacket = new Packet("EXIT", "1.0");
			ProtocolHandler.sendPacket(dataWriter, exitPacket);

			serverSocket.close();
			System.out.println("[ App Closing ]");
			Log.info("Client", "Client Disconnected from Server");
			Log.info("Client", "-----------------------------------");
			

		} catch (Exception e) {
			Log.error("Client", e.toString(), e.getMessage());
		}
	}

	public static boolean clientLogin() {
		try {
			console = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("[ Enter Pin ] : ");
			String pin = console.readLine();

			Packet loginPacket = new Packet("LOGIN", "1.0", pin);
			ProtocolHandler.sendPacket(dataWriter, loginPacket);

			Packet response = ProtocolHandler.receivePacket(dataReader);

			if (response.getType().equals("ERROR")) {
				System.out.println(response.getPayload().get(0));
				return false;
			} else if (response.getType().equals("OK")) {
				System.out.println(response.getPayload().get(0));
				return true;
			} else {
				System.out.println("[ Something Unknown Happened ]");
				System.out.println(response.getPayload().get(0));
				return false;
			}



		} catch (Exception e) {
			Log.error("Client", e.toString(), e.getMessage());
			return false;
		}
	}

	public static void getMenu() {
		try {
			File menuFile = new File("clientdata/menu.json");

			if (!menuFile.exists()) {
				ProtocolHandler.sendPacket(dataWriter, new Packet("GET_MENU", "1.0"));
                Log.info("Client", "Get Menu Request Sent");
				//String productJson = ProtocolHandler.receivePacket(dataReader).getPayload().get(0);
				Packet menu_res = ProtocolHandler.receivePacket(dataReader);
				Log.info("Client", "One Product Received");
				menuFile.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(menuFile, true));
                Gson gson = new Gson();
				while (!menu_res.getType().equals("DONE")) {
					Product product = gson.fromJson(menu_res.getPayload().get(0), Product.class);
					menu.add(product);
					bw.write(menu_res.getPayload().get(0));
					bw.newLine();
					menu_res = ProtocolHandler.receivePacket(dataReader);
				    Log.info("Client", "One Product Received");
				}
                Log.info("Client", "All Products Received");
				bw.close();
				
				ProtocolHandler.sendPacket(dataWriter, new Packet("MENU_VERSION", "1.0"));
				
				File lastMod = new File("clientdata/lastModification.txt");
				lastMod.createNewFile();
				
				bw = new BufferedWriter(new FileWriter(lastMod));
				bw.write(ProtocolHandler.receivePacket(dataReader).getPayload().get(0));
				bw.close();


			} else {
				File lastMod = new File("clientdata/lastModification.txt");
				BufferedReader br = new BufferedReader(new FileReader(lastMod));
				String lastModification = br.readLine();
				br.close();
				ProtocolHandler.sendPacket(dataWriter, new Packet("MENU_VERSION", "1.0"));
				String serverLastMod = ProtocolHandler.receivePacket(dataReader).getPayload().get(0);

				if (!lastModification.equals(serverLastMod)) {
					menuFile.delete();
					menuFile.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(menuFile, true));

					ProtocolHandler.sendPacket(dataWriter, new Packet("GET_MENU", "1.0"));
					Packet productPacket = ProtocolHandler.receivePacket(dataReader);
					Gson gson = new Gson();
					while (!productPacket.getType().equals("DONE")) {
						Product product = gson.fromJson(productPacket.getPayload().get(0), Product.class);
						menu.add(product);
						bw.write(productPacket.getPayload().get(0));
						bw.newLine();
						productPacket = ProtocolHandler.receivePacket(dataReader);
					}

					bw.close();
					
					bw = new BufferedWriter(new FileWriter(lastMod));
					bw.write(serverLastMod);
					bw.close();
				}
			}

		} catch (Exception e) {
			Log.error("Client", e.toString(), e.getMessage());
		}
	}

	public String getPin() {
		return waiterPin;
	}
}