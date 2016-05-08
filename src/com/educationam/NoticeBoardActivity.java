package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.os.Build;

public class NoticeBoardActivity extends Fragment {

	public static NoticeBoardActivity create() {
		NoticeBoardActivity f = new NoticeBoardActivity();
		return f;
	}

	private ExpandableListView mExpandableList;
	ArrayList<Parent> arrayParents = new ArrayList<Parent>();
	ArrayList<String> arrayChildren = new ArrayList<String>();
	String[] notice = { "notice1", "notice2", "notice3", "notice4", "notice5" };
	private HashMap<String, String> userDetails;
	private SessionManager sessionmanager;
	private String deptid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_notice_board, container, false);

		// listview=(ListView)v.findViewById(R.id.listView1);
		mExpandableList = (ExpandableListView) v.findViewById(R.id.expandableListView1);
		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();
		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();

		deptid = userDetails.get(sessionmanager.KEY_DEPTID);
		// add data to ListView
		ConnectionDetector cd = new ConnectionDetector(getActivity());

		if (cd.isConnectingToInternet() == true) {
			new DownloadWebPageTask().execute("");

		} else {
			populatelist();

		}

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public void populatelist() {
		arrayParents.clear();
		Parent parent = new Parent();

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select * from noticeboard", null);
		getActivity().startManagingCursor(c);
		while (c.moveToNext()) {
			try {
				parent = new Parent();
				String n1 = c.getString(0);
				String n2 = c.getString(3);
				parent.setTitle(c.getString(1));
				parent.setArrayChildren(arrayChildren);
				// arrayParents.add(parent);
				// arrparentrn.add(rn);
				// int row=0;
				arrayChildren = new ArrayList<String>();
				arrayChildren.clear();
				String qqr = "select * from noticeboard where id=" + c.getString(0);
				Cursor c1 = sd.rawQuery("select * from noticeboard where id=" + c.getString(0), null);
				getActivity().startManagingCursor(c1);
				while (c1.moveToNext()) {

					String n = c1.getString(1);
					arrayChildren.add("" + c1.getString(2));
				}
				parent.setArrayChildren(arrayChildren);

				// in this array we add the Parent object. We will use the
				// arrayParents at the setAdapter
				arrayParents.add(parent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private JSONArray jarrayholiday;
		private String url;

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

			String qry = "Select * from DataUrl";
			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();
			Cursor cdata = sd.rawQuery(qry, null);
			getActivity().startManagingCursor(cdata);
			while (cdata.moveToNext()) {
				url1 = cdata.getString(0);
			}
			// JSONParser jParser = new JSONParser();
			// getting JSON string from URL
			url = AllKeys.WEBSITE + "GetNoticeBoardsDataForClass?type=NoticeBoard&classid=" + deptid + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch=" + userDetails.get(SessionManager.KEY_BRANCHID);
			// url = url1 + "JSON_data.aspx?type=NoticeBoard";
			ServiceHandler sh = new ServiceHandler();
			String str_stud_remark = sh.makeServiceCall(url, ServiceHandler.GET);

			if (str_stud_remark != null && !str_stud_remark.equals("")) {

				str_stud_remark = "{" + '"' + "NoticeBoard" + '"' + ":" + str_stud_remark + "}";

				try {
					JSONObject json = new JSONObject(str_stud_remark);
					// JSONObject json = jParser.getJSONFromUrl(url);

					if (json != null) {
						try {
							// Getting Array of Contacts
							jarrayholiday = json.getJSONArray("NoticeBoard");

							// looping through All Contacts
							sd.delete("noticeboard", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO noticeboard VALUES('" + c.getString("Id") + "','"
										+ c.getString("ShortDescription") + "','" + c.getString("LongDescription")
										+ "','" + c.getString("Date") + "','" + c.getString("IsShown") + "') ");

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

			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			populatelist();
			mExpandableList.setAdapter(new ExpandableListAdapter(getActivity(), arrayParents));

			pDialog.dismiss();
			pDialog.cancel();

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}
