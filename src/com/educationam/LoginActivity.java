package com.educationam;

import static com.educationam.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.educationam.CommonUtilities.EXTRA_MESSAGE;
import static com.educationam.CommonUtilities.SENDER_ID;
import static com.educationam.CommonUtilities.SERVER_URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends ActionBarActivity {

	private EditText txtUsername;
	private EditText txtPassword;
	private JSONObject json2;
	private String deptid, stdname, branchid, type;
	private String empid, actstatus, verdtatus = "0";
	private Button btnLogin;
	AlertDialogManager adm = new AlertDialogManager();
	private Context context = this;
	static String deptids, empids = "";
	private boolean flag;
	private ArrayList<String> lstMessage = new ArrayList<String>();
	HashMap<String, String> userDetails;
	SessionManager session;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	private int maxrec;
	TextView txtRegister;

	private int TOTALDEPTS;
	String USERMOBILE;
	static String MOBILE;
	dbhandler db;
	SQLiteDatabase sd;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		getSupportActionBar().hide();

		session = new SessionManager(context);

		userDetails = new HashMap<String, String>();
		userDetails = session.getUserDetails();
		txtUsername = (EditText) findViewById(R.id.editText1);
		txtPassword = (EditText) findViewById(R.id.editText2);
		txtPassword.setText("" + AllKeys.WEBSITE2);
		txtPassword.setEnabled(false);

		txtPassword.setTextColor(Color.parseColor("#000000"));

		// txtUsername.setText("9825681802");// 9825159860

		btnLogin = (Button) findViewById(R.id.button1);
		txtRegister = (TextView) findViewById(R.id.txtRegister);
		// imgview=(ImageView)findViewById(R.id.imageAnimation);
		txtRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(i);

			}
		});

		db = new dbhandler(context);
		sd = db.getReadableDatabase();

		Cursor c = sd.rawQuery("select count(*) from studinfo", null);
		startManagingCursor(c);
		while (c.moveToNext()) {
			maxrec = c.getInt(0);
		}
		if (maxrec == 0) {

		} else if (maxrec == 1) {
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
			finish();
		} else {

			Intent intent = new Intent(context, MultipleClasses.class);
			intent.putExtra("USERMOBILE", "" + USERMOBILE);

			startActivity(intent);
			finish();
		}

		ConnectionDetector cd = new ConnectionDetector(context);
		AlertDialogManager alert = new AlertDialogManager();

		if (cd.isConnectingToInternet() == false) {
			alert.showAlertDialog(context, "Internet",
					"Please Enable your Internet Connection", false);
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(context, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			return;
		}

		MOBILE = txtUsername.getText().toString();
		btnLogin.setOnClickListener(new OnClickListener() {

			private JSONParser jparser;
			private String url, urlkk;
			private JSONObject json = null;
			private JSONArray jarrayholiday;
			private String url1;
			private boolean flag;
			private String depturl, urlcontact;
			private String urllogin;
			private JSONObject jsonlogin;
			String str_stud_login;
			private String urltestResult;

			@Override
			public void onClick(View v) {

				// Typecasting the Animation Drawable

				USERMOBILE = txtUsername.getText().toString();
				if (txtUsername.getText().toString().equals("")
						|| txtPassword.getText().toString().equals("")) {
					adm.showAlertDialog(context, "Login Info",
							"Please Enter Username or Password", false);
				} else {

					if (txtUsername.getText().toString().equals("admin")
							&& txtPassword.getText().toString().equals("admin")) {
						String qry = "Select * from DataUrl";
						Cursor cdata = sd.rawQuery(qry, null);
						startManagingCursor(cdata);
						while (cdata.moveToNext()) {
							url1 = cdata.getString(0);
						}
						// ********
						Intent intent = new Intent(context,
								AdminNotesActivity.class);
						startActivity(intent);
						finish();
					} else {

						url1 = "http://" + txtPassword.getText().toString()
								+ "/";
						sd.delete("dataurl", null, null);
						sd.execSQL("INSERT INTO dataurl values('" + url1 + "')");
						JSONParser jparser = new JSONParser();

						// String currentDateTimeString =
						// DateFormat.getDateTimeInstance().format(new Date());

						// url = url1 + "JSON_Data.aspx?MobileNo="
						// + txtUsername.getText().toString() + "";

						ConnectionDetector cd = new ConnectionDetector(context);
						AlertDialogManager alert = new AlertDialogManager();

						if (cd.isConnectingToInternet() == true) {

							// ************************
							urllogin = AllKeys.WEBSITE
									+ "GetLoginForParents?MobileNo="
									+ txtUsername.getText().toString()
									+ "&clientid="
									+ AllKeys.CLIENT_ID
									+ "&branch="
									+ userDetails
											.get(SessionManager.KEY_BRANCHID);

							Log.d("login url =>", urllogin);
							try {

								ServiceHandler shlogin = new ServiceHandler();
								str_stud_login = shlogin.makeServiceCall(
										urllogin, ServiceHandler.GET);
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							if (str_stud_login != null
									&& !str_stud_login.equals("")) {

								str_stud_login = "{" + '"' + "Login" + '"'
										+ ":" + str_stud_login + "}";
							}

							try {
								jsonlogin = new JSONObject(str_stud_login);
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							// *************************
							// JSONObject jsons =
							// jparser.getJSONFromUrl(url);
							if (jsonlogin == null) {
								adm.showAlertDialog(context, "Information",
										"Invalid Usernames or Password", false);

							} else {

								// json=jparser.getJSONFromUrl(url);

								try {

									JSONArray jarray = jsonlogin
											.getJSONArray("Login");
									sd.delete("studinfo", null, null);
									sd.execSQL("create table if not exists mobile(phone text,name text)");
									sd.delete("mobile", null, null);
									sd.delete("DeptMaster", null, null);
									TOTALDEPTS = jarray.length();

									for (int i = 0; i < jarray.length(); i++) {
										JSONObject jobj = jarray
												.getJSONObject(i);
										deptid = jobj.getString("DeptId");
										empid = jobj.getString("EmployeeId");
										verdtatus = jobj
												.getString("VerificationStatus");
										actstatus = jobj
												.getString("ActivationStatus");
										stdname = jobj.getString("Studentname");
										branchid = jobj.getString("BranchId");
										type = jobj.getString("Type");
										session.createBranch(branchid);
										session.createLoginUserTypeSession(type);
										session.createStudentNameSession(stdname);
										session.createStudentSession(deptid,
												empid, actstatus);
										sd.execSQL("insert into studinfo values("
												+ jobj.getString("DeptId")
												+ ",'"
												+ jobj.getString("EmployeeId")
												+ "','"
												+ jobj.getString("Studentname")
												+ "','"
												+ jobj.getString("BranchId")
												+ "','"
												+ jobj.getString("DeptName")
												+ "','"
												+ jobj.getString("Type")

												+ "')");
										sd.execSQL("insert into mobile values('"
												+ txtUsername.getText()
														.toString()
												+ "','"
												+ jobj.getString("Studentname")
												+ "')");
									}
									JSONParser jParser = new JSONParser();

									// ********************************

									urltestResult = AllKeys.WEBSITE
											+ "GetJSONForTestDetail?type=TDD&DeptId="
											+ deptid
											+ "&EmpId="
											+ empid
											+ "&trsrno=1&clientid="
											+ AllKeys.CLIENT_ID
											+ "&branch="
											+ userDetails
													.get(SessionManager.KEY_BRANCHID);

									Log.d("TDD =>", urltestResult);
									// url = url1 +
									// "json_data.aspx?type=TDD&DeptId=" +
									// deptid +
									// "&EmpId="
									// + empid;

									ServiceHandler shTDD = new ServiceHandler();
									String str_stud_TDD = shTDD
											.makeServiceCall(
													(String) urltestResult,
													ServiceHandler.GET);
									if (str_stud_TDD != null
											&& !str_stud_TDD.equals("")) {

										try {
											try {
												JSONObject json = new JSONObject(
														str_stud_TDD);
												if (json != null) {
													try {
														// Getting Array of
														// Contacts
														jarrayholiday = json
																.getJSONArray("Test");

														// looping through All
														// Contacts
														sd.delete("testdetail",
																null, null);
														for (int i = 0; i < jarrayholiday
																.length(); i++) {
															JSONObject c = jarrayholiday
																	.getJSONObject(i);

															sd.execSQL("INSERT INTO testdetail VALUES('"
																	+ c.getString("TestId")
																	+ "','"
																	+ c.getString("StudId")
																	+ "','"
																	+ c.getString("StudName")
																	+ "','"
																	+ c.getString("SubId")
																	+ "','"
																	+ c.getString("Marks")
																	+ "','"
																	+ c.getString("Percentage")
																	+ "','"
																	+ c.getString("HighestMks")
																	+ "','"
																	+ c.getString("DeptId")
																	+ "') ");

														}

													} catch (JSONException e) {

														e.printStackTrace();
													}
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									// ********************************

									// getting JSON string from URL
									// urlkk="http://panel.dnsitexperts.com/serviceP.asmx/GetJSONForContactUsData?type=contact&clientid=36";
									urlkk = AllKeys.WEBSITE
											+ "GetJSONForContactUsData?type=contact&clientid="
											+ "45";
									ServiceHandler shcon = new ServiceHandler();
									String str_stud_contact = shcon
											.makeServiceCall(urlkk,
													ServiceHandler.GET);

									if (str_stud_contact != null
											&& !str_stud_contact.equals("")) {

										str_stud_contact = "{" + '"'
												+ "Contact" + '"' + ":"
												+ str_stud_contact + "}";
										// urlkk = AllKeys.WEBSITE
										// + "json_data.aspx?type=contact";
										try {
											JSONObject jsoncontact = new JSONObject(
													str_stud_contact);
											// json2 =
											// jParser.getJSONFromUrl(urlkk);

											if (jsoncontact != null) {
												try {
													// Getting Array of
													// Contacts
													jarrayholiday = jsoncontact
															.getJSONArray("Contact");

													// looping through All
													// Contacts
													// sd.delete("submaster",
													// null,
													// null);
													for (int i = 0; i < jarrayholiday
															.length(); i++) {
														JSONObject c = jarrayholiday
																.getJSONObject(i);

														String lat = c
																.getString("Latitude");
														String lon = c
																.getString("Longitude");
														String add = c
																.getString("Address :"
																		+ "Address");
														String mob = c
																.getString("Mobile No :"
																		+ "MobileNo");
														String email = c
																.getString("Email : "
																		+ "Email");

														session.createContactUsDetails(
																""
																		+ c.getString("Latitude"),
																""
																		+ c.getString("Longitude"),
																""
																		+ c.getString("Address")
																		+ ",,Mobile No : "
																		+ c.getString("MobileNo")
																		+ ",,Phone No : "
																		+ c.getString("PhoneNo")
																		+ ",,Email : "
																		+ c.getString("Email"));

														session.createContactUsDetails(
																lat, lon, add
																		+ " "
																		+ mob
																		+ " "
																		+ email);
														// sd.execSQL("INSERT
														// INTO submaster
														// VALUES('"+c.getString("code")+"','"+c.getString("name")+"')
														// ");
													}

												} catch (JSONException e) {
													e.printStackTrace();
													Log.d("contact json error",
															e.getMessage());
												}
											}
										} catch (Exception e) {
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}
									}
									// getting JSON string from URL

									deptids = deptid;
									empids = empid;
									session.createUserMobile(USERMOBILE);
									if (TOTALDEPTS == 1) {

										new DownloadWebPageTask().execute("");
									} else {
										Intent mul = new Intent(context,
												MultipleClasses.class);
										mul.putExtra("USERMOBILE", ""
												+ USERMOBILE);
										startActivity(mul);
										finish();
									}
									// Make sure the device has the proper
									// dependencies.
									GCMRegistrar
											.checkDevice(getApplicationContext());

									// Make sure the manifest was properly
									// set -
									// comment out this line
									// while developing the app, then
									// uncomment
									// it when it's ready.
									// GCMRegistrar.checkManifest(getApplicationContext());

									// lblMessage = (TextView)
									// findViewById(R.id.lblMessage);

									registerReceiver(mHandleMessageReceiver,
											new IntentFilter(
													DISPLAY_MESSAGE_ACTION));

									// Get GCM registration id
									final String regId = GCMRegistrar
											.getRegistrationId(context);

									// Check if regid already presents
									if (regId.equals("")) {
										// Registration is not present,
										// register
										// now with GCM
										GCMRegistrar.register(
												getApplicationContext(),
												SENDER_ID);
									} else {
										// Device is already registered on
										// GCM
										if (GCMRegistrar
												.isRegisteredOnServer(getApplicationContext())) {

											mRegisterTask = new AsyncTask<Void, Void, Void>() {

												private String response;

												@Override
												protected Void doInBackground(
														Void... params) {
													// Register on our
													// server
													// On server creates a
													// new
													// user
													ServerUtilities
															.register(
																	context,
																	txtUsername
																			.getText()
																			.toString(),
																	regId);
													return null;
												}

												@Override
												protected void onPostExecute(
														Void result) {
													mRegisterTask = null;
												}

											};
											mRegisterTask.execute(null, null,
													null);

											// Skips registration.
											// Toast.makeText(getApplicationContext(),
											// "Already registered with GCM",
											// Toast.LENGTH_LONG).show();
										} else {
											// Try to register again, but
											// not in
											// the UI thread.
											// It's also necessary to cancel
											// the
											// thread onDestroy(),
											// hence the use of AsyncTask
											// instead of a raw thread.

											mRegisterTask = new AsyncTask<Void, Void, Void>() {

												private String response;

												@Override
												protected Void doInBackground(
														Void... params) {
													// Register on our
													// server
													// On server creates a
													// new
													// user
													ServerUtilities
															.register(
																	context,
																	txtUsername
																			.getText()
																			.toString(),
																	regId);
													return null;
												}

												@Override
												protected void onPostExecute(
														Void result) {
													mRegisterTask = null;
												}

											};
											mRegisterTask.execute(null, null,
													null);
										}
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}

								// /////Getting Standard Details of Current
								// Institute ////////
								// depturl = url1 +
								// "JSON_Data.aspx?type=Dept";
								depturl = AllKeys.WEBSITE
										+ "ServiceP.asmx/GetStandardsData?type=standard&clientid="
										+ AllKeys.CLIENT_ID
										+ "&branch="
										+ userDetails
												.get(SessionManager.KEY_BRANCHID);

								Log.d("depturl", depturl);
								// JSONObject jsonDept = jparser
								// .getJSONFromUrl(depturl);

								ServiceHandler sh = new ServiceHandler();
								String str_stud_remark = sh.makeServiceCall(
										depturl, ServiceHandler.GET);
								if (str_stud_remark != null
										&& !str_stud_remark.equals("")) {

									str_stud_remark = "{" + '"' + "Dept" + '"'
											+ ":" + str_stud_remark + "}";
									try {
										JSONObject jsonDept = new JSONObject(
												str_stud_remark);
										if (jsonDept != null) {
											try {
												JSONArray jarray = jsonDept
														.getJSONArray("Dept");

												for (int i = 0; i < jarray
														.length(); i++) {
													JSONObject jobj = jarray
															.getJSONObject(i);

													String did = jobj
															.getString("StdId");
													String dname = jobj
															.getString("StdName");

													sd.execSQL("insert into DeptMaster values('"
															+ did
															+ "','"
															+ dname + "')");
												}
											} catch (SQLException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								// //Complete Getting Standard Details of
								// Current Standard ///

							}

						}

						else {
							alert.showAlertDialog(context, "Network Error",
									"No Network Connection", false);
						}

					}

				}

			}

		});

		setTitleColor(Color.WHITE);
		try {
			if (android.os.Build.VERSION.SDK_INT < 11) {
				// getSupportActionBar().setTitle("User Login");
				setTitle(Html
						.fromHtml("<font color='#ffffff'>User Login</font>"));
				getSupportActionBar().setIcon(R.drawable.ic_launcher);

				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor("#E52847")));
			} else {
				// getActionBar().setTitle("User Login");
				setTitle(Html
						.fromHtml("<font color='#ffffff'>User Login</font>"));
				getSupportActionBar().setIcon(R.drawable.ic_launcher);

				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor("#E52847")));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// getSupportActionBar().hide();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// Intent i = new Intent(context, SettingActivity.class);
		// startActivity(i);
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * if (keyCode == KeyEvent.KEYCODE_BACK) { moveTaskToBack(true);
	 * 
	 * return true; } return super.onKeyDown(keyCode, event);
	 * 
	 * }
	 */

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		private String url1;

		private String url, urlcontact, urlsubject, urltestResult;

		private JSONArray jarrayholiday;

		private JSONObject json3;
		private JSONObject json4;
		private JSONObject json5;
		private JSONObject json6;

		private PendingIntent pendingIntent;

		private int m;

		private int min;

		ProgressDialog moProgress = new ProgressDialog(LoginActivity.this);

		private JSONObject jsonnotes;

		private String urlBOD;

		private String urlfaculty;

		@Override
		protected void onPreExecute() {
			// moProgress.dismiss();
			if (!moProgress.isShowing()) {
				moProgress.setMessage("Please Wait Verifying ");
				moProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				moProgress.setIndeterminate(true);
				moProgress.show();
				flag = true;
			}
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			String qry = "Select * from DataUrl";

			Cursor cdata = sd.rawQuery(qry, null);
			startManagingCursor(cdata);
			while (cdata.moveToNext()) {
				url1 = cdata.getString(0);
			}
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			// url = url1 + "json_data.aspx?type=Faculty";
			urlfaculty = AllKeys.WEBSITE
					+ "GetFacultiesData?type=Faculty&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_faculty = sh.makeServiceCall(urlfaculty,
					ServiceHandler.GET);
			// JSONObject json1 = jParser.getJSONFromUrl(url);
			if (str_stud_faculty != null && !str_stud_faculty.equals("")) {

				str_stud_faculty = "{" + '"' + "Faculty" + '"' + ":"
						+ str_stud_faculty + "}";
				try {
					JSONObject jsonContact = new JSONObject(str_stud_faculty);
					if (jsonContact != null) {
						try {
							Log.d("Faculty Json : ", jsonContact.toString());
							// Getting Array of Contacts
							jarrayholiday = jsonContact.getJSONArray("Faculty");

							// looping through All Contacts
							sd.delete("staffinfo", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO staffinfo VALUES('"
										+ c.getString("FacultyId") + "','"
										+ c.getString("FacultyName") + "') ");

							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Faculty Json Error : ", e.getMessage());
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

			jParser = new JSONParser();

			// getting JSON string from URL
			// url = url1 + "json_data.aspx?type=Subject";
			urlsubject = AllKeys.WEBSITE
					+ "GetSubjectsData?type=Subject&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler shSubject = new ServiceHandler();
			String str_stud_Subject = shSubject.makeServiceCall(urlsubject,
					ServiceHandler.GET);
			if (str_stud_Subject != null && !str_stud_Subject.equals("")) {

				str_stud_Subject = "{" + '"' + "SubjectMst" + '"' + ":"
						+ str_stud_Subject + "}";
				try {
					JSONObject jsonsub = new JSONObject(str_stud_Subject);
					// json2 = jParser.getJSONFromUrl(url);

					if (jsonsub != null) {
						try {
							Log.d("subject json", "" + jsonsub.toString());
							// Getting Array of Contacts
							jarrayholiday = jsonsub.getJSONArray("SubjectMst");

							// looping through All Contacts
							sd.delete("submaster", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);
								sd.execSQL("INSERT INTO submaster VALUES('"
										+ c.getString("SubId") + "','"
										+ c.getString("SubName") + "') ");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Subject json error", e.getMessage());
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

			jParser = new JSONParser();

			// getting JSON string from URL
			// url = url1 + "json_data.aspx?DeptId=" + deptid + "&EmpId=" +
			// empid
			// + "&type=A";
			// url = url1 + "json_data.aspx?EmpId=" + empid + "&type=A";
			// http://radiant.dnsitexperts.com/json_data.aspx?DeptId=13&EmpId=388&type=A
			String urlAttendance = AllKeys.WEBSITE
					+ "GetAttendanceData?type=A&EmpId=" + empid + "&DeptId="
					+ deptid + "&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler shAtt = new ServiceHandler();
			String str_stud_att = shAtt.makeServiceCall(urlAttendance,
					ServiceHandler.GET);

			if (str_stud_att != null && !str_stud_att.equals("")) {

				str_stud_att = "{" + '"' + "TransDet" + '"' + ":"
						+ str_stud_att + "}";
				try {
					JSONObject json = new JSONObject(str_stud_att);
					if (json != null) {
						try {
							jarrayholiday = json.getJSONArray("TransDet");
							// Getting Array of Contacts

							// looping through All Contacts
							sd.delete("studattendence", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO studattendence VALUES('"
										+ c.getString("AttendanceDate")
										+ "','"
										+ c.getString("Flag") + "') ");
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
			jParser = new JSONParser();
			urlBOD = AllKeys.WEBSITE + "GetStudentDetails?type=BTD&deptid="
					+ deptid + "&empid="
					+ userDetails.get(SessionManager.KEY_EMPID) + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			// getting JSON string from URL
			// url = url1 + "json_data.aspx?type=BTD&DeptId=" + deptid +
			// "&EmpId="+ empid;
			ServiceHandler shBOD = new ServiceHandler();
			String str_stud_BOD = shBOD.makeServiceCall(urlBOD,
					ServiceHandler.GET);

			// JSONObject jsonbday = jParser.getJSONFromUrl(url);
			if (str_stud_BOD != null && !str_stud_BOD.equals("")) {

				str_stud_BOD = "{" + '"' + "StudentBirthday" + '"' + ":"
						+ str_stud_BOD + "}";
				try {
					JSONObject jsonbday = new JSONObject(str_stud_BOD);
					if (jsonbday != null) {

						try {
							// Getting Array of Contacts
							jarrayholiday = jsonbday
									.getJSONArray("StudentBirthday");

							// looping through All Contacts
							sd.delete("birthday", null, null);

							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);

								sd.execSQL("INSERT INTO birthday VALUES('"
										+ c.getString("EmpId") + "','"
										+ c.getString("Name") + "','"
										+ c.getString("DOB") + "') ");
								String dt = c.getString("DOB");
								if (!c.getString("DOB").equals("")) {
									String[] sdt = dt.split("-");
									int mon = Integer.parseInt(sdt[1]);
									int day = Integer.parseInt(sdt[2]);
									int eid = Integer.parseInt(c
											.getString("EmpId"));
									if (mon >= 1)
										m = mon - 1;

									Calendar calendar = Calendar.getInstance();

									calendar.set(Calendar.MONTH, m);
									calendar.set(Calendar.YEAR,
											calendar.get(Calendar.YEAR));
									calendar.set(Calendar.DAY_OF_MONTH, day);

									calendar.set(Calendar.HOUR_OF_DAY, 8);
									calendar.set(Calendar.MINUTE, 30);
									calendar.set(Calendar.SECOND, 0);
									calendar.set(Calendar.AM_PM, Calendar.AM);

									Intent myIntent = new Intent(context,
											MyBroadcastReceiver.class);
									// Birthday Notification
									// myIntent.putExtra("name",
									// "Happy Birthday "+c.getString("Name"));
									// myIntent.putExtra("eid", ""+eid);
									// pendingIntent =
									// PendingIntent.getBroadcast(context, eid,
									// myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

									AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
									alarmManager.set(AlarmManager.RTC,
											calendar.getTimeInMillis(),
											pendingIntent);
								}

							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (SQLException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
			jParser = new JSONParser();
			// getting JSON string from URL
			// url = url1 + "json_data.aspx?type=TD&DeptId=" + deptid +
			// "&EmpId="+ empid;
			url = AllKeys.WEBSITE + "GetTestMasterData?type=TD&DeptId="
					+ deptid + "&Trsrno=0&clientid=" + AllKeys.CLIENT_ID
					+ "&branch=" + userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler shTD = new ServiceHandler();
			String str_stud_TD = shTD.makeServiceCall(url, ServiceHandler.GET);
			if (str_stud_TD != null && !str_stud_TD.equals("")) {

				str_stud_TD = "{" + '"' + "Test" + '"' + ":" + str_stud_TD
						+ "}";
				try {
					JSONObject jsontest = new JSONObject(str_stud_TD);
					// JSONObject jsontest = jParser.getJSONFromUrl(url);
					if (jsontest != null) {

						try {
							// Getting Array of Contacts
							jarrayholiday = jsontest.getJSONArray("Test");

							// looping through All Contacts
							sd.delete("testmaster", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);
								qry = "INSERT INTO testmaster VALUES('"
										+ c.getString("TrsrNo") + "','"
										+ c.getString("Dt") + "','"
										+ c.getString("TestName") + "','"
										+ c.getString("TotMks") + "','"
										+ c.getString("PassingMks") + "','"
										+ c.getString("DesigId") + "','"
										+ c.getString("SubId") + "') ";
								sd.execSQL(qry);

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

			if (TOTALDEPTS > 1) {
				Intent intent = new Intent(context, MultipleClasses.class);
				intent.putExtra("USERMOBILE", "" + USERMOBILE);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(context, MainActivity.class);

				startActivity(intent);
				finish();

			}

			moProgress.dismiss();

		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
				String query;
				SessionManager sessionmanager = new SessionManager(
						getApplicationContext());
				HashMap<String, String> userDetails = new HashMap<String, String>();
				userDetails = sessionmanager.getUserDetails();

				if (newMessage.contains("GSS")) {

					// Waking up mobile if it is sleeping
					WakeLocker.acquire(getApplicationContext());
					// Showing received message
					// lblMessage.append(newMessage + "\n");

					// sd.execSQL("insert into NotificationMst values(null,'DNS
					// IT
					// EXPERTS','"+
					// newMessage +"','datetime()','05:11')");
					String cdate = getDateTime();

					sessionmanager.StorePushNotification("", newMessage, cdate);
					// Notification_Mst(id INTEGER PRIMARY KEY
					// AUTOINCREMENT,header
					// TEXT,notification TEXT,ndate text)");
					// dfsd
					try {
						String TotalMessage = userDetails
								.get(SessionManager.KEY_PUSH_DESCR);

						// String TITLE =
						// TotalMessage = "Hello GSS 123";
						String[] splitted = TotalMessage.split("GSS");
						lstMessage.clear();
						for (String str : splitted) {
							lstMessage.add(str);
							// Do what you need with your tokens here.
							// Toast.makeText(context, "splitted : "+str,
							// Toast.LENGTH_SHORT).show();

						}

					} catch (Exception e) {
						System.out.print("Error : " + e.getMessage());
					}

					query = "insert into Notification_Mst values(null,'"
							+ lstMessage.get(0) + "','" + lstMessage.get(1)
							+ "','" + cdate + "')";

					sd.execSQL(query);

					System.out.print("Current Date : " + cdate);
					System.out.print("Not Query : " + query);

					// sd.execSQL("insert into NotificationMst
					// values(null,'paymyreview.in','"+
					// newMessage +"','"+ cdate +"','05:10')");
					// Toast.makeText(getApplicationContext(), "New Message: " +
					// newMessage, Toast.LENGTH_LONG).show();
					// Releasing wake lock
					WakeLocker.release();

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
		sd.close();
		db.close();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString("flag", "" + flag);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		if (savedInstanceState.get("flag").equals("true")) {
			new DownloadWebPageTask().execute("");
		} else {

		}

	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
				Locale.getDefault());

		Date date = new Date();
		return dateFormat.format(date);
	}

}
