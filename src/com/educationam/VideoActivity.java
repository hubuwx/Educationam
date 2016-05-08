package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VideoActivity extends ActionBarActivity{
	AllKeys keys;
	String URL;
	LazyAdapterVideo adapter;
	
    
    private dbhandler db;
    private SQLiteDatabase sd;
    private Context context=this;
private Cursor c;    

    ArrayList<HashMap<String, String>> allshools; 
    
    SessionManager session;
    //private TextView txttitle;
	private ListView list;
    
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		try {
			if (android.os.Build.VERSION.SDK_INT >11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>Videos</font>"));
				
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

			}
			else
			{
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>Videos</font>"));
				getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);	
				getActionBar().show();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		allshools =new ArrayList<HashMap<String,String>>();

		//songsList = new ArrayList<HashMap<String, String>>();
		db=new dbhandler(context);
		sd=db.getReadableDatabase();
		sd=db.getWritableDatabase();
		session=new SessionManager(context);
		HashMap<String, String> user=session.getUserDetails();
//		String SchoolID="3";//user.get(SessionManager.KEY_SUBSCHOOL_ID);
	//	String SchoolName=user.get(SessionManagement.KEY_SUBSCHOOL_NAME);
		
		
		
		 URL = keys.WEBSITE+"/JSON_data.aspx?type=Video";//http://api.androidhive.info/music/music.xml";
		

		// txttitle=(TextView)findViewById(R.id.txttitle);
//txttitle.setText(""+SchoolName);


		JSONParser json=new JSONParser();
		
		list=(ListView)findViewById(R.id.list);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position+=1;
				
				
				String url=((TextView)view.findViewById(R.id.schooladdress)).getText().toString();
				
				
				
				 startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(""+url)));

			}
		});
		if(NetConnectivity.isOnline(context))
		{
			new GetVideosFromURL().execute();
		}
		else
		{
			Toast.makeText(context, "Please Enable Data Connection", 1000).show();
		}


		
	

		
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetVideosFromURL extends AsyncTask<Void, Void, Void> 
	{
		//private JSONArray empdetails;
		private JSONArray alltasks;
		private ProgressDialog pDialog;
		private Object context=this;



		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(VideoActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... arg0) 
		{


			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
//{"Video":[{"VideoId":"29", "VideoName":"Alternating Voltage and Current","VideoUrl":"","ThumbCode":"http://img.youtube.com/vi//1.jpg"}]}
			if (jsonStr != null)
			{
				try
				{
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					alltasks = jsonObj.getJSONArray(keys.TAG_MOBILEVIDEOSARRAY);
					//Create Object of dbhandler
					System.out.println("Success");
					try
					{
						/*dbhandler db2=new dbhandler(context);
						SQLiteDatabase sd2=db2.getReadableDatabase();*/
						sd.delete("Video", null, null);
						/*	sd2.delete("BrandMst", null, null);
						sd2.delete("ProductMst", null, null);*/
						/*db2.close();
						sd2.close();*/
					}
					catch(Exception e)
					{
						e.getMessage();
					}
					// looping through All Contacts
					for (int i = 0; i < alltasks.length(); i++) 
					{
						JSONObject c = alltasks.getJSONObject(i);

						String  videoname =c.getString(AllKeys. TAG_VIDEONAME);
						String  videourl =c.getString(AllKeys. TAG_VIDEOURL);
						String  thumbnail =c.getString(AllKeys. TAG_THUMBNAIL);
						





						//Insert Data into the table
						try
						{

							String ddd="";

							String qry="insert into Video values(null,'"+ videoname +"','"+ videourl +"','"+ thumbnail +"','','','')";
							sd.execSQL(qry);


						}
						catch(Exception e)
						{
							System.out.println("Error MSG:"+e.getMessage());
						} 
						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();
						// adding each child node to HashMap key => value


						contact.put(keys.TAG_VIDEONAME, videoname);



						contact.put(keys.TAG_VIDEOURL, videourl);
						
						//thumbnail="http://img.youtube.com/vi/"+thumbnail+"/2.jpg";
						//http://img.youtube.com/vi/TMKZox3rYrc/2.jpg
						contact.put(keys.TAG_THUMBNAIL, thumbnail);
						
						contact.put(keys.TAG_VIDEOID, "");
						//contact.put(keys.TAG_ADDRESS, "");
						




						// adding task to task list
						allshools.add(contact);
					}
				}
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;

		}
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			try
			{

				adapter=new LazyAdapterVideo(VideoActivity.this, allshools);        
				list.setAdapter(adapter);
				if(allshools.isEmpty())
				{
					//list.setVisibility(View.GONE);
					/*					laynodata.setVisibility(View.VISIBLE);
					laynodata.setBackgroundDrawable(getResources().getDrawable(R.drawable.nodata));
					 */

				}
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	if(item.getItemId() ==android.R.id.home)
	{
		
		
			Intent i=new Intent(context,MainActivity.class);
			startActivity(i);
			onBackPressed();
		
		return true;
	}

		
		
		return super.onOptionsItemSelected(item);
		
	}


}
