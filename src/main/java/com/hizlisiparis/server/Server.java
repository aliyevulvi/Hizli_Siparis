package com.hizlisiparis.server;

import com.hizlisiparis.logger.*;
import java.net.*;
import java.time.LocalTime;
import java.util.*;

public class Server {
	private static LocalTime closeTime = LocalTime.now().plusSeconds(30);
	private static List<ClientService> connectedUsers = Collections.synchronizedList(new ArrayList<>());

	public static void main(String[] args) {
		startServer();
	}

	public static void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(5001);
			serverSocket.setSoTimeout(1000);
			serverSocket.setReuseAddress(true);
			Log.info("Server", "Server Opened");

			while (connectedUsers.size() > 0 || LocalTime.now().isBefore(closeTime)) {
				if (closeTime.isBefore(LocalTime.now())) {
					closeTime = closeTime.plusSeconds(30);
				}
				try {
					Socket clientSocket = serverSocket.accept();
					ClientService cs = new ClientService(clientSocket);
					cs.start();

				} catch (SocketTimeoutException ste) {
					serverInfo();
				}
			}

			serverSocket.close();
			Log.info("Server", "Server Closed");

		} catch (Exception e) {
			Log.error("Server", e.toString(), e.getMessage());
		}
	}

	public static void serverInfo() {
		LocalTime now = LocalTime.now();
		String currentTimeStr = "" + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
		String closeTimeStr = "" + closeTime.getHour() + ":" + closeTime.getMinute() + ":" + closeTime.getSecond();
		String status = connectedUsers.size() > 0 ? "Busy" : "Idle" ;

		System.out.printf("[ %-4s%-12s%4s ]%n", "---", "SERVER  INFO", "---");
		System.out.printf("[ %-11s%-1s%8s ]%n", "Status ", ":", status);
		System.out.printf("[ %-11s%-1s%8s ]%n", "Time ", ":", currentTimeStr);
		System.out.printf("[ %-11s%-1s%8s ]%n", "Close Time ", ":", closeTimeStr);
		System.out.printf("[ %-10s%10s ]%n%n", "----", "----");

	}

	public static void exitClient(ClientService cs) {
		try {
			connectedUsers.removeIf(c -> c == cs);
		} catch (Exception e) {
			Log.error("Server", e.toString(), e.getMessage());
		}
	}
}