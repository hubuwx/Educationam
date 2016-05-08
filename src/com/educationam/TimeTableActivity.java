package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class TimeTableActivity extends Fragment {

	public static TimeTableActivity create() {
		TimeTableActivity f = new TimeTableActivity();
		return f;
	}

	private ExpandableListView mExpandableList;
	ArrayList<Parent> arrayParents = new ArrayList<Parent>();
	ArrayList<String> arrayChildren = new ArrayList<String>();
	String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private String deptid;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_time_table, container, false);

		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();

		// listview=(ListView)v.findViewById(R.id.listView1);
		mExpandableList = (ExpandableListView) v.findViewById(R.id.expandableListView1);
		populatelist();

		mExpandableList.setAdapter(new ExpandableListAdapter(getActivity(), arrayParents));
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ConnectionDetector cd = new ConnectionDetector(getActivity());

		if (cd.isConnectingToInternet() == true) {
			new DownloadWebPageTask().execute("");
		} else {
			populatelist();
		}

	}

	public void populatelist() {
		arrayParents.clear();
		Parent parent = new Parent();
		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor cdept = sd.rawQuery("select * from studinfo", null);
		getActivity().startManagingCursor(cdept);
		while (cdept.moveToNext()) {
			deptid = cdept.getString(0);
		}
		for (int i = 0; i < days.length; i++) {
			parent = new Parent();

			parent.setTitle("      " + days[i]);

			parent.setArrayChildren(arrayChildren);

			arrayChildren = new ArrayList<String>();
			arrayChildren.clear();
			String quesry = "select * from time_tb_mst where day='" + days[i] + "' and deptid=" + deptid;
			Cursor c = sd.rawQuery(quesry, null);
			getActivity().startManagingCursor(c);
			while (c.moveToNext()) {
				arrayChildren.add(c.getString(1) + "               " + c.getString(3));
			}
			parent.setArrayChildren(arrayChildren);

			// in this array we add the Parent object. We will use the
			// arrayParents at the setAdapter
			arrayParents.add(parent);
		}

	}

	class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private String urls;
		private String url;
		private JSONObject json6;
		private JSONArray jarrayholiday;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
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
			pDialog.dismiss();
			pDialog.cancel();

			String qry = "Select * from DataUrl";
			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();
			Cursor cdata = sd.rawQuery(qry, null);
			getActivity().startManagingCursor(cdata);
			while (cdata.moveToNext()) {
				urls = cdata.getString(0);
			}

			JSONParser jParser = new JSONParser();

			url = AllKeys.WEBSITE + "GetTimeTableData?type=TimeTable&deptid="
					+ userDetails.get(SessionManager.KEY_DEPTID) + "&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(sessionmanager.KEY_BRANCHID);

			// getting JSON string from URL
			// url = urls + "json_data.aspx?DeptId=" +
			// userDetails.get(SessionManager.KEY_DEPTID) + "&type=TimeTable";
			// json6 = jParser.getJSONFromUrl(url);
			
			//url = "http://radiant.dnsitexperts.com/json_data.aspx?type=TimeTable&deptid=13";

			ServiceHandler sh = new ServiceHandler();
			String str_stud_time = sh.makeServiceCall(url, ServiceHandler.GET);

			ContentValues cv = new ContentValues();
			if (str_stud_time != null && !str_stud_time.equals("")) {

				str_stud_time = "{" + '"' + "TimeTable" + '"' + ":" + str_stud_time + "}";

				try {
					try {
						JSONObject json6 = new JSONObject(str_stud_time);
						//json6 = jParser.getJSONFromUrl(url);
						if (json6 != null) {
							try {
								// Getting Array of Contacts
								jarrayholiday = json6.getJSONArray("TimeTable");
								// looping through All Contacts
								sd.delete("time_tb_mst", null, null);
								for (int i = 0; i < jarrayholiday.length(); i++) {
									JSONObject c = jarrayholiday.getJSONObject(i);

									sd.execSQL("INSERT INTO time_tb_mst VALUES('" + c.getString("DeptId") + "','"
											+ c.getString("Time") + "','" + c.getString("Day") + "','"
											+ c.getString("Subject") + "') ");

								}
								populatelist();

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}

	}

}
