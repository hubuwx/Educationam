package com.educationam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HolidayActivity extends Fragment {

	Activity a;
	ArrayList<HolidayItem> holidaylist = new ArrayList<HolidayItem>();

	public static HolidayActivity create() {
		HolidayActivity f = new HolidayActivity();
		return f;
	}

	private ListView listview;
	private Cursor c;
	private String urls;
	private String url;
	private JSONArray jarrayholiday;
	private String s;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.holidayactivity, container, false);

		listview = (ListView) v.findViewById(R.id.listView1);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// add data to ListView

		ConnectionDetector cd = new ConnectionDetector(getActivity());

		if (cd.isConnectingToInternet() == true) {
			new DownloadWebPageTask().execute("");
		} else {
			showdata();
		}

	}

	public void showdata() {

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from holiday", null);
		getActivity().startManagingCursor(c);
		while (c.moveToNext()) {
			SimpleDateFormat sf = new SimpleDateFormat("dd MMM yyyy");

			String dt = c.getString(2);
			if (dt.contains("-")) {

				String[] dts = dt.split("-");
				if (dts.length > 2) {
					int y = Integer.parseInt(dts[0]);
					int m = Integer.parseInt(dts[1]);
					int d = Integer.parseInt(dts[2]);
					m = m - 1;
					y = y - 1900;
					s = sf.format(new Date(y, m, d));
				}
			} else
				s = c.getString(2);
			holidaylist.add(new HolidayItem("" + s, c.getString(3), c
					.getString(1)));
		}

		ListView listView = (ListView) getActivity().findViewById(
				R.id.listView1);
		// listView.setAdapter(new ListAdapter(getActivity(),
		// android.R.layout.simple_list_item_1, holidaylist));

	}

	class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;

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

		@SuppressLint("SimpleDateFormat")
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

			// getting JSON string from URL
			url = urls + "json_data.aspx?type=Holiday";
			JSONObject json = jParser.getJSONFromUrl(url);

			ContentValues cv = new ContentValues();
			if (json != null) {
				try {
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Holiday");

					// looping through All Contacts
					sd.delete("holiday", null, null);
					for (int i = 0; i < jarrayholiday.length(); i++) {
						JSONObject c = jarrayholiday.getJSONObject(i);

						sd.execSQL("INSERT INTO holiday VALUES('"
								+ c.getString("HolidayId") + "','"
								+ c.getString("HolidayName") + "','"
								+ c.getString("HolidayDate") + "','"
								+ c.getString("Day") + "') ");

						SimpleDateFormat sf = new SimpleDateFormat(
								"dd MMM yyyy");
						String dt = c.getString("HolidayDate");

						if (dt.contains("-")) {

							String[] dts = dt.split("-");
							if (dts.length > 2) {
								int y = Integer.parseInt(dts[0]);
								int m = Integer.parseInt(dts[1]);
								int d = Integer.parseInt(dts[2]);
								m = m - 1;
								y = y - 1900;
								s = sf.format(new Date(y, m, d));
							}
						} else
							s = dt;

						holidaylist.add(new HolidayItem("" + s, c
								.getString("Day"), c.getString("HolidayName")));

					}
					ListView listView = (ListView) getActivity().findViewById(
							R.id.listView1);
					// listView.setAdapter(new ListAdapter(getActivity(),
					// android.R.layout.simple_list_item_1, holidaylist));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				showdata();

		}

	}

}
