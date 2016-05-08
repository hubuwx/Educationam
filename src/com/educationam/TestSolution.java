package com.educationam;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TestSolution extends Fragment {
	ArrayList<ListItem> lsttestsolution = new ArrayList<ListItem>();
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
	private JSONArray jarrayholiday;
	private String s;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	dbhandler db;
	SQLiteDatabase sd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_test_solution, container,
				false);

		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();
		listview = (ListView) v.findViewById(R.id.listView1);

		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// add data to ListView
		ConnectionDetector cd = new ConnectionDetector(getActivity());

		if (cd.isConnectingToInternet() == true) {

			new ProgressTask().execute("");

		} else {

			getallData();

			listview.setAdapter(new CustomAdapter(getActivity(),
					android.R.layout.simple_list_item_1, lsttestsolution));

		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String data = arrTitle.get(position) + " "
						+ arrPath.get(position);
				// Toast.makeText(getActivity(), "Data :"+data,
				// Toast.LENGTH_SHORT).show();

				Intent i = new Intent(getActivity(), ShowTestSolution.class);

				i.putExtra("title", "" + arrTitle.get(position));

				i.putExtra("solutionpath", "" + arrPath.get(position));
				i.putExtra("visibility", "0");

				startActivity(i);
			}
		});

	}

	public void getallData() {

		try {
			c = sd.rawQuery("SELECT * FROM  testsolution Order by id DESC",
					null);
			lsttestsolution.clear();
			arr.clear();
			arrPath.clear();
			arrTitle.clear();
			while (c.moveToNext()) {

				lsttestsolution.add(new ListItem(c.getString(2), ""));
				arr.add("" + c.getInt(0));
				arrPath.add("" + c.getString(1));
				arrTitle.add("" + c.getString(2));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class ProgressTask extends AsyncTask<String, Integer, String> {
		private ProgressDialog progressDialog;
		private String url1;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Test Solutions Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = AllKeys.WEBSITE
					+ "GetJSONForTestSolution?type=testsolution&deptid="
					+ userDetails.get(SessionManager.KEY_DEPTID) + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			Log.d("test solution", url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_t_solution = sh.makeServiceCall(url,
					ServiceHandler.GET);

			ContentValues cv = new ContentValues();
			if (str_stud_t_solution != null && !str_stud_t_solution.equals("")) {

				// JSONObject jsonnotes = jParser1.getJSONFromUrl(url);
				try {

					JSONObject jsonnotes = new JSONObject(str_stud_t_solution);

					if (jsonnotes != null) {
						try {
							// Getting Array of Contacts
							jarrayholiday = jsonnotes
									.getJSONArray("TestSolution");
							sd.delete("testsolution", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO testsolution VALUES(null,'"
										+ c.getString("File")
										+ "','"
										+ c.getString("TestName") + "') ");
								lsttestsolution.add(new ListItem(""
										+ c.getString("TestName"), s));
								// arr.add(c.getString("NoteId"));
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

			getallData();

			listview.setAdapter(new CustomAdapter(getActivity(),
					android.R.layout.simple_list_item_1, lsttestsolution));

			progressDialog.dismiss();

		}
	}

}
