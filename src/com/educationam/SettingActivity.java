package com.educationam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class SettingActivity extends ActionBarActivity {

	private EditText txturl;
	private Button btnsubmit;
	
	private Context context=this;
	private Button btnimport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		txturl=(EditText)findViewById(R.id.editText1);
		btnsubmit=(Button)findViewById(R.id.button1);
		btnimport=(Button)findViewById(R.id.button2);
				
		
		btnsubmit.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbhandler db=new dbhandler(context);
				SQLiteDatabase sd=db.getWritableDatabase();
				sd.delete("dataurl", null, null);
				if(txturl.getText().toString().length()==0)
				{
					Toast.makeText(context, "pls fill all fields", 1000).show();
				}
				else
				{
					sd.execSQL("INSERT INTO dataurl values('"+txturl.getText().toString()+"')");
					Toast.makeText(context, "Successfully inserted", 1000).show();
				}
			}
		});
		btnimport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConnectionDetector cd=new ConnectionDetector(context);
				 AlertDialogManager alert=new AlertDialogManager();
				 if(cd.isConnectingToInternet()==true)
			     {
					new DownloadWebPageTask().execute("");
					
			     }
				 else
				 {
					alert.showAlertDialog(context, "Network Error", "No Network Connection", false);
				 }
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		  

		private String url1;
		private ProgressDialog pDialog;
		private String url;
		private JSONArray jarrayholiday;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(context);
			pDialog.setTitle("Loading....");
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
	    protected String doInBackground(String... urls) {
	      String response = "";
	     

	     
	      
	      return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	 String qry="Select * from DataUrl";
				dbhandler db=new dbhandler(context);
				SQLiteDatabase sd=db.getReadableDatabase();
				Cursor cdata=sd.rawQuery(qry, null);
				startManagingCursor(cdata);
				while(cdata.moveToNext())
				{
					url1=cdata.getString(0);
				}
		      
				JSONParser jParser = new JSONParser();
		
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Faculty";
				JSONObject json = jParser.getJSONFromUrl(url);
				
					
				ContentValues cv=new ContentValues();
				
		
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Faculty");
					
					
					
					// looping through All Contacts
					sd.delete("staffinfo", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
					
						sd.execSQL("INSERT INTO staffinfo VALUES('"+c.getString("FacultyId")+"','"+c.getString("FacultyName")+"') ");
						
					}
					
					
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				jParser = new JSONParser();
				
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Subject";
				 json = jParser.getJSONFromUrl(url);
				
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("SubjectMst");
						
					// looping through All Contacts
					sd.delete("submaster", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
						sd.execSQL("INSERT INTO submaster VALUES('"+c.getString("code")+"','"+c.getString("name")+"') ");
					}
					
					
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				jParser = new JSONParser();
				
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Category";
				 json = jParser.getJSONFromUrl(url);
				
					
		
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("LinksCategory");
						// looping through All Contacts
					sd.delete("linkcategory", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
						sd.execSQL("INSERT INTO linkcategory VALUES('"+c.getString("CategoryId")+"','"+c.getString("CategoryName")+"') ");
						
					}
						
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				
				jParser = new JSONParser();
				
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Links";
				 json = jParser.getJSONFromUrl(url);
				
				 try 
				 {
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Dept");
					
					
					
					// looping through All Contacts
					sd.delete("uselink", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
					
						sd.execSQL("INSERT INTO uselink VALUES('"+c.getString("LinkId")+"','"+c.getString("CategoryId")+"','"+c.getString("LinkName")+"','"+c.getString("Link")+"','"+c.getString("LinkDesc")+"') ");
						
					}
					
					
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				
jParser = new JSONParser();
				
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Dept";
				 json = jParser.getJSONFromUrl(url);
			
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Dept");
							// looping through All Contacts
					sd.delete("stdmaster", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
					
						sd.execSQL("INSERT INTO stdmaster VALUES('"+c.getString("DeptId")+"','"+c.getString("DeptName")+"') ");
						
					}
					
					
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
				
				jParser = new JSONParser();
				
				// getting JSON string from URL
				url=url1+"json_data.aspx?DeptId=7&type=TimeTable";
				 json = jParser.getJSONFromUrl(url);
			
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("TimeTable");
					
					
					
					// looping through All Contacts
					sd.delete("time_tb_mst", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
					
						sd.execSQL("INSERT INTO time_tb_mst VALUES('"+c.getString("DeptId")+"','"+c.getString("Day")+"','"+c.getString("Time")+"','"+c.getString("Subject")+"','"+c.getString("FacultyId")+"') ");
						
					}
					
					pDialog.dismiss();
				    pDialog.cancel();
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
		     
	    }
	  }


}
