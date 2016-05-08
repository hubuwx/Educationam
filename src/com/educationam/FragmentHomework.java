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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FragmentHomework extends android.support.v4.app.Fragment {

	ArrayList<HomeWork> homeworklista = new ArrayList<HomeWork>();
	ArrayList<HashMap<String, String>> homeworklist = new ArrayList<HashMap<String, String>>();

	public static FragmentHomework create() {
		FragmentHomework f = new FragmentHomework();
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
	private String homeworkname, facultyname, date, sub;
	private dbhandler db;
	private SQLiteDatabase sd;
	ProgressBar pgrShowHomework;
	TextView lblShowTextSync;
	private String type;

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
		pgrShowHomework = (ProgressBar) v.findViewById(R.id.pgrShowHomework);
		lblShowTextSync = (TextView) v.findViewById(R.id.lblSyncTextHomework);

		deptid = userDetails.get(sessionmanager.KEY_DEPTID);

		listview = (ListView) v.findViewById(R.id.listHomework);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (NetConnectivity.isOnline(getActivity())) {
			new GetHomewrokDetailsFromServer().execute("");
		} else {
			FillDataonListView();
		}
	}

	public void FillDataonListView() {

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from homework", null);
		getActivity().startManagingCursor(c);
		homeworklist.clear();
		while (c.moveToNext()) {

			homeworkname = c.getString(1);
			facultyname = c.getString(2);
			date = c.getString(3);
			type = c.getString(4);
			sub = c.getString(6);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("HOMEWORK", homeworkname);
			map.put("FACULTY", "Teacher : " + facultyname);
			map.put("DATE", date);
			map.put("SUBJECT", sub);

			homeworklist.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), homeworklist,
				R.layout.lst_row_single_homework, new String[] { "HOMEWORK",
						"FACULTY", "DATE", "SUBJECT" }, new int[] {
						R.id.lblHomeWOrk, R.id.lblFaculty, R.id.lblDate,
						R.id.lblSubject });

		listview.setAdapter(adapter);
	}

	// ***************************************

	class GetHomewrokDetailsFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// pDialog = new ProgressDialog(getActivity());
			// pDialog.setMessage("Please wait...");
			// pDialog.show();
			// pDialog.setCancelable(false);

			pgrShowHomework.setVisibility(View.VISIBLE);
			lblShowTextSync.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			// url = AllKeys.WEBSITE + "json_data.aspx?DeptId=" + deptid
			// + "&type=homework";

			url = AllKeys.WEBSITE
					+ "GetHomeworkDetailDataByClass?type=homework&deptid="
					+ deptid + "&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			Log.d("url", url);
			// url = AllKeys.WEBSITE+ "json_data.aspx?DeptId=" + "3" +
			// "&type=homework";
			// JSONObject json = jParser.getJSONFromUrl(url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_remark = sh
					.makeServiceCall(url, ServiceHandler.GET);
			ContentValues cv = new ContentValues();
			if (str_stud_remark != null && !str_stud_remark.equals("")) {
				str_stud_remark = "{" + '"' + "HomeworkData" + '"' + ":"
						+ str_stud_remark + "}";
				try {
					JSONObject json = new JSONObject(str_stud_remark);
					if (json != null) {
						try {
							Log.d("Homework Json data", json.toString());
							// Getting Array of Contacts
							jarrayhomework = json.getJSONArray("HomeworkData");

							// looping through All Contacts
							sd.delete("homework", null, null);
							for (int i = 0; i < jarrayhomework.length(); i++) {
								JSONObject c = jarrayhomework.getJSONObject(i);

								sd.execSQL("INSERT INTO homework VALUES(null,'"
										+ c.getString("Homework") + "','"
										+ c.getString("Faculty") + "','"
										+ c.getString("Date") + "','"
										+ c.getString("HomeworkType") + "','"
										+ c.getString("Imagename") + "','"
										+ c.getString("Subject") + "') ");
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Homework json error", e.getMessage());
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
			// pDialog.dismiss();
			// pDialog.cancel();

			pgrShowHomework.setVisibility(View.GONE);
			lblShowTextSync.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);

			FillDataonListView();

		}

	}

	// ***************************************
}
