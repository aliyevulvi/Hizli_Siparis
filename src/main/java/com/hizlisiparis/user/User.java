package com.hizlisiparis.user;

import com.hizlisiparis.logger.Log;
import com.google.gson.Gson;

public class User {
	private String userName;
	private String userPass;
	private String userLevel
	
	public User(String name, String pass, String level) {
	    this.userName = name;
	    this.userPass = pass;
	    this.userLevel = level;
	}
	
	public String getName() {
	    return this.userName;
	}
	
	public String getLevel() {
	    return this.userLevel;
	}
	
	public String getUserJson() {
	    try {
	        Gson gson = new Gson();
	        String userJson = gson.toJson(this);
	        
	        return userJson;
	    } catch {
	        Log.error("User", e.toString(), e.getMessage());
	        return "Error_100";
	    }
	}
}