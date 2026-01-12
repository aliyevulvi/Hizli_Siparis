package com.hizlisiparis;

import com.logger.*;
import java.net.*;

public class Server {
    
    
    public static void main(String[] args) {
        startServer();
    }
    
    public static void startServer() {
        try {
            ServerSocket server = new ServerSocket(5000);
            
        } catch (Exception e) {
            Log.error("Server", e.toString(), e.getMessage());
        }
    }
}