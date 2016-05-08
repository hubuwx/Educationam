package com.educationam;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExamsheetFrag extends Fragment{
	
	Activity a;
	//ArrayList<HolidayItem> holidaylist=new ArrayList<HolidayItem>();
	public static ExamsheetFrag create() {
		ExamsheetFrag f = new ExamsheetFrag();
        return f;
    }
	 
	
    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.examsheetxml, container, false);
      
       
		
        return v;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState); 
           
        //add data to ListView 
       
    } 
}
