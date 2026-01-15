package com.hizlisiparis.client;

import com.hizlisiparis.logger.Log;
import com.hizlisiparis.protocol.*;

import java.net.*;
import java.io.*;

public class Client {
    private String waiterName;
    private String waiterLevel;
    private String waiterPin;
    private static DataOutputStream dataWriter;
    private static DataInputStream dataReader;
    private static BufferedReader console;
    
    public static void main(String[] args) {
        startProgram();
    }
    
    public static void startProgram() {
        try {
            Socket serverSocket = new Socket("localhost", 5000);
            Log.info("Client", "Client Connected to Server");
            dataWriter = new DataOutputStream(serverSocket.getOutputStream());
            dataReader = new DataInputStream(serverSocket.getInputStream());
            
            int trial = 0;
            while (trial < 3) {
                if (clientLogin()) {
                    break;
                } else {
                    trial++;
                }
            }
            
            Packet exitPacket = new Packet("EXIT", "1.0");
            ProtocolHandler.sendPacket(dataWriter, exitPacket);
            
            serverSocket.close();
            System.out.println("[ App Closing ]");
            Log.info("Client", "Client Disconnected from Server");
            
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
    
    public String getPin() {
        return waiterPin;
    }
}