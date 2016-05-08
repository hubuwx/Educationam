package com.educationam;

public class HolidayItem {
	 String title=null;
	 String day=null;
	 String date=null;
	 
	  
	 public HolidayItem(String title,String day,String date) 
	  {
		  super();
		
		  this.day=day;
		  this.title=title;
		  this.date=date;
		
	 }
	  
	 public String getTitel() 
	 {
		 return title;
	 }
	 public void setTitle(String title)
	 {
		 this.title = title;
	 }
	 public String getDay() 
	 {
		 return day;
	 }
	 public void setDay(String day) 
	 {
		 this.day = day;
	 }
	 
	 public String getDate() 
	 {
		 return date;
	 }
	 public void setDate(String date)
	 {
		 this.date = date;
	 }
	
}
