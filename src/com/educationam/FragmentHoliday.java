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

public class FragmentHoliday extends android.support.v4.app.Fragment {

	ArrayList<HomeWork> homeworklista = new ArrayList<HomeWork>();
	ArrayList<HashMap<String, String>> homeworklist = new ArrayList<HashMap<String, String>>();

	public static FragmentHoliday create() {
		FragmentHoliday f = new FragmentHoliday();
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
	private JSONArray jarrayholiday;
	private String holiday, day, date;
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
		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		deptid = userDetails.get(sessionmanager.KEY_DEPTID);

		listview = (ListView) v.findViewById(R.id.listHomework);

		if (NetConnectivity.isOnline(getActivity())) {
			new GetHolidayDetailsFromServer().execute("");
		} else {
			FillDataonListView();
		}

		return v;
	}

	public void FillDataonListView() {

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from holiday", null);
		getActivity().startManagingCursor(c);
		homeworklist.clear();
		while (c.moveToNext()) {

			holiday = c.getString(1);
			day = c.getString(3);
			date = c.getString(2);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("HOLIDAY", holiday);
			map.put("DAY", "Day : " + day);
			map.put("DATE", date);

			homeworklist.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), homeworklist,
				R.layout.lst_row_single_holiday, new String[] { "HOLIDAY",
						"DAY", "DATE" }, new int[] { R.id.txtholiday,
						R.id.txtday, R.id.txtdate });

		listview.setAdapter(adapter);

	}

	// ***************************************

	class GetHolidayDetailsFromServer extends AsyncTask<String, Void, String> {

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

			/*
			 * String qry = "Select * from DataUrl"; dbhandler db = new
			 * dbhandler(getActivity()); SQLiteDatabase sd =
			 * db.getReadableDatabase(); Cursor cdata = sd.rawQuery(qry, null);
			 * getActivity().startManagingCursor(cdata); while
			 * (cdata.moveToNext()) { urls = cdata.getString(0); }
			 */

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			url = AllKeys.WEBSITE + "json_data.aspx?type=Holiday";

			// url = AllKeys.WEBSITE+ "json_data.aspx?DeptId=" + "3" +
			// "&type=homework";
			JSONObject json = jParser.getJSONFromUrl(url);

			ContentValues cv = new ContentValues();
			if (json != null) {
				try {
					Log.d("Holiday Json data", json.toString());
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Holiday");

					// looping through All Contacts
					sd.delete("holiday", null, null);
					for (int i = 0; i < jarrayholiday.length(); i++) {
						JSONObject c = jarrayholiday.getJSONObject(i);

						sd.execSQL("INSERT INTO holiday VALUES(null,'"
								+ c.getString("HolidayName") + "','"
								+ c.getString("HolidayDate") + "','"
								+ c.getString("Day") + "') ");

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.d("Holiday json error", e.getMessage());
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
