package com.educationam;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;


public class AppoinmentActivity extends Fragment {
	static final int DATE_PICKER_ID = 1111;
	 static final int TIME_DIALOG_ID = 1112; 
	 private int hour;
		private int minute;
	private int y;
	private int m;
	private int d;
	private int spm;
	private String startDate,sendDate;;
	private Button btnDate;
	private Button btnTime;
	private Button btnSubmit;
	private EditText txtreason;
	private String ampm;
	ArrayList<HolidayItem> lstapp=new ArrayList<HolidayItem>();
	private ListView listview;
	private String str;
	private String s;
	public static AppoinmentActivity create() {
		AppoinmentActivity f = new AppoinmentActivity();
        return f;
    }
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_appoinment, container, false);
      
       listview=(ListView)v.findViewById(R.id.listView1);
		btnDate=(Button)v.findViewById(R.id.button1);
		btnTime=(Button)v.findViewById(R.id.button2);
		btnSubmit=(Button)v.findViewById(R.id.button3);
		txtreason=(EditText)v.findViewById(R.id.editText1);
		 Calendar cal = Calendar.getInstance();
	    	y = cal.get(Calendar.YEAR);
	    		m = cal.get(Calendar.MONTH);
	    		 d = cal.get(Calendar.DAY_OF_MONTH);

	    	 spm=m+1;
	    	 if(spm<=9)
	    	 {
	    		 	String mm="0"+spm;
	    		 	startDate=d+"-"+mm+"-"+y;
	    		 	sendDate=y+"-"+mm+"-"+d;
	    	 }
	    	 else
	    	 {
	    		 	startDate=d+"-"+spm+"-"+y;
	    	 		sendDate=y+"-"+spm+"-"+d;
	    	 }
	    		 btnDate.setText(startDate);
	    		
        return v;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState); 
        
        btnDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogFragment picker = new DatePickerFragment();
				picker.show(getFragmentManager(), "datePicker");
			}
		});
        Calendar cal=Calendar.getInstance();
        
      
        int amp=cal.get(Calendar.AM_PM);
        if(amp==1)
        	ampm="PM";
        else
        	ampm="AM";
        btnTime.setText(""+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+""+ampm);
        btnTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 DialogFragment newFragment = new TimePickerFragment();
				    newFragment.show(getFragmentManager(), "timePicker");

			}
		});
        dbhandler db=new dbhandler(getActivity());
		SQLiteDatabase sd=db.getWritableDatabase();
		
		Cursor c=sd.rawQuery("select * from appoinment", null);
		getActivity().startManagingCursor(c);
		lstapp.add(0,new HolidayItem("","Status","Reason"));
		while(c.moveToNext())
		{
			if(c.getString(6).equals("A"))
				str="Accept";
			else if(c.getString(6).equals("R"))
				str="Reject";
				
			
			 SimpleDateFormat sf=new SimpleDateFormat("dd MMM yyyy");
			 String dt=c.getString(1);
			 if(dt.contains("-"))
			 {
				 
				 String[] dts=dt.split("-");
				 if(dts.length>2)
				 { 
					 int y=Integer.parseInt(dts[0]);
					 int m=Integer.parseInt(dts[1]);
					 int d=Integer.parseInt(dts[2]);
					 m=m-1;
					 y=y-1900;
					  s=sf.format(new Date(y, m, d));
				 }
			 }
			 else
				 s=dt;
			lstapp.add(new HolidayItem(c.getString(0),str,c.getString(3)+"\n"+s+"    "+c.getString(2)+"\n"));
		}
        
		listview.setAdapter(new AppoinAdapter(getActivity(), android.R.layout.simple_list_item_1, lstapp));
        
        btnSubmit.setOnClickListener(new OnClickListener() {
			
			private int maxid;
			private int deptid;
			private int empid;
			private String url1;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((txtreason.getText().toString().length()==0) || (btnTime.getText().toString().equals("Time")))
				{
					
					AlertDialogManager alert =new AlertDialogManager();
					alert.showAlertDialog(getActivity(), "Error", "pls enter reason or set time", false);
					
				}
				else
				{
					dbhandler db=new dbhandler(getActivity());
					SQLiteDatabase sd=db.getWritableDatabase();
					
					Cursor c=sd.rawQuery("select max(id) from appoinment", null);
					getActivity().startManagingCursor(c);
					while(c.moveToNext())
					{
						maxid=c.getInt(0);
						maxid=maxid+1;
					}
					
					 c=sd.rawQuery("select * from studinfo", null);
					getActivity().startManagingCursor(c);
					if(c.getCount()>0)
					{
						while(c.moveToNext())
						{
						 deptid=c.getInt(0);
						 empid=c.getInt(1);
						}
					}
					sd.execSQL("INSERT INTO appoinment values("+maxid+",'"+sendDate+"','"+btnTime.getText().toString()+"','"+txtreason.getText().toString()+"',"+deptid+","+empid+",'N')");
					WebView web=new WebView(getActivity());
					 String qry="Select * from DataUrl";
						
						Cursor cdata=sd.rawQuery(qry, null);
						getActivity().startManagingCursor(cdata);
						while(cdata.moveToNext())
						{
							url1=cdata.getString(0);
						}
				      String url=url1+"json_data.aspx?Reason="+txtreason.getText().toString()+"&DeptId="+deptid+"&Dt="+sendDate+"&Time="+btnTime.getText().toString()+"&type=App&EmpId="+empid+"&APPFLG=N";
					web.loadUrl(url);
					
					AlertDialogManager alert =new AlertDialogManager();
					alert.showAlertDialog(getActivity(), "Success", "Successfully Appointment Added", true);
					txtreason.setText("");
				}
			}
		});
        
    }
    
    

    @SuppressLint("ValidFragment")
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH); 

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
    Calendar c = Calendar.getInstance();
    c.set(year, month, day);

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = sdf.format(c.getTime());  
    btnDate.setText(formattedDate);
    }
    }
    
    
    @SuppressLint("ValidFragment")
	public  class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker
final Calendar c = Calendar.getInstance();
int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);

// Create a new instance of TimePickerDialog and return it
return new TimePickerDialog(getActivity(), this, hour, minute,
DateFormat.is24HourFormat(getActivity()));
}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// Do something with the time chosen by the user
	if(DateFormat.is24HourFormat(getActivity())==false)
	//btnTime.setText(""+hourOfDay+":"+minute+""+DateFormat.is24HourFormat(getActivity()));
	updateTime(hourOfDay, minute);
}
    }
    private  String utilTime(int value) {
    	 
    	if (value < 10)
    	    return "0" + String.valueOf(value);
    	else
    	    return String.valueOf(value);
    	}

    	// Used to convert 24hr format to 12hr format with AM/PM values
    	private void updateTime(int hours, int mins) {
    	 
    	String timeSet = "";
    	if (hours > 12) {
    	    hours -= 12;
    	    timeSet = "PM";
    	} else if (hours == 0) {
    	    hours += 12;
    	    timeSet = "AM";
    	} else if (hours == 12)
    	    timeSet = "PM";
    	else
    	    timeSet = "AM";

    	 
    	String minutes = "";
    	if (mins < 10)
    	    minutes = "0" + mins;
    	else
    	    minutes = String.valueOf(mins);

    	// Append in a StringBuilder
    	 String aTime = new StringBuilder().append(hours).append(':')
    	        .append(minutes).append(" "+timeSet).toString();

    	  btnTime.setText(aTime);
    	}

}
