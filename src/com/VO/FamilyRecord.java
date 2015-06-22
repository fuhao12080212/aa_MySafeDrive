package com.VO;

public class FamilyRecord {
	private int Id;
	private String username;
	private String Time;
	private String Type;
	private String Data;
	private String DangerTime;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

	public String getDangerTime() {
		return DangerTime;
	}

	public void setDangerTime(String dangerTime) {
		DangerTime = dangerTime;
	}

}
