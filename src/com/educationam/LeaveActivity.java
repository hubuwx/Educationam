package com.educationam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LeaveActivity extends ActionBarActivity {

	private String SELECTEDREASON = "";
	private Button btnfrom;
	private Button btnto;
	private EditText txtreason;
	private Button btnsubmit;
	private Button btnclear;
	private Spinner spnReason;
	String respomse;
	Date d1, d2;
	ArrayList<String> arrType = new ArrayList<String>();
	private SessionManager session;
	private HashMap<String, String> userdetails;
	private dbhandler db;
	private Context context = this;
	private SQLiteDatabase sd;

	private String startDate;
	private int spm;
	private int y;
	private int d;
	private int ty;
	private int tm;
	private int td;
	private String enddate;
	private int m;
	protected String showsdate;
	private String endsdate, type;
	private String AppTitle = "Leave Request";
	private String ThemeColor;

	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID1 = 1112;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave);

		session = new SessionManager(context);
		userdetails = new HashMap<String, String>();

		userdetails = session.getUserDetails();

		type = userdetails.get(SessionManager.KEY_TYPE);

		/*
		 * if (type == "F" || type.equals("F")) { ThemeColor = "#8BC34A"; } else
		 * { ThemeColor = "#FF8C00"; }
		 */

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + AppTitle
						+ "</font>"));

				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();
				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(ThemeColor)));

			} else {
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + AppTitle
						+ "</font>"));
				getActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(ThemeColor)));
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().show();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		db = new dbhandler(context);
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		btnfrom = (Button) findViewById(R.id.btnfrom);
		btnto = (Button) findViewById(R.id.btnto);

		txtreason = (EditText) findViewById(R.id.txtreason);

		btnsubmit = (Button) findViewById(R.id.btnsend);
		btnclear = (Button) findViewById(R.id.btnclear);

		if (type == "F" || type.equals("F")) {
			btnsubmit.setBackgroundColor(Color.rgb(139, 195, 74));
			btnclear.setBackgroundColor(Color.rgb(139, 195, 74));
		} else {
			btnsubmit.setBackgroundColor(Color.rgb(255, 140, 0));
			btnclear.setBackgroundColor(Color.rgb(255, 140, 0));
		}
		spnReason = (Spinner) findViewById(R.id.spntype);

		arrType.clear();
		arrType.add("Select Leave Type");
		arrType.add("Sickness");
		arrType.add("Traveling");
		arrType.add("Family Function");
		arrType.add("Other");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, arrType);

		spnReason.setAdapter(adapter);
		spnReason.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				SELECTEDREASON = arrType.get(arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		final Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DAY_OF_MONTH);

		spm = m + 1;
		if (spm <= 9) {
			String mm = "0" + spm;
			startDate = y + "-" + mm + "-" + d;
			showsdate = d + "-" + mm + "-" + y;
		} else {
			startDate = y + "-" + spm + "-" + d;
			showsdate = d + "-" + spm + "-" + y;
		}

		btnfrom.setText(showsdate);
		ty = cal.get(Calendar.YEAR);
		tm = cal.get(Calendar.MONTH);
		td = cal.get(Calendar.DAY_OF_MONTH);

		spm = tm + 1;
		if (spm <= 9) {
			String mm = "0" + spm;
			enddate = ty + "-" + mm + "-" + td;
			endsdate = td + "-" + mm + "-" + ty;
		} else {
			enddate = ty + "-" + spm + "-" + td;
			endsdate = td + "-" + spm + "-" + ty;
		}
		btnto.setText(endsdate);

		btnfrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID);
			}
		});

		btnto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_ID1);
			}
		});

		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (txtreason.getText().toString().equals("")
						|| txtreason.getText().toString().equals(null)) {
					Toast.makeText(getApplicationContext(),
							"please fill description", Toast.LENGTH_LONG)
							.show();
				} else {
					new SendLeaveRequestToTeacher().execute();
				}

				// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=leave&empid=387&title=leave%20test&desc=leave%20for%20personal%20reason&frmdt=2015-12-24&todt=2015-12-26
				// Send Leave Deatails To server
				Toast.makeText(context, "Leave Details are Sending to server",
						Toast.LENGTH_SHORT).show();

			}
		});

		btnclear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txtreason.setText("");
				spnReason.setSelection(0);

			}
		});

	}

	public String getDateTime(String cur_date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());

		// String string = "January 2, 2010";
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		Date dd = null;
		try {
			dd = format.parse(cur_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dd);

		return dateFormat.format(dd);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.leave, menu); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {

			// moveTaskToBack(true);

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			return new DatePickerDialog(this, pickerListener, y, m, d);
		case DATE_PICKER_ID1:
			return new DatePickerDialog(this, pickerListener1, ty, tm, td);
		}
		return null;

	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			y = selectedYear;
			m = selectedMonth;
			d = selectedDay;

			// Show selected date

			// String startDate;
			int spm = m + 1;
			if (spm <= 9) {
				String mm = "0" + spm;
				startDate = y + "-" + mm + "-" + d;
				showsdate = d + "-" + mm + "-" + y;
			} else {
				startDate = y + "-" + spm + "-" + d;
				showsdate = d + "-" + spm + "-" + y;
			}
			btnfrom.setText(showsdate);

		}
	};

	private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			ty = selectedYear;
			tm = selectedMonth;
			td = selectedDay;

			// Show selected date

			// String startDate;
			int spm = tm + 1;
			if (spm <= 9) {
				String mm = "0" + spm;
				enddate = ty + "-" + mm + "-" + td;
				endsdate = td + "-" + mm + "-" + ty;
			} else {
				enddate = ty + "-" + spm + "-" + td;
				endsdate = td + "-" + spm + "-" + ty;
			}
			btnto.setText(endsdate);
		}
	};

	class SendLeaveRequestToTeacher extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			try {
				pDialog = new ProgressDialog(LeaveActivity.this);
				pDialog.setMessage("Please wait...");
				pDialog.show();
				pDialog.setCancelable(false);
				super.onPreExecute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... urls) {

			JSONParser jParser = new JSONParser();

			String REASON = txtreason.getText().toString();

			REASON = REASON.replace(" ", "%20");
			REASON = REASON.replace("!", "%21");
			REASON = REASON.replace("\n", "%0A");
			REASON = REASON.replace("#", "%23");
			REASON = REASON.replace("$", "%24");
			REASON = REASON.replace("%", "%25");
			REASON = REASON.replace("&", "%26");
			REASON = REASON.replace("'", "%27");
			REASON = REASON.replace("(", "%28");
			REASON = REASON.replace(")", "%29");
			REASON = REASON.replace("-", "%2D");
			REASON = REASON.replace(":", "%3A");
			REASON = REASON.replace(".", "%2E");
			REASON = REASON.replace(";", "%3B");
			REASON = REASON.replace("?", "%3F");
			REASON = REASON.replace("@", "%40");
			SELECTEDREASON = SELECTEDREASON.replace(" ", "%20");

			// getting JSON string from URL=
			// String url = AllKeys.WEBSITE + "JSON_Data.aspx?type=leave&empid="
			// + userdetails.get(SessionManager.KEY_EMPID) + "&title="
			// + SELECTEDREASON + "&desc=" + REASON + "&frmdt="
			// + btnfrom.getText().toString() + "&todt="
			// + btnto.getText().toString() + "";

			String url = AllKeys.WEBSITE2 + "getdata.aspx?type=leave&website="
					+ AllKeys.MAIN_WEB + "&empid="
					+ userdetails.get(SessionManager.KEY_EMPID) + "&title="
					+ SELECTEDREASON + "&desc=" + REASON + "&frmdt="
					+ getDateTime(btnfrom.getText().toString()) + "&todt="
					+ getDateTime(btnto.getText().toString()) + "&branch="
					+ userdetails.get(SessionManager.KEY_BRANCHID);

			Log.d("leave url =>", url);
			// url = AllKeys.WEBSITE+ "json_data.aspx?DeptId=" + "3" +
			// "&type=homework";

			// getDateTime(btnfrom.getText().toString());

			try {
				ServiceHandler sh = new ServiceHandler();
				respomse = sh.makeServiceCall(url, ServiceHandler.GET);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// JSONObject json = jParser.getJSONFromUrl(url);

			ContentValues cv = new ContentValues();
			if (respomse != null) {
				try {
					Log.d("Leave Request Json data", respomse.toString());

				} catch (Exception e) {
					e.printStackTrace();
					Log.d("Leave Request json error", e.getMessage());
				}
			}

			return null;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();
			txtreason.setText("");
			spnReason.setSelection(0);

			AlertDialog.Builder alert = new AlertDialog.Builder(
					LeaveActivity.this);
			alert.setTitle("Leave Info");
			alert.setIcon(R.drawable.ic_launcher);
			alert.setMessage("Leave request has been sending successfully");

			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					// TODO Auto-generated method stub

					Intent ii = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(ii);

				}

			});
			alert.show();

		}

	}

}
