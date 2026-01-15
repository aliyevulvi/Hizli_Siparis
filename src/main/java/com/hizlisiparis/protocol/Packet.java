package com.hizlisiparis.protocol;

import java.util.*;
import java.time.LocalTime;
import com.google.gson.Gson;
import com.hizlisiparis.logger.Log;

public class Packet {
	private String packetType;
	private String packetVer;
	private String timestamp;
	private ArrayList<String> payload = new ArrayList<>();

	public static Packet getPacket(byte[] packetBytes) {
		try {
			Gson gson = new Gson();
			String packetStr = new String(packetBytes, "UTF-8");
			Packet packet = gson.fromJson(packetStr, Packet.class);

			return packet;

		} catch (Exception e) {
			Log.error("Packet", e.toString(), e.getMessage());
			return null;
		}
	}

	public Packet(String type, String ver, String oneline) {
		try {
			this.packetType = type;
			this.packetVer = ver;
			LocalTime now = LocalTime.now();
			this.timestamp = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
			this.payload.add(oneline);
		} catch (Exception e) {
			Log.error("Client", e.toString(), e.getMessage());
		}
	}
	
	public Packet(String type, String ver) {
	    try {
			this.packetType = type;
			this.packetVer = ver;
			LocalTime now = LocalTime.now();
			this.timestamp = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
		} catch (Exception e) {
			Log.error("Client", e.toString(), e.getMessage());
		}
	}

	public String getJsonStr() {
		try {
			Gson gson = new Gson();
			String packetJsonStr = gson.toJson(this);

			return packetJsonStr;
		} catch (Exception e) {
			Log.error("Packet", e.toString(), e.getMessage());
			return null;
		}
	}

	public String getType() {
		return packetType;
	}

	public ArrayList<String> getPayload() {
        return payload;
	}
}