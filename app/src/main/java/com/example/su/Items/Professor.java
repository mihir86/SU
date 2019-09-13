package com.example.su.Items;

public class Professor {

	private String professorName;
	private String roomNumber;
	private String courseCode;
	private boolean available;

	public Professor(String professorName, String roomNumber, String courseCode, boolean available)
	{
		this.professorName = professorName;
		this.roomNumber = roomNumber;
		this.courseCode = courseCode;
		this.available = available;
	}

	public String getProfessorName() {
		return professorName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public boolean isAvailable() {
		return available;
	}
}
