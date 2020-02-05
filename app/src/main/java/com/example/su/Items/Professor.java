package com.example.su.Items;

public class Professor {

	private String email;
	private String name;
	private long department;
	private String roomNo;
	private boolean available;

	public Professor(String email, String name, long department, String roomNo, boolean available) {
		this.email = email;
		this.name = name;
		this.department = department;
		this.roomNo = roomNo;
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public long getDepartment() {
		return department;
	}

	public String getDepartmentName() {
		//TODO: implement long-based system for departments and return name here
		return "Computer Science";
	}

	public String getRoomNo() {
		return roomNo;
	}

	public boolean isAvailable() {
		return available;
	}

	public String getEmail() {
		return email;
	}
}
