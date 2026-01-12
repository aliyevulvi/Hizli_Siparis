package com.hizlisiparis;

import com.logger.*;
import java.net.*;

public class ClientService extends Threads {
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
            Log.error("ClientService", e.toString(),e.getMessage());
        }
    }
    
    public void run() {
        try {
            while (running) {
                String packet = ProtocolHandler.receivePacket(dataReader);
                packetParser(packet);
            }
        } catch (Exception e) {
            Log.error("ClientService", e.toString(), e.getMessage());
        }
    }
    
    public void packetParser(String packet) {
        try {
            
        } catch (Exception e) {
            Log.error("ClientService", e.toString(), e.getMessage());
        }
    }
}