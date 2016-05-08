package com.educationam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MultipleClasses extends ListActivity {

	ListView lst;
	private String urlBOD;
	dbhandler db;
	SQLiteDatabase sd;
	private Context context = this;

	private String deptid;

	private String empid;

	private String MOBILE;
	private String deptID;
	private String empID;
	private String IDdept;
	private String IDemp;

	SessionManager session;

	ArrayList<HashMap<String, String>> userDetails;

	private HashMap<String, String> sessiondetails = new HashMap<String, String>();
	private String classname;
	private String studname;
	private String branchid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multipleclasses);

		lst = getListView();

		Intent iii = getIntent();

		MOBILE = "" + iii.getStringExtra("USERMOBILE");

		session = new SessionManager(context);

		db = new dbhandler(context);
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();
		userDetails = new ArrayList<HashMap<String, String>>();
		sessiondetails = session.getUserDetails();
		deptID = sessiondetails.get(SessionManager.KEY_DEPTID);
		empID = sessiondetails.get(SessionManager.KEY_EMPID);
		getAllStandards();

		lst.setOnItemClickListener(new OnItemClickListener() {

			private String deptids;
			private String empids;
			private int TOTALDEPTS;
			private String url1;
			private String url;
			private String depturl;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				String did = ((TextView) view.findViewById(R.id.txtstandardid))
						.getText().toString();
				String eid = ((TextView) view.findViewById(R.id.txtempid))
						.getText().toString();
				IDdept = did;
				IDemp = eid;

				session.createBranch(branchid);
				session.createStudentNameSession(studname);

				session.createStudentSession(IDdept, IDemp,
						sessiondetails.get(SessionManager.KEY_VERSTATUS));

				// ///////////Getting Current Student Details From
				// Wesite////////////////

				JSONParser jparser = new JSONParser();
				/*
				 * dbhandler db=new dbhandler(context); final SQLiteDatabase
				 * sd=db.getReadableDatabase();
				 * 
				 * sd.delete("dataurl", null, null); sd.execSQL(
				 * "INSERT INTO dataurl values('"+url1+"')"); JSONParser
				 * jparser=new JSONParser();
				 * url=url1+"JSON_Data.aspx?MobileNo="+MOBILE;
				 */ConnectionDetector cd = new ConnectionDetector(context);
				AlertDialogManager alert = new AlertDialogManager();

				url1 = "http://" + AllKeys.WEBSITE2 + "/";
				// depturl = url1 + "JSON_Data.aspx?type=Dept";

				if (cd.isConnectingToInternet() == true) {

					depturl = AllKeys.WEBSITE
							+ "ServiceP.asmx/GetStandardsData?type=standard&clientid="
							+ AllKeys.CLIENT_ID + "&branch="
							+ sessiondetails.get(SessionManager.KEY_BRANCHID);

					Log.d("depturl", depturl);
					// JSONObject jsonDept = jparser
					// .getJSONFromUrl(depturl);

					ServiceHandler sh = new ServiceHandler();
					String str_stud_remark = sh.makeServiceCall(depturl,
							ServiceHandler.GET);
					if (str_stud_remark != null && !str_stud_remark.equals("")) {

						str_stud_remark = "{" + '"' + "Dept" + '"' + ":"
								+ str_stud_remark + "}";
						try {
							JSONObject jsonDept = new JSONObject(
									str_stud_remark);
							if (jsonDept != null) {
								try {
									JSONArray jarray = jsonDept
											.getJSONArray("Dept");

									for (int i = 0; i < jarray.length(); i++) {
										JSONObject jobj = jarray
												.getJSONObject(i);

										String deid = jobj.getString("StdId");
										String dname = jobj
												.getString("StdName");

										sd.execSQL("insert into DeptMaster values('"
												+ deid + "','" + dname + "')");
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

					new DownloadWebPageTask().execute("");

					// //Complete Getting Standard Details of Current Standard
					// ///

				}

				else {
					alert.showAlertDialog(
							context,
							"No Network Connection",
							"Please check your Wi-Fi or mobile network connection and try again.",
							false);
				}

				// //////////////Complete Getting Current Student DEtails
				// //////////////

				// new DownloadWebPageTask().execute("");

			}

		});

	}

	public void getAllStandards() {

		String query = "select * from studinfo";

		Cursor cc = sd.rawQuery(query, null);

		userDetails.clear();
		while (cc.moveToNext()) {
			HashMap<String, String> user = new HashMap<String, String>();
			user.put("DEPTID", "" + cc.getString(0));
			user.put("EMPID", "" + cc.getString(1));

			studname = cc.getString(2);
			branchid = cc.getString(3);
			String ddname = getCurrentDeptNameByID(Integer.parseInt(cc
					.getString(0)));
			// user.put("STDNAME", "" + cc.getString(0));
			user.put("NAME", "" + cc.getString(2));
			user.put("BRANCHID", "" + cc.getString(3));
			user.put("DEPTNAME", "" + cc.getString(4));
			userDetails.add(user);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, userDetails,
				R.layout.row_single_standard, new String[] { "DEPTID", "EMPID",
						"NAME", "DEPTNAME", "BRANCHID" }, new int[] {
						R.id.txtstandardid, R.id.txtempid, R.id.txtname,
						R.id.txtstandatd, R.id.txtbranchid });
		lst.setAdapter(adapter);

	}

	public String getCurrentDeptNameByID(int deptid) {
		String Stadardname = "";
		Cursor c = sd.rawQuery("select * from DeptMaster where DEPTID = "
				+ deptid, null);
		while (c.moveToNext()) {

			if (c.getInt(0) == deptid) {
				Stadardname = c.getString(1);
				return Stadardname;
			}
		}

		return Stadardname;
	}

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		private String url1;

		private String url, urltestResult;
		private JSONArray jarrayholiday;
		private JSONObject json2;
		private JSONObject json3;
		private JSONObject json4;
		private JSONObject json5;
		private JSONObject json6;

		private PendingIntent pendingIntent;

		private int m;

		private int min;

		ProgressDialog moProgress = new ProgressDialog(MultipleClasses.this);

		private JSONObject jsonnotes;

		private boolean flag;

		private String depturl;

		private String urlsubject;

		private String urlNotice;

		private String urlfaculty;

		@Override
		protected void onPreExecute() {
			// moProgress.dismiss();
			if (!moProgress.isShowing()) {
				moProgress.setMessage("Please Wait Details Are Loading.... ");
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
			dbhandler db = new dbhandler(context);
			SQLiteDatabase sd = db.getReadableDatabase();
			Cursor cdata = sd.rawQuery(qry, null);
			startManagingCursor(cdata);
			while (cdata.moveToNext()) {
				url1 = cdata.getString(0);
			}

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			try {
				urlfaculty = AllKeys.WEBSITE
						+ "GetFacultiesData?type=Faculty&clientid="
						+ AllKeys.CLIENT_ID + "&branch="
						+ sessiondetails.get(SessionManager.KEY_BRANCHID);
				ServiceHandler sh = new ServiceHandler();
				String str_stud_faculty = sh.makeServiceCall(urlfaculty,
						ServiceHandler.GET);
				// JSONObject json1 = jParser.getJSONFromUrl(url);

				if (str_stud_faculty != null && !str_stud_faculty.equals("")) {

					str_stud_faculty = "{" + '"' + "Faculty" + '"' + ":"
							+ str_stud_faculty + "}";
					try {
						JSONObject jsonContact = new JSONObject(
								str_stud_faculty);
						if (jsonContact != null) {
							try {
								Log.d("Faculty Json : ", jsonContact.toString());
								// Getting Array of Contacts
								jarrayholiday = jsonContact
										.getJSONArray("Faculty");

								// looping through All Contacts
								sd.delete("staffinfo", null, null);
								for (int i = 0; i < jarrayholiday.length(); i++) {
									JSONObject c = jarrayholiday
											.getJSONObject(i);

									sd.execSQL("INSERT INTO staffinfo VALUES('"
											+ c.getString("FacultyId") + "','"
											+ c.getString("FacultyName")
											+ "') ");

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
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			jParser = new JSONParser();

			// getting JSON string from URL
			urlsubject = AllKeys.WEBSITE
					+ "GetSubjectsData?type=Subject&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);

			ServiceHandler shSubject = new ServiceHandler();
			String str_stud_Subject = shSubject.makeServiceCall(urlsubject,
					ServiceHandler.GET);
			if (str_stud_Subject != null && !str_stud_Subject.equals("")) {

				str_stud_Subject = "{" + '"' + "SubjectMst" + '"' + ":"
						+ str_stud_Subject + "}";

				// json2 = jParser.getJSONFromUrl(url);
				try {
					JSONObject json = new JSONObject(str_stud_Subject);
					if (json != null) {
						try {
							Log.d("subject json", "" + json.toString());
							// Getting Array of Contacts
							jarrayholiday = json.getJSONArray("SubjectMst");

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

			depturl = AllKeys.WEBSITE
					+ "GetStandardsData?type=standard&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);
			// JSONObject jsonDept = jparser
			// .getJSONFromUrl(depturl);

			ServiceHandler sh1 = new ServiceHandler();
			String str_stud_remark = sh1.makeServiceCall(depturl,
					ServiceHandler.GET);
			if (str_stud_remark != null && !str_stud_remark.equals("")) {

				str_stud_remark = "{" + '"' + "Dept" + '"' + ":"
						+ str_stud_remark + "}";
				try {
					JSONObject jsonDept = new JSONObject(str_stud_remark);
					if (jsonDept != null) {
						try {
							JSONArray jarray = jsonDept.getJSONArray("Dept");

							for (int i = 0; i < jarray.length(); i++) {
								JSONObject jobj = jarray.getJSONObject(i);

								String did = jobj.getString("StdId");
								String dname = jobj.getString("StdName");

								sd.execSQL("insert into DeptMaster values('"
										+ did + "','" + dname + "')");
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

			jParser = new JSONParser();

			// getting JSON string from URL
			String urlAttendance = AllKeys.WEBSITE
					+ "GetAttendanceData?type=A&EmpId=" + empID + "&DeptId="
					+ deptID + "&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);

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
							// Getting Array of Contacts
							jarrayholiday = json.getJSONArray("TransDet");

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
			urlNotice = AllKeys.WEBSITE
					+ "GetNoticeBoardsData?type=NoticeBoard&classid=" + deptID
					+ "&clientid=" + empID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);
			// getting JSON string from URL
			// url = url1 + "json_data.aspx?type=notice";
			ServiceHandler shNotice = new ServiceHandler();
			String str_stud_Notice = shNotice.makeServiceCall(urlNotice,
					ServiceHandler.GET);

			if (str_stud_Notice != null && !str_stud_Notice.equals("")) {

				str_stud_Notice = "{" + '"' + "NoticeBoard" + '"' + ":"
						+ str_stud_Notice + "}";
				// json6 = jParser.getJSONFromUrl(url);
				try {
					JSONObject jsonn = new JSONObject(str_stud_Notice);
					if (jsonn != null) {
						try {
							// Getting Array of Contacts
							jarrayholiday = jsonn.getJSONArray("NoticeBoard");

							// looping through All Contacts
							sd.delete("noticeboard", null, null);
							for (int i = 0; i < jarrayholiday.length(); i++) {
								JSONObject c = jarrayholiday.getJSONObject(i);
								// CharSequence
								// content=(Html.fromHtml(c.getString("LongDescription")));
								sd.execSQL("INSERT INTO noticeboard VALUES('"
										+ c.getString("Id") + "','"
										+ c.getString("ShortDescription")
										+ "','"
										+ c.getString("LongDescription")
										+ "','" + c.getString("Date") + "','"
										+ c.getString("IsShown") + "') ");

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

			// getting JSON string from URL

			// JSONObject json = jParser.getJSONFromUrl(url);

			jParser = new JSONParser();

			// getting JSON string from URL
			/*
			 * url = url1 + "json_data.aspx?type=Video"; JSONObject jsonvideo =
			 * jParser.getJSONFromUrl(url); if (jsonvideo != null) {
			 * 
			 * try { // Getting Array of Contacts jarrayholiday =
			 * jsonvideo.getJSONArray("Video");
			 * 
			 * // looping through All Contacts sd.delete("Video", null, null);
			 * for (int i = 0; i < jarrayholiday.length(); i++) { JSONObject c =
			 * jarrayholiday.getJSONObject(i);
			 * 
			 * sd.execSQL("INSERT INTO Video VALUES('" + c.getString("VideoId")
			 * + "','" + c.getString("VideoName") + "','" +
			 * c.getString("VideoUrl") + "','" + c.getString("DeptId") + "','" +
			 * c.getString("SubId") + "','" + c.getString("Chapter") + "') ");
			 * 
			 * }
			 * 
			 * } catch (JSONException e) {
			 * 
			 * e.printStackTrace(); }
			 * 
			 * }
			 */

			jParser = new JSONParser();

			// getting JSON string from URL
			urlBOD = AllKeys.WEBSITE + "GetStudentDetails?type=BTD&deptid="
					+ deptID + "&empid=" + empID + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			url = AllKeys.WEBSITE + "GetTestMasterData?type=TD&DeptId="
					+ sessiondetails.get(SessionManager.KEY_DEPTID)
					+ "&Trsrno=1&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);
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

			jParser = new JSONParser();

			// getting JSON string from URL
			urltestResult = AllKeys.WEBSITE
					+ "GetResultDataByObjectiveandSubjective?type=TDD&DeptId="
					+ sessiondetails.get(SessionManager.KEY_DEPTID) + "&EmpId="
					+ sessiondetails.get(SessionManager.KEY_EMPID)
					+ "&Trsrno=1&clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ sessiondetails.get(SessionManager.KEY_BRANCHID);
			// url = url1 + "json_data.aspx?type=TDD&DeptId=" + deptid +
			// "&EmpId="
			// + empid;

			ServiceHandler shTDD = new ServiceHandler();
			String str_stud_TDD = shTDD.makeServiceCall(urltestResult,
					ServiceHandler.GET);
			if (str_stud_TDD != null && !str_stud_TDD.equals("")) {
				str_stud_TDD = "{" + '"' + "TestResult" + '"' + ":"
						+ str_stud_TDD + "}";
				try {
					try {
						JSONObject json = new JSONObject(str_stud_TDD);
						if (json != null) {
							try {
								// Getting Array of Contacts
								jarrayholiday = json.getJSONArray("TestResult");

								// looping through All Contacts
								sd.delete("testdetail", null, null);
								for (int i = 0; i < jarrayholiday.length(); i++) {
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
											+ c.getString("SubMarks")
											+ "','"
											+ c.getString("ObjMarks")
											+ "','"
											+ c.getString("HighestMks")
											+ "','"
											+ c.getString("DeptId") + "') ");
								}

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			moProgress.dismiss();
			Intent main = new Intent(context, MainActivity.class);
			main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// main.putExtra("DEPTID", "" + IDdept);
			// main.putExtra("STUDENTID", "" + IDemp);
			startActivity(main);
			/*
			 * String status =
			 * sessiondetails.get(SessionManager.KEY_ACT_STATUS); if
			 * (sessiondetails.get(SessionManager.KEY_ACT_STATUS).equals("0")) {
			 * Intent main = new Intent(context, StartActivity.class);
			 * startActivity(main); } else { Intent main = new Intent(context,
			 * MainActivity.class); main.putExtra("DEPTID", "" + IDdept);
			 * main.putExtra("STUDENTID", "" + IDemp); startActivity(main);
			 * 
			 * }
			 */

			/*
			 * Intent intent=new Intent(context,MainActivity.class);
			 * startActivity(intent); finish();
			 */

		}
	}

}
