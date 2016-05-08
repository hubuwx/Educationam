package com.educationam;

public class ListItem {
	 String title=null;
	 String time=null;
	 
	  
	  public ListItem(String title,String time) 
	  {
		  super();
		
		  this.time=time;
		  this.title=title;
		
	 }
	  
	 public String getTitel() 
	 {
		 return title;
	 }
	 public void setTitle(String title)
	 {
		 this.title = title;
	 }
	 public String getTime() 
	 {
		 return time;
	 }
	 public void setTime(String time) 
	 {
		 this.time = time;
	 }
	
}
