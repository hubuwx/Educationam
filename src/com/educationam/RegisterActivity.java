package com.educationam;

import java.util.regex.Pattern;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

	private static String TAG_STUD_TABLE = "UserDetail";
	private static String TAG_STUD_ID = "id";
	private static String TAG_STUD_NAME = "Name";
	private static String TAG_STUD_MOBILE = "Mobile";
	private static String TAG_STUD_EMAIL = "Email";
	private static String TAG_STUD_DESC = "Email";

	String user_id;
	EditText studName, studMobile, studEmail, studSchoolname, studDescription;
	Button btnReg;

	private String studNameValue;
	private String studEmailValue;
	private String studMobileValue;
	private String studSchoolValue;
	private String studDescriptionValue;

	private ProgressDialog pDialog;
	JSONArray operators = null;
	private String verificationTextGet;
	ServiceHandler sh;
	private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,100}" + "@"
			+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,10}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,20}" + ")+");
	private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{1,250}");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("[0-9]{1,10}");

	private static final String REQUIRED_MSG = "required";
	private static final String VALID_EMAIL_MSG = "Enter Valid Email";

	private String user_exists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getSupportActionBar().hide();

		studName = (EditText) findViewById(R.id.txtStudentName);
		studMobile = (EditText) findViewById(R.id.txtStudentMobileNo);
		studEmail = (EditText) findViewById(R.id.txtStudentEmail);
		studSchoolname = (EditText) findViewById(R.id.txtStudentSchoolName);
		studDescription = (EditText) findViewById(R.id.txtStudentDescription);
		btnReg = (Button) findViewById(R.id.btnStudentRegister);

		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				studNameValue = studName.getText().toString();
				studMobileValue = studMobile.getText().toString();
				studEmailValue = studEmail.getText().toString();
				studSchoolValue = studSchoolname.getText().toString();
				studDescriptionValue = studDescription.getText().toString();

				// *************************************************

				if (studNameValue.length() == 0) {
					studName.setError(REQUIRED_MSG);

				}
				if (studEmailValue.length() == 0) {
					studEmail.setError(REQUIRED_MSG);

				}
				if (studMobileValue.length() == 0) {
					studMobile.setError(REQUIRED_MSG);

				}
				if (studSchoolValue.length() == 0) {
					studSchoolname.setError(REQUIRED_MSG);

				}

				if (studDescriptionValue.length() == 0) {
					studDescription.setError(REQUIRED_MSG);

				}

				if (!studNameValue.equals("") && !studEmailValue.equals("") && !studEmailValue.equals("")
						&& !studMobileValue.equals("")) {

					if (!CheckEmail(studEmailValue)) {
						studEmail.setError(VALID_EMAIL_MSG);
					} else {

						studName.setText("");
						studEmail.setText("");
						studMobile.setText("");
						studSchoolname.setText("");
						studDescription.setText("");
						new SendingRegistrationDetails().execute();
						// Intent i = new
						// Intent(RegisterActivity.this,LoginActivity.class);
						// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// startActivity(i);
					}
					if (!CheckMobile(studMobileValue)) {
						// studEmail.setError(VALID_MOBILE_MSG);
					}
				}

			}
		});

	}

	// Sending Data
	private class SendingRegistrationDetails extends AsyncTask<Void, Void, Void> {

		Boolean flag = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog

			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Registering...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			// deleteCategoryData();
			// deleteProductData();

			try {

				// http://educationam.dnsitexperts.com/JSON_Data.aspx?type=studinq&name=
				String add = studNameValue + "&mobile=" + studMobileValue + "&email=" + studEmailValue + "&school="
						+ studSchoolValue + "&desc=" + studDescriptionValue;
				Log.d("register =>", add);

				add = add.replace(" ", "%20");
				// add = add.replace("&", "%26");
				add = add.replace("'", "%27");
				add = add.replace("(", "%28");
				add = add.replace(")", "%29");
				add = add.replace("-", "%2D");
				add = add.replace(".", "%2E");
				add = add.replace(":", "%3A");
				add = add.replace(";", "%3B");
				add = add.replace("?", "%3F");
				add = add.replace("@", "%40");
				add = add.replace("_", "%5F");

				String RegisUrl = AllKeys.WEBSITE + "JSON_Data.aspx?type=studinq&name=" + add;

				Log.d("Main Register Url =>", RegisUrl);

				sh = new ServiceHandler();

				String jsonStrSendverifyCode = sh.makeServiceCall(RegisUrl, ServiceHandler.GET);

				Log.d("User: ", "> " + jsonStrSendverifyCode);

				if (jsonStrSendverifyCode != null) {
					try {
						if (jsonStrSendverifyCode == "1" || jsonStrSendverifyCode.equals("1")) {

							flag = true;
						} else {
							flag = false;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			try {
				if (flag == true) {
					Toast.makeText(getApplicationContext(), "Registered Sucessfully", Toast.LENGTH_LONG).show();

					Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);

				} else {
					Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private boolean CheckEmail(String email) {

		return EMAIL_PATTERN.matcher(email).matches();
	}

	private boolean CheckMobile(String password) {

		return MOBILE_PATTERN.matcher(password).matches();
	}

	private boolean CheckUsername(String username) {

		return USERNAME_PATTERN.matcher(username).matches();
	}

}
