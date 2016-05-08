package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.educationam.FragmentHomework.GetHomewrokDetailsFromServer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
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
import android.widget.SimpleAdapter;

public class UpcomingTests extends android.support.v4.app.Fragment {

	ArrayList<HashMap<String, String>> upcomingtestlist = new ArrayList<HashMap<String, String>>();

	public static UpcomingTests create() {
		UpcomingTests f = new UpcomingTests();
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
	private JSONArray jarrayupcoming;
	private String testname, facultyname, date, sub;
	private dbhandler db;
	private SQLiteDatabase sd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.upcoming_tests_frag, container,
				false);

		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();
		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		deptid = userDetails.get(sessionmanager.KEY_DEPTID);

		listview = (ListView) v.findViewById(R.id.listUpcomingTest);

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

		try {
			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();

			Cursor c = sd.rawQuery("select * from upcomingtest", null);
			getActivity().startManagingCursor(c);
			upcomingtestlist.clear();
			while (c.moveToNext()) {
				testname = c.getString(1);
				date = c.getString(2);
				sub = c.getString(3);

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("TESTNAME", testname);
				map.put("DATE", date);
				map.put("SUBJECT", sub);

				upcomingtestlist.add(map);
			}

			SimpleAdapter adapter = new SimpleAdapter(getActivity(),
					upcomingtestlist, R.layout.lstrowappointment, new String[] {
							"TESTNAME", "DATE", "SUBJECT" }, new int[] {
							R.id.lblTestName, R.id.lblTestDate,
							R.id.lblTestSubject });

			listview.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ***************************************

	class GetHomewrokDetailsFromServer extends AsyncTask<String, Void, String> {

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

			String url = AllKeys.WEBSITE
					+ "GetJSONForUpcomingTestDetail?clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID) + "&deptid="
					+ userDetails.get(SessionManager.KEY_DEPTID);
			Log.d("url", url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_t_solution = sh.makeServiceCall(url,
					ServiceHandler.GET);
			if (str_stud_t_solution != null && !str_stud_t_solution.equals("")) {
				try {
					JSONObject json = new JSONObject(str_stud_t_solution);
					if (json != null) {
						try {
							Log.d("UpcomingTest Json data", json.toString());
							// Getting Array of Contacts
							jarrayupcoming = json.getJSONArray("UpcomingTest");
							// looping through All Contacts
							sd.delete("upcomingtest", null, null);
							for (int i = 0; i < jarrayupcoming.length(); i++) {
								JSONObject c = jarrayupcoming.getJSONObject(i);

								sd.execSQL("INSERT INTO upcomingtest VALUES(null,'"
										+ c.getString("TestName")
										+ "','"
										+ c.getString("Subject")
										+ "','"
										+ c.getString("Date") + "') ");

							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("UpcomingTest json error", e.getMessage());
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

	// ***************************************
}
