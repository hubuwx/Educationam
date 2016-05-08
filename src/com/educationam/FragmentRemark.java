package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FragmentRemark extends android.support.v4.app.Fragment {

	ArrayList<HomeWork> homeworklista = new ArrayList<HomeWork>();
	ArrayList<HashMap<String, String>> homeworklist = new ArrayList<HashMap<String, String>>();

	public static FragmentRemark create() {
		FragmentRemark f = new FragmentRemark();
		return f;
	}

	Activity a;
	private String deptid;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	private ListView listview;
	Menu mymenu;
	private Cursor c;
	private String urls;
	private String url;
	private JSONArray jarrayremark;
	private String remark, facultyname, date;
	private dbhandler db;
	private SQLiteDatabase sd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		try {
			if (mymenu != null) {

				mymenu.findItem(R.id.action_sync).setVisible(false);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Error in diable option menu", e.getMessage());
		}
	}

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
			new GetRemarkDetailsFromServer().execute("");
		} else {
			FillDataonListView();
		}

		return v;
	}

	public void FillDataonListView() {

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from remarkmst", null);
		getActivity().startManagingCursor(c);
		homeworklist.clear();
		while (c.moveToNext()) {

			remark = c.getString(3);
			facultyname = c.getString(2);
			date = c.getString(1);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("REMARK", remark);
			map.put("FACULTY", "Teacher : " + facultyname);
			map.put("DATE", date);

			homeworklist.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), homeworklist,
				R.layout.lst_row_single_homework, new String[] { "REMARK",
						"FACULTY", "DATE" }, new int[] { R.id.lblHomeWOrk,
						R.id.lblFaculty, R.id.lblDate });

		listview.setAdapter(adapter);

	}

	// ***************************************

	class GetRemarkDetailsFromServer extends AsyncTask<String, Void, String> {

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
			// url = AllKeys.WEBSITE+ "json_data.aspx?StudId=" + deptid +
			// "&type=studremark";

			// url = AllKeys.WEBSITE + "json_data.aspx?StudId="
			// + userDetails.get(SessionManager.KEY_EMPID)
			// + "&type=studremark";

			url = AllKeys.WEBSITE
					+ "GetStudRemarkDetailByStudData?type=studremark&studid="
					+ userDetails.get(SessionManager.KEY_EMPID) + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(sessionmanager.KEY_BRANCHID);
			Log.d("remark", url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_remark = sh
					.makeServiceCall(url, ServiceHandler.GET);

			ContentValues cv = new ContentValues();
			if (str_stud_remark != null && !str_stud_remark.equals("")) {

				str_stud_remark = "{" + '"' + "StudRemarks" + '"' + ":"
						+ str_stud_remark + "}";

				try {
					JSONObject json = new JSONObject(str_stud_remark);

					if (json != null) {
						try {
							Log.d("Remark Json data", json.toString());
							// Getting Array of Contacts
							jarrayremark = json.getJSONArray("StudRemarks");

							// looping through All Contacts
							sd.delete("remarkmst", null, null);
							for (int i = 0; i < jarrayremark.length(); i++) {
								JSONObject c = jarrayremark.getJSONObject(i);

								sd.execSQL("INSERT INTO remarkmst VALUES(null,'"
										+ c.getString("Date")
										+ "','"
										+ c.getString("FacultyID")
										+ "','"
										+ c.getString("Remark") + "') ");
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Remark json error", e.getMessage());
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

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();

			FillDataonListView();

		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.mymenu = menu;
		inflater.inflate(R.menu.login, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	// ***************************************
}
