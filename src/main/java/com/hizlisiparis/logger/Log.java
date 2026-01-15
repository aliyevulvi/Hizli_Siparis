package com.hizlisiparis.logger;

import java.io.*;
import java.time.*;


public class Log {
    
    public static void error(String className, String exc, String message) {
        try {
            File logFile = new File("Logs/"+className+"_errors.txt");
            
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            
            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            LocalTime now = LocalTime.now();
            
            String timestamp = "Time " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond() + ":" + (now.getNano()/1000);

            bw.newLine();
            bw.write(timestamp);
            bw.newLine();
            bw.write(exc);
            bw.newLine();
            bw.write(message);
            bw.newLine();
            
            bw.close();
            
        } catch (Exception e) {
            error("Log", e.toString(), e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void info(String className, String info) {
        try {
            File logFile = new File("Logs/" + className + "_info.txt");
            
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            
            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            LocalTime now = LocalTime.now();
            String timestamp = "Time " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
            info = "[ " + info + " ]";
            
            bw.newLine();
            bw.write(timestamp);
            bw.newLine();
            bw.write(info);
            bw.newLine();
            
            bw.close();
        
        } catch (Exception e) {
            error("Log", e.toString(), e.getMessage());
        }
    }
}