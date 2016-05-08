package com.educationam;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.educationam.FragmentRemark.GetRemarkDetailsFromServer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BatchRequestActivity extends Fragment {
	TextView lblBackForBatch, txtTopcForBatch;
	Spinner spnBatchSelect;
	dbhandler db;
	String str_stud_remark;
	int label;
	String topic;
	SQLiteDatabase sd;
	Button btnRequestForBatch;
	String status;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	public static BatchRequestActivity create() {
		BatchRequestActivity f = new BatchRequestActivity();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_batch_request, container, false);

		btnRequestForBatch = (Button) v.findViewById(R.id.btnRequestForBatch);
		txtTopcForBatch = (TextView) v.findViewById(R.id.txtTopcForBatch);
		spnBatchSelect = (Spinner) v.findViewById(R.id.spnBatchSelect);
		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();
		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		loadSpinnerData();

		btnRequestForBatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				topic = txtTopcForBatch.getText().toString();
				if (NetConnectivity.isOnline(getActivity())) {
					new GetBatchRequestFromServer().execute("");
				} else {

					Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
				}

			}
		});
		spnBatchSelect.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

				label = spnBatchSelect.getSelectedItemPosition();
				// parent.getItemAtPosition(position).toString();
				Toast.makeText(getActivity(), "You selected: " + label, Toast.LENGTH_LONG).show();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void loadSpinnerData() {
		// database handler

		// Spinner Drop down elements
		List<String> lables = db.getAllLabels();
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				lables);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spnBatchSelect.setAdapter(dataAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// ************************************

	class GetBatchRequestFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private JSONArray jarrayremark;

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

			String url = AllKeys.WEBSITE + "InsertExtraBatch?empid=" + userDetails.get(SessionManager.KEY_EMPID)
					+ "&topic=" + topic + "&subid=" + label + "&classid=" + userDetails.get(SessionManager.KEY_DEPTID)
					+ "&clientid=" + AllKeys.CLIENT_ID;
			ServiceHandler sh = new ServiceHandler();
			str_stud_remark = sh.makeServiceCall(url, ServiceHandler.GET);

			return null;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();

			txtTopcForBatch.setText("");
			Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_LONG).show();

		}
	}

	// ************************************

}
