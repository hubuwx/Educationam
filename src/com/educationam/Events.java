package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.educationam.FragmentHomework.GetHomewrokDetailsFromServer;
import com.educationam.TestSolution.ProgressTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Events extends android.support.v4.app.Fragment {

	ArrayList<HashMap<String, String>> homeworklist = new ArrayList<HashMap<String, String>>();
	Activity a;
	private CustomAdapter customeadaptor;
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> arrPath = new ArrayList<String>();
	ArrayList<String> arrTitle = new ArrayList<String>();

	public static TestSolution create() {
		TestSolution f = new TestSolution();
		return f;
	}

	private ListView listview;

	private Cursor c;
	private String urls;
	private String url;
	private JSONArray jarrayevent;
	private String s, eventname, desc, date, time;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.events_frag, container, false);

		listview = (ListView) v.findViewById(R.id.listEvents);

		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		// add data to ListView
		ConnectionDetector cd = new ConnectionDetector(getActivity());

		if (NetConnectivity.isOnline(getActivity())) {
			new ProgressTask().execute("");
		} else {
			FillDataonListView();
		}

	}

	public void FillDataonListView() {

		try {
			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();

			Cursor c = sd.rawQuery("select * from event", null);
			getActivity().startManagingCursor(c);
			homeworklist.clear();
			while (c.moveToNext()) {

				eventname = c.getString(1);
				desc = c.getString(2);
				date = c.getString(3);
				time = c.getString(4);

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("EVENT", eventname);
				map.put("DESC", desc);
				map.put("DATE", date);
				map.put("TIME", time);

				homeworklist.add(map);
			}

			SimpleAdapter adapter = new SimpleAdapter(getActivity(),
					homeworklist, R.layout.list_event_item, new String[] {
							"EVENT", "DESC", "DATE", "TIME" }, new int[] {
							R.id.lblEventName, R.id.lblDescription,
							R.id.lblDate, R.id.lblTime });

			listview.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param msg
	 */

	class ProgressTask extends AsyncTask<String, Integer, String> {

		private ProgressDialog progressDialog;
		private String url1;
		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Please wait");
			progressDialog.setMessage("Test Solutions Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {

			JSONParser jParser1 = new JSONParser();

			// getting JSON string from URL
			// url=url1+"json_data.aspx?type=testsolution&classid="+
			// userDetails.get(SessionManagement.KEY_DEPTID) +"";

			url = AllKeys.WEBSITE + "GetJSONForEvents?clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(sessionmanager.KEY_BRANCHID);
			Log.d("events =>", url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_events = sh
					.makeServiceCall(url, ServiceHandler.GET);

			if (str_stud_events != null && !str_stud_events.equals("")) {

				// str_stud_events = "{" + '"' + "Events" + '"' + ":" +
				// str_stud_events + "}";

				try {
					JSONObject jsonnotes = new JSONObject(str_stud_events);
					// url =
					// "http://educationam.dnsitexperts.com/json_data.aspx?type=event";
					// JSONObject jsonnotes = jParser1.getJSONFromUrl(url);
					if (jsonnotes != null) {
						try {
							// Getting Array of Contacts
							jarrayevent = jsonnotes.getJSONArray("Events");

							// looping through All Contacts
							// db.execSQL("create table IF NOT EXISTS
							// testsolution(id INTEGER PRIMARY KEY
							// AUTOINCREMENT,filepath TEXT,title TEXT)");
							sd.delete("event", null, null);
							for (int i = 0; i < jarrayevent.length(); i++) {
								JSONObject c = jarrayevent.getJSONObject(i);

								sd.execSQL("INSERT INTO event VALUES(null,'"
										+ c.getString("EventName") + "','"
										+ c.getString("Description") + "','"
										+ c.getString("Date") + "','"
										+ c.getString("Time") + "') ");

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressDialog.isShowing())
				progressDialog.dismiss();
			FillDataonListView();
		}
	}

}
