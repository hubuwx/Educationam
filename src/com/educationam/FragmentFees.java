package com.educationam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.educationam.HolidayActivity.DownloadWebPageTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FragmentFees extends android.support.v4.app.Fragment {

	ArrayList<HomeWork> homeworklista = new ArrayList<HomeWork>();
	ArrayList<HashMap<String, String>> homeworklist=new ArrayList<HashMap<String,String>>();

	public static FragmentAchievements create() {
		FragmentAchievements f = new FragmentAchievements();
		return f;
	}

	Activity a;
	private String deptid;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	private ListView listview;

	private Cursor c;
	private String urls;
	private String url;
	private JSONArray jarrayhomework;
	private String homeworkname, facultyname, date;
	private dbhandler db;
	private SQLiteDatabase sd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.activity_home_work, container, false);
		
		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();
		db= new dbhandler(getActivity());
		sd= db.getReadableDatabase();
		sd=db.getWritableDatabase();
	
		deptid = userDetails.get(sessionmanager.KEY_DEPTID);
		
		listview = (ListView) v.findViewById(R.id.listHomework);
		
		
		
		
		if(NetConnectivity.isOnline(getActivity()))
		{
			new GetFeesDetailsFromServer().execute("");
		}
		else
		{
			FillDataonListView();
		}
		
		
		return v;
	}

	
	public void FillDataonListView() {

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from achievementsmst", null);
		getActivity().startManagingCursor(c);
		homeworklist.clear();
		while (c.moveToNext()) {
			
			homeworkname = c.getString(1);
			facultyname = c.getString(2);
			date = c.getString(3);
			String studname=c.getString(4);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("ACHIEVEMENT", homeworkname);
			map.put("FACULTY", "Teacher : "+facultyname);
			map.put("DATE", date);
			map.put("STUDNAME", studname);
			
			

			homeworklist.add(map);
		}


		SimpleAdapter adapter = new SimpleAdapter(getActivity(), homeworklist,
				R.layout.lst_row_single_achievement, new String[] { "ACHIEVEMENT",
						"FACULTY", "DATE" ,"STUDNAME"}, new int[] { R.id.lblAchievement,
						R.id.lblFaculty, R.id.lblDate,R.id.txtstudname });
		
		listview.setAdapter(adapter);

	}

	// ***************************************

	class GetFeesDetailsFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());

			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {
			
			
			

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			// url = AllKeys.WEBSITE+ "json_data.aspx?DeptId=" + deptid + "&type=achievement";
			
			url = AllKeys.WEBSITE+ "json_data.aspx?DeptId=1&type=achievement";
			JSONObject json = jParser.getJSONFromUrl(url);
			Log.d("Achievement Json data",json.toString());

			ContentValues cv = new ContentValues();
			if (json != null) {
				try {
					// Getting Array of Contacts
					jarrayhomework = json.getJSONArray("Achievement");

					// looping through All Contacts
					sd.delete("achievementsmst", null, null);
					for (int i = 0; i < jarrayhomework.length(); i++) {
						JSONObject c = jarrayhomework.getJSONObject(i);

						sd.execSQL("INSERT INTO achievementsmst VALUES(null,'"
								+ c.getString("Description") + "','"
								+ c.getString("FacultyID") + "','"
								+ c.getString("Date") + "','sathish') ");

					}
		
				} catch (JSONException e) {
					e.printStackTrace();
					Log.d("Homework json error",e.getMessage());
				}
			}
			
			
			return null;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();


			FillDataonListView();

		}

	}

	// ***************************************
}
