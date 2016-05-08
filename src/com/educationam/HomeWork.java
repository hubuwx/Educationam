package com.educationam;

public class HomeWork {

	String homework, faculty, date;

	public HomeWork(String homework, String faculty, String date) {
		super();
		this.homework = homework;
		this.faculty = faculty;
		this.date = date;
	}

	public HomeWork() {
		super();
	}

	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
