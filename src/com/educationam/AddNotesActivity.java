package com.educationam;

import java.util.Calendar;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;


public class AddNotesActivity extends ActionBarActivity {

	private EditText txtnotetitle;
	private EditText txtnoteDesc;
	private AlertDialogManager alert;
	private Context context=this;
	private String time;
	private String mm;
	private String dd;
	private String date;
	private String notesid;
	private String edit;
	private MenuItem savemenu;
	private int maxid;
	private String url1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_notes);
		getSupportActionBar().show();
		
		txtnotetitle=(EditText)findViewById(R.id.title);
		txtnoteDesc=(EditText)findViewById(R.id.body);
		final Calendar cal = Calendar.getInstance();
		
		 int mMonth = cal.get(Calendar.MONTH);
		 int year=cal.get(Calendar.YEAR);
		 int day=cal.get(Calendar.DAY_OF_MONTH);
		 int hh=cal.get(Calendar.HOUR);
		 int min=cal.get(Calendar.MINUTE);
		 int apm=cal.get(Calendar.AM_PM);
		 
			if(apm==0)
			{
				time=hh+":"+min+"AM";
			}
			else
			{
				time=hh+":"+min+"PM";
			}
			mMonth=mMonth+1;
			if(mMonth<=9)
			{
				mm="0"+mMonth;
			}
			else
				mm=""+mMonth;
			
			if(day<=9)
			{
				dd="0"+day;
			}
			else
				dd=""+day;
			date=dd+"-"+mm+"-"+year;
		
		getSupportActionBar().show();
		
		setTitleColor(Color.WHITE);
		
		  
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setBackgroundDrawable(new 
		               ColorDrawable(Color.parseColor("#E52847"))); 
		
		 notesid=getIntent().getStringExtra("id");
		 edit=getIntent().getStringExtra("Edit");
		
		 if(notesid.equals("false"))
		 {}
		 else
		 {
			 dbhandler db=new dbhandler(context);
			 SQLiteDatabase sd=db.getReadableDatabase();
			 Cursor c=sd.rawQuery("SELECT * FROM NOTES where id="+notesid, null);
				startManagingCursor(c);
				while(c.moveToNext())
				{
					txtnotetitle.setText(""+c.getString(1));
					txtnoteDesc.setText(""+c.getString(2));
				}
		 }
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_notes, menu);
		
		
	    savemenu=menu.findItem(R.id.action_savenotes);
	    if(edit.toString().equals("false"))
	    {
	        savemenu.setVisible(false);
	        getSupportActionBar().setTitle("Notes");
	    }
	    else
	    {
	    	savemenu.setVisible(true);
	    	getSupportActionBar().setTitle("Add Notes");
	    }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_savenotes) {
			if(txtnotetitle.getText().toString().length()>0)
			{
				dbhandler db=new dbhandler(context);
				SQLiteDatabase sd=db.getWritableDatabase();
				ContentValues cv=new ContentValues();
				if(notesid.equals("false"))
				{
					
					cv.put("id", 1);
					cv.put("notetitle",txtnotetitle.getText().toString());
					cv.put("notedesc",txtnoteDesc.getText().toString());
					cv.put("notedate",date);
					cv.put("notetime",time);
					
					sd.insert("NOTES", null, cv);
					WebView web=new WebView(context);
					 String qry="Select * from DataUrl";
						
						Cursor cdata=sd.rawQuery(qry, null);
						startManagingCursor(cdata);
						while(cdata.moveToNext())
						{
							url1=cdata.getString(0);
						}
				      
					web.loadUrl(url1+"json_data.aspx?NoteTitle="+txtnotetitle.getText().toString()+"&NoteDesc="+txtnoteDesc.getText().toString()+"&Dt="+date+"&Time="+time+"&type=Note");
					
				}
				else
				{
					sd.execSQL("UPDATE NOTES SET notetitle='"+txtnotetitle.getText().toString()+"',notedesc='"+txtnoteDesc.getText().toString()+"',notedate='"+date+"',notetime='"+time+"' where id="+notesid);
				}
				Intent intent=new Intent(context,AdminNotesActivity.class);
				startActivity(intent);
				
			}
			else
			{
				alert=new AlertDialogManager();
				alert.showAlertDialog(context, "Invalid notes", "Please Enter Notes Title", false);
			}
			return true;
		}
		if(id==android.R.id.home)
		{
			this.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_notes,
					container, false);
			return rootView;
		}
	}
	
	 public void onBackPressed() {
	        super.onBackPressed();

	    }


	   

}
