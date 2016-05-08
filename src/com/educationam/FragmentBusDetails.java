package com.educationam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

public class FragmentBusDetails extends Fragment {

	private Context context = getActivity();
	private ListView lvbus;
	private dbhandler db;
	private SQLiteDatabase sd;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	private ArrayList<HashMap<String, String>> allbuses = new ArrayList<HashMap<String, String>>();
	private String empid;

	public FragmentBusDetails() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bustdetails,
				container, false);

		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();
		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();
		lvbus = (ListView) rootView.findViewById(R.id.lvbus);

		empid = userDetails.get(sessionmanager.KEY_EMPID);
		if (NetConnectivity.isOnline(getActivity())) {
			new GetBusDetailsFromServer().execute();
		} else {
			FillDataonListView();
		}

		// Inflate the layout for this fragment
		return rootView;
	}

	private void FillDataonListView() {
		// TODO Auto-generated method stub

		try {
			String q = "select * from busmst";
			Cursor c = sd.rawQuery(q, null);
			if (c != null) {
				allbuses.clear();
				while (c.moveToNext()) {

					HashMap<String, String> busdetails = new HashMap<String, String>();
					busdetails.put("DRIVERNAME", c.getString(2));
					busdetails.put("VEHICLENO", c.getString(3));
					busdetails.put("MOBILENO", c.getString(6));
					busdetails.put("BUSNAME", c.getString(1));
					busdetails.put("BUS INCHARGE", c.getString(4));
					busdetails.put("INCHARGE NO", c.getString(5));
					allbuses.add(busdetails);

				}
			}

			SimpleAdapter adapter = new SimpleAdapter(getActivity(), allbuses,
					R.layout.lst_row_single_bus, new String[] { "DRIVERNAME",
							"VEHICLENO", "MOBILENO", "BUSNAME", "BUS INCHARGE",
							"INCHARGE NO" }, new int[] { R.id.txtdriver,
							R.id.txtvehicleno, R.id.txtmobile, R.id.txtbusname,
							R.id.txtBusincharge, R.id.txtInchargeNo });
			lvbus.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Error in FillBusses : ", e.getMessage());
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	class GetBusDetailsFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private String urls;
		private String url;
		private JSONObject json6;
		private JSONArray jarrayholiday;

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

			// url = AllKeys.WEBSITE + "json_data.aspx?type=busdetail&empid="
			// + empid;

			url = AllKeys.WEBSITE+"GetStudentBusDetail?type=busdetail&empid="
					+ empid + "&clientid="+AllKeys.CLIENT_ID+"&branch="+userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_remark = sh
					.makeServiceCall(url, ServiceHandler.GET);
			if (str_stud_remark != null && !str_stud_remark.equals("")) {

				str_stud_remark = "{" + '"' + "BusDetail" + '"' + ":"
						+ str_stud_remark + "}";
				try {
					JSONObject jsonBus = new JSONObject(str_stud_remark);
					if (jsonBus != null) {
						Log.d("Bus Json : ", jsonBus.toString());
						try {

							// Getting Array of Contacts

							jarrayholiday = jsonBus.getJSONArray("BusDetail");

							// looping through All Contacts
							sd.delete("busmst", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO busmst VALUES('"
										+ c.getString("BusCode") + "','"
										+ c.getString("BusName") + "','"
										+ c.getString("DriverName") + "','"
										+ c.getString("VehicleNo") + "','"
										+ c.getString("BusIncharge") + "','"
										+ c.getString("InchargeMobile") + "','"
										+ c.getString("MobileNo") + "') ");

							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Bus Json Error : ", e.getMessage());
						}
					} else {
						Log.d("Bus json", "No Data Found of bus details");
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
			pDialog.dismiss();
			pDialog.cancel();

			FillDataonListView();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

}