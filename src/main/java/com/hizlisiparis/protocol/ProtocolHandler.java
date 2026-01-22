package com.hizlisiparis.protocol;

import java.io.*;
import com.hizlisiparis.logger.Log;

public class ProtocolHandler {
    public static void sendPacket(DataOutputStream dos, Packet packet) {
        try {
            dos.writeInt(packet.getJsonStr().length());
            //dos.write(packet.getJsonStr().getBytes());
            dos.writeUTF(packet.getJsonStr());
            dos.flush();
        } catch (Exception e) {
            Log.error("ProtocolHandler", e.toString(), e.getMessage());
        }
    }
    
    public static Packet receivePacket(DataInputStream dis) {
        try {
            int packetLength = dis.readInt();
            byte[] packetBytes = new byte[packetLength];
            //dis.readFully(packetBytes);
            String packetStr = dis.readUTF();
            
            return Packet.getPacket(packetStr.getBytes());
            
        } catch (Exception e) {
           Log.error("ProtocolHandler", e.toString(), e.getMessage());
           return null;
        }
    }
}