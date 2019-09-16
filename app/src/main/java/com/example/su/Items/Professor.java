package com.example.su.Items;

public class Professor {

	private String Name;
	private String Department;
	private String ChamberNumber;
	private boolean Availability;

	public Professor(){}

	public Professor(String name, String department, String chamberNumber,boolean availability) {
		Name = name;
		Department = department;
		ChamberNumber = chamberNumber;
		Availability = availability;
	}

	public String getName() {
		return Name;
	}

	public String getDepartment() {
		return Department;
	}

	public String getChamberNumber() {
		return ChamberNumber;
	}

	public boolean isAvailability() {
		return Availability;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public void setChamberNumber(String chamberNumber) {
		ChamberNumber = chamberNumber;
	}

	public void setAvailability(boolean availability) {
		Availability = availability;
	}
}
