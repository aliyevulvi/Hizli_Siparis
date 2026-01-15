package com.hizlisiparis.database;

import com.hizlisiparis.logger.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import com.hizlisiparis.client.*;

public class DB {
	private static ArrayList<Client> users = new ArrayList<>();

	public static synchronized boolean isClientExists(String pinNumber) {
		try {
		    if (users.size() == 0) {
		        getClientsFromFile();
		    }
		    
            for (Client cl : users) {
                if (cl.getPin().equals(pinNumber)) {
                    return true;
                }
            }
            
            return false;
		} catch (Exception e) {
			Log.error("DB", e.toString(), e.getMessage());
			return false;
		}
	}

	public static synchronized void getClientsFromFile() {
		try {
			File usersFile = new File("Data/users.json");
			
			if (!usersFile.exists()) {
			    usersFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(usersFile));
			Gson gson = new Gson();
            
            String userJson;
			while ((userJson = br.readLine()) != null) {
				String user = br.readLine();
				users.add(gson.fromJson(user, Client.class));
			}

			br.close();
		} catch (Exception e) {
			Log.error("DB", e.toString(), e.getMessage());
		}
	}
	
	public static synchronized void setClientsToFile() {
	    try {
	        File usersFile = new File("Data/users.json");
	        BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile));
	        Gson gson = new Gson();
	        
	        for (Client cl : users) {
	            String jsonCl = gson.toJson(cl);
	            bw.write(jsonCl);
	            bw.newLine();
	        }
	        
	        bw.close();
	    } catch (Exception e) {
	        Log.error("DB", e.toString(), e.getMessage());
	    }
	}
}