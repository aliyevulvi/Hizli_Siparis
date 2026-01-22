package com.hizlisiparis.database;

import com.hizlisiparis.logger.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import java.time.*;
import com.hizlisiparis.client.*;
import com.hizlisiparis.product.*;

public class DB {
	private static ArrayList<Client> users = new ArrayList<>();
	private static ArrayList<Product> products = new ArrayList<>();

	//    CLIENT DB STUFF

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

	//    PRODUCT DB STUFF

	public static void addProduct(Product product) {
		try {
			getProducts();
			for (Product pr : products) {
				if (pr.getProductName().equals(product.getProductName())) {
					return;
				}
			}
			File menuFile = new File("Data/menu.json");

			Gson gson = new Gson();
			String productString = gson.toJson(product);

			BufferedWriter bw = new BufferedWriter(new FileWriter(menuFile, true));
			bw.write(productString);
			bw.newLine();
			bw.close();

			File lastMod = new File("Data/lastModification.txt");
			if (!lastMod.exists()) {
				lastMod.createNewFile();
			}

			BufferedWriter bw2 = new BufferedWriter(new FileWriter(lastMod));
			LocalDateTime now = LocalDateTime.now();
			bw2.write(now.getDayOfMonth() + "." + now.getMonth() + "." + now.getYear() + "." + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
			bw2.close();

		} catch (Exception e) {
			Log.error("Product", e.toString(), e.getMessage());
		}
	}

	public static ArrayList<Product> getProducts() {
		try {
			File menuFile = new File("Data/menu.json");

			if (!menuFile.exists()) {
				menuFile.createNewFile();
			}

			BufferedReader br = new BufferedReader(new FileReader(menuFile));
			Gson gson = new Gson();

			String productString;
			while ((productString = br.readLine()) != null) {
				Product product = gson.fromJson(productString, Product.class);
				products.add(product);
			}

			br.close();
			
			return products;

		} catch (Exception e) {
			Log.error("Product", e.toString(), e.getMessage());
			return null;
		}
	}
	
	public static String getLastMod() {
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(new File("Data/lastModification.txt")));
	        String lastModStr = br.readLine();
	        
	        br.close();
	        
	        return lastModStr;
	        
	    } catch (Exception e) {
	        Log.error("DB", e.toString(), e.getMessage());
	    }
	    
	    return null;
	}
}