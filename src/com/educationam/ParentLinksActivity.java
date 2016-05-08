package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ParentLinksActivity extends ActionBarActivity {
	private ProgressDialog pDialog;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	String url;
	private static final String TAG_Parnet_Table = "Emagazine";
	private static final String TAG_Parent_ID = "Id";
	private static final String TAG_Parent_Title = "Title";
	private static final String TAG_Parent_Link = "PLink";
	private TextView lblBack;
	JSONArray parentlinks = null;
	ArrayList<HashMap<String, String>> parentList;
	ListView list;
	String parentlink_name_get;
	String deptid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent_links);
		getSupportActionBar().hide();
		sessionmanager = new SessionManager(getApplicationContext());
		userDetails = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();
		lblBack = (TextView) findViewById(R.id.lblBackParent);

		deptid = userDetails.get(SessionManager.KEY_DEPTID);
		lblBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ParentLinksActivity.this,
						MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

			}
		});

		parentList = new ArrayList<HashMap<String, String>>();
		list = (ListView) findViewById(R.id.listParentLink);

		new GetParentLinks().execute();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				parentlink_name_get = ((TextView) arg1
						.findViewById(R.id.ParenLinkText)).getText().toString();

				Intent i = new Intent(ParentLinksActivity.this,
						ParentLinksWebActivity.class);
				i.putExtra("parent", parentlink_name_get);
				startActivity(i);

			}
		});
	}

	private class GetParentLinks extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ParentLinksActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			url = AllKeys.WEBSITE
					+ "GetParentingLinks?type=ParentingLinks&classid=0&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			Log.d("links", url);
			ServiceHandler sh1 = new ServiceHandler();
			String str_stud_links = sh1
					.makeServiceCall(url, ServiceHandler.GET);

			ContentValues cv = new ContentValues();
			if (str_stud_links != null && !str_stud_links.equals("")) {

				str_stud_links = "{" + '"' + "Emagazine" + '"' + ":"
						+ str_stud_links + "}";
				// Making a request to url and getting response
				try {
					JSONObject json = new JSONObject(str_stud_links);

					if (json != null) {
						try {

							// Getting JSON Array node
							parentlinks = json.getJSONArray(TAG_Parnet_Table);

							// looping through All Contacts
							for (int i = 0; i < parentlinks.length(); i++) {
								JSONObject c = parentlinks.getJSONObject(i);

								String id = c.getString(TAG_Parent_ID);
								String plink = c.getString(TAG_Parent_Link);
								String ptitle = c.getString(TAG_Parent_Title);

								// tmp hashmap for single contact
								HashMap<String, String> parent = new HashMap<String, String>();

								// adding each child node to HashMap key =>
								// value
								parent.put("id", id);
								parent.put("plink", plink);
								parent.put("ptitle", ptitle);

								// adding contact to contact list
								parentList.add(parent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			try {
				ListAdapter adapter = new SimpleAdapter(
						ParentLinksActivity.this, parentList,
						R.layout.parentlinks_list_item, new String[] { "id",
								"plink", "ptitle" }, new int[] {
								R.id.ParenLinkIdText, R.id.ParenLinkText,
								R.id.ParenLinkNameText });

				list.setAdapter(adapter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
