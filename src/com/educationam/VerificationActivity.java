package com.educationam;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class VerificationActivity extends ActionBarActivity {

	private ProgressDialog pDialog;

	private String title = "VERIFICATION PROCESS";
	private TextView txtcode;
	private TextView txtverify;
	private TextView txt;
	private TextView txtresend;
	private TextView txterror;
	private Context context = this;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	private int counter;
	private Timer timer;

	dbhandler dh;
	SQLiteDatabase sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title
						+ "</font>"));

				// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				// getSupportActionBar().setHomeButtonEnabled(true);
				// getSupportActionBar().show();
				getSupportActionBar()
						.setBackgroundDrawable(
								new ColorDrawable(Color
										.parseColor(AllKeys.THEME_COLOR)));

			} else {
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title
						+ "</font>"));
				getActionBar()
						.setBackgroundDrawable(
								new ColorDrawable(Color
										.parseColor(AllKeys.THEME_COLOR)));
				// getActionBar().setDisplayHomeAsUpEnabled(true);
				// getActionBar().setHomeButtonEnabled(true);
				// getActionBar().show();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		dh = new dbhandler(getApplicationContext());
		sd = dh.getReadableDatabase();
		sd = dh.getWritableDatabase();
		txtcode = (TextView) findViewById(R.id.txtcode);
		txtverify = (TextView) findViewById(R.id.txtverify);
		txt = (TextView) findViewById(R.id.textView2);
		txtresend = (TextView) findViewById(R.id.txtresend);
		txterror = (TextView) findViewById(R.id.txterror);

		txtcode.setClickable(false);
		txtcode.setDuplicateParentStateEnabled(false);
		// txtcode.setEnabled(false);

		sessionmanager = new SessionManager(context);

		userDetails = new HashMap<String, String>();

		userDetails = sessionmanager.getUserDetails();

		counter = Integer.parseInt(userDetails
				.get(SessionManager.KEY_VERIFICATION_COUNTER));

		if (NetConnectivity.isOnline(context)) {
			timer = new Timer();
			TimerTask hourlyTask = new TimerTask() {
				@Override
				public void run() {
					// your code here...

					userDetails = sessionmanager.getUserDetails();
					String nnn = userDetails
							.get(SessionManager.KEY_RECEIVECODE);
					try {
						userDetails.get(SessionManager.KEY_CODE);

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (userDetails.get(SessionManager.KEY_RECEIVECODE)
							.length() == 4) {
						if (userDetails.get(SessionManager.KEY_RECEIVECODE)
								.equals(userDetails
										.get(SessionManager.KEY_CODE))) {
							try {
								txtcode.setText(""
										+ userDetails
												.get(SessionManager.KEY_RECEIVECODE));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String url = AllKeys.WEBSITE
									+ "json_data.aspx?type=varemp&empid="
									+ userDetails.get(SessionManager.KEY_EMPID)
									+ "&mobile="
									+ userDetails
											.get(SessionManager.KEY_MOBILE)
									+ "&status=1";
							ServiceHandler sh = new ServiceHandler();
							sh.makeServiceCall(url, ServiceHandler.GET);

							sessionmanager.CheckSMSVerificationActivity("",
									"true");

							sessionmanager.createStudentSession(
									userDetails.get(SessionManager.KEY_DEPTID),
									userDetails.get(SessionManager.KEY_EMPID),
									"" + 1);

							timer.cancel();
							timer.purge();

							timer = null;

							Intent intent = new Intent(context,
									MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();

							/*
							 * if(timer != null) { timer.cancel(); timer = null;
							 * }
							 */

						}
					}

				}
			};

			// schedule the task to run starting now and then every hour...
			timer.schedule(hourlyTask, 0l, 1000 * 5); // 1000*10*60 every 10
														// minut
		}

		txtresend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// String msgurl =
				// AllKeys.TAG_WEBSITE+"?action=mobverify&userid="+
				// userDetails.get(SessionManager.KEY_UID) +"&code="+ Vcode +"";

				txterror.setVisibility(View.GONE);

				if (counter >= 3) {
					String dd = ", please mail us on info@paymyreview.com from your registered mail";
					AlertDialogManager2.showAlertDialog(context,
							"Synergii Classes", "Maximum Request Exceeded....",
							false);
				} else {

					new sendSmsToUser().execute();
					/*
					 * Toast.makeText(context, "Please wait...",
					 * Toast.LENGTH_LONG).show(); String sendsmsurl =
					 * userDetails.get(SessionManager.KEY_SENDSMSURL);
					 * 
					 * ServiceHandler sh= new ServiceHandler(); String respones
					 * = sh.makeServiceCall(sendsmsurl, ServiceHandler.GET);
					 * 
					 * if(respones.equals("1")) { //Toast.makeText(context,
					 * "Please wait...", Toast.LENGTH_LONG).show(); } else {
					 * Toast.makeText(context, "Please try again later",
					 * Toast.LENGTH_SHORT).show(); } //new
					 * SendSSMToUser().execute();
					 */}
			}
		});

		if (NetConnectivity.isOnline(context)) {
			new sendSmsToUser().execute();
		} else {
			Toast.makeText(context, "Please enable internet",
					Toast.LENGTH_SHORT).show();
		}

		txt.setText("A verification code is being sent to your mobile number "
				+ userDetails.get(SessionManager.KEY_MOBILE)
				+ ". To verify your mobile number, please enter the code once it  arrives.");

		txtverify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String currentcode = txtcode.getText().toString();
				// CheckVerification(currentcode);

				if (userDetails.get(SessionManager.KEY_CODE)
						.equals(currentcode)) {

					String vv = userDetails.get(SessionManager.KEY_CODE);

					sessionmanager.CheckSMSVerificationActivity("", "true");
					Intent intnet = new Intent(getApplicationContext(),
							MainActivity.class);
					intnet.putExtra("WELCOME",
							"Thanks For Downloading Radiant App");

					startActivity(intnet);
					finish();

				} else {
					Toast.makeText(context, "Invalid code", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.verification, menu); return true; }
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		Toast.makeText(getApplicationContext(), "Please Complete Verification",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getApplicationContext(),
				VerificationActivity.class);

		startActivity(intent);

	}

	private class sendSmsToUser extends AsyncTask<Void, Void, Void> {

		String Error = "";
		private String jsonStr;

		String ans = "";
		String url;
		ServiceHandler sh;
		String sendsms;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(VerificationActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// /Write here statements

			sh = new ServiceHandler();

			if (userDetails.get(SessionManager.KEY_CODE).equals("0")) {
				Random r = new Random();
				int code = r.nextInt(9999 - 1000)+1000;
				Log.d("Verification Code : ", "" + code);
				sendsms ="radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile="
						+ userDetails.get(SessionManager.KEY_MOBILE) + "&code="
						+ code + "";
				sessionmanager.createUserSendSmsUrl("" + code, sendsms);

				ContentValues vl = new ContentValues();
				vl.put("otp", code);
				vl.put("stdid", userDetails.get(SessionManager.KEY_EMPID));
				Log.d("otp", vl.toString());
				sd.insert("verificaton", null, vl);

			} else {
				userDetails = sessionmanager.getUserDetails();
				sendsms = userDetails.get(SessionManager.KEY_SMSURL);
			}

			Log.d("sendsms res : ", "" + sendsms);
			String resp = sh.makeServiceCall(sendsms, ServiceHandler.GET);
			Log.d("sendsms res : ", "" + resp);

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.cancel();

			// Write statement after background process execution
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		pDialog.dismiss();

	}

}
