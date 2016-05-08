package com.educationam;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParentComplaintsActivity extends Fragment {

	private Button btnRequestForBatch;
	private TextView txtTopcForBatch;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	private dbhandler db;
	private SQLiteDatabase sd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_parent_complaints,
				container, false);

		btnRequestForBatch = (Button) v.findViewById(R.id.btnAddComplaint);
		txtTopcForBatch = (TextView) v.findViewById(R.id.txtComplaint);

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

		super.onActivityCreated(savedInstanceState);

	}

	// ************************************

	class GetBatchRequestFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private JSONArray jarrayremark;
		private String topic;
		private String label;
		private String str_stud_remark;

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

			String url = AllKeys.WEBSITE + "InsertExtraBatch?empid="
					+ userDetails.get(SessionManager.KEY_EMPID) + "&topic="
					+ topic + "&subid=" + label + "&classid="
					+ userDetails.get(SessionManager.KEY_DEPTID) + "&clientid="
					+ AllKeys.CLIENT_ID;
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
			Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_LONG)
					.show();

		}
	}

	// ************************************

}
