package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applidium.shutterbug.FetchableImageView;
import com.applidium.shutterbug.utils.ShutterbugManager;
import com.applidium.shutterbug.utils.ShutterbugManager.ShutterbugManagerListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentProfile extends Fragment {

	ArrayList<HashMap<String, String>> homeworklist = new ArrayList<HashMap<String, String>>();
	private Context context = getActivity();
	private TextView txtname;
	private TextView txtmobile;
	private TextView txtgender;
	private TextView txtdob;
	private TextView txtfathermob;
	private TextView txtmothermob;
	private TextView txtstandard;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	private dbhandler db;
	private SQLiteDatabase sd;
	private TextView txtaddress, txtachievement;
	private String studName;
	private String studname = "";
	private String standard = "";
	private String dob = "";
	private String fathermobile = "";
	private String mothermobile = "";
	private String studentmobile = "";
	private String gender = "";
	private String address = "", FacultyName = "", AcademicYear = "";
	String studid, studphoto;
	FetchableImageView fetchimg;
	private TextView txtfaculty;
	private TextView txtacademic;
	private String homeworkname;
	private String facultyname;
	private String date;
	private String achievementname;
	private ListView listview;

	public FragmentProfile() {
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
		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);

		sessionmanager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();
		listview = (ListView) rootView.findViewById(R.id.listAchiv);

		// scrollViewprofile.requestDisallowInterceptTouchEvent(true);
		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();
		fetchimg = (FetchableImageView) rootView
				.findViewById(R.id.imgProfilePic);
		txtname = (TextView) rootView.findViewById(R.id.txtname);
		txtmobile = (TextView) rootView.findViewById(R.id.txtmobile);
		txtstandard = (TextView) rootView.findViewById(R.id.txtstandard);
		txtgender = (TextView) rootView.findViewById(R.id.txtgender);
		txtdob = (TextView) rootView.findViewById(R.id.txtdob);
		txtfathermob = (TextView) rootView.findViewById(R.id.txtfather);
		txtmothermob = (TextView) rootView.findViewById(R.id.txtmother);
		txtaddress = (TextView) rootView.findViewById(R.id.txtaddress);
		// txtachievement = (TextView)
		// rootView.findViewById(R.id.txtachievement);
		txtfaculty = (TextView) rootView.findViewById(R.id.txtfaculty);
		txtacademic = (TextView) rootView.findViewById(R.id.txtacademic);

		txtstandard.setEnabled(false);
		txtdob.setEnabled(false);
		txtfathermob.setEnabled(false);
		txtmothermob.setEnabled(false);
		txtaddress.setEnabled(false);
		// txtachievement.setEnabled(false);
		txtgender.setEnabled(false);
		txtfaculty.setEnabled(false);
		txtacademic.setEnabled(false);
		txtmobile.setText("" + userDetails.get(SessionManager.KEY_MOBILE));

		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (NetConnectivity.isOnline(getActivity())) {
			new GetStudentDetailsFromServer().execute();
		} else {
			FillData();
			Toast.makeText(getActivity(), "Please Enable Internet",
					Toast.LENGTH_SHORT).show();
		}
		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	class GetStudentDetailsFromServer extends AsyncTask<String, Void, String> {

		private String url1;
		private ProgressDialog pDialog;
		private JSONArray jarraystudent, jarraystudentphoto;
		private JSONArray jarrayhomework;
		private String url2;

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

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			String url1 = AllKeys.WEBSITE
					+ "JSON_Data.aspx?type=empphoto&empid="
					+ userDetails.get(SessionManager.KEY_EMPID);
			// String url = AllKeys.WEBSITE + "json_data.aspx?deptid="
			// + userDetails.get(SessionManager.KEY_DEPTID) + "&empid="
			// + userDetails.get(SessionManager.KEY_EMPID) + "";
			String url = AllKeys.WEBSITE + "GetJSONForStudentDetails?classid="
					+ userDetails.get(SessionManager.KEY_DEPTID) + "&empid="
					+ userDetails.get(SessionManager.KEY_EMPID) + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);

			ServiceHandler sh = new ServiceHandler();
			String str_stud_profile = sh.makeServiceCall(url,
					ServiceHandler.GET);

			ContentValues cv = new ContentValues();
			if (str_stud_profile != null && !str_stud_profile.equals("")) {

				str_stud_profile = "{" + '"' + "StudDet" + '"' + ":"
						+ str_stud_profile + "}";

				try {
					JSONObject jsonprofile = new JSONObject(str_stud_profile);

					if (jsonprofile != null) {
						try {
							Log.d("StudentDet Json data",
									jsonprofile.toString());
							// Getting Array of Contacts
							jarraystudent = jsonprofile.getJSONArray("StudDet");
							for (int i = 0; i < jarraystudent.length(); i++) {
								JSONObject c = jarraystudent.getJSONObject(i);

								studname = c.getString("studentname");
								standard = c.getString("standard");
								dob = c.getString("dob");
								fathermobile = c.getString("fathermobile");
								mothermobile = c.getString("mothermobile");
								studentmobile = c.getString("studentmobile");
								gender = c.getString("gender");
								address = c.getString("address");
								AcademicYear = c.getString("AcademicYear");
								FacultyName = c.getString("Faculty");

								sessionmanager.StoreStudentDetails(studname,
										standard, dob, fathermobile,
										mothermobile, studentmobile, gender,
										address, AcademicYear, FacultyName);
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("StudentDet json error", e.getMessage());
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String urlachieve = AllKeys.WEBSITE
					+ "GetAchievementDetailByStudData?type=achievement&studid="
					+ userDetails.get(SessionManager.KEY_DEPTID) + "&clientid="
					+ AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			ServiceHandler sh1 = new ServiceHandler();
			String str_stud_remark2 = sh1.makeServiceCall(urlachieve,
					ServiceHandler.GET);

			/*
			 * if (str_stud_remark2 != null && !str_stud_remark2.equals("")) {
			 * 
			 * str_stud_remark2 = "{" + '"' + "Achievement" + '"' + ":" +
			 * str_stud_remark2 + "}"; Log.d("achieve=>", urlachieve);
			 * 
			 * try { JSONObject json2 = new JSONObject(str_stud_remark2); if
			 * (json2 != null) { try { Log.d("Achievement Json data",
			 * json2.toString()); // Getting Array of Contacts jarrayhomework =
			 * json2.getJSONArray("Achievement");
			 * 
			 * // looping through All Contacts sd.delete("achievementsmst",
			 * null, null); for (int i = 0; i < jarrayhomework.length(); i++) {
			 * JSONObject cr = jarrayhomework.getJSONObject(i);
			 * 
			 * sd.execSQL("INSERT INTO achievementsmst VALUES(null,'" +
			 * cr.getString("Description") + "','" + cr.getString("FacultyID") +
			 * "','" + cr.getString("Date") + "','" + cr.getString("StudID") +
			 * "') ");
			 * 
			 * }
			 * 
			 * } catch (JSONException e) { e.printStackTrace(); Log.d(
			 * "Homework json error", e.getMessage()); } } } catch (SQLException
			 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 * catch (JSONException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */
			return null;

		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();
			DownloadImage(studphoto);
			FillData();
		}
	}

	private void DownloadImage(String photolink) {

		try {
			photolink = photolink.replace(" ", "%20");
			ShutterbugManager.getSharedImageManager(getActivity()).download(
					photolink, (ShutterbugManagerListener) fetchimg);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void FillData() {
		// TODO Auto-generated method stub

		try {

			// *********************************************

			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();

			Cursor c = sd.rawQuery("select * from achievementsmst", null);
			getActivity().startManagingCursor(c);
			homeworklist.clear();
			int cnt = 0;
			while (c.moveToNext()) {
				achievementname = c.getString(1);
				facultyname = c.getString(2);
				date = c.getString(3);
				studName = c.getString(4);
				cnt = cnt + 1;
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("ACHIEVEMENT", achievementname);
				map.put("FACULTY", "Teacher : " + facultyname);
				map.put("DATE", date);
				map.put("NO", "" + cnt + ".");
				map.put("STUDNAME", studname);

				homeworklist.add(map);

				SimpleAdapter adapter = new SimpleAdapter(
						getActivity(),
						homeworklist,
						R.layout.lst_row_single_achievement,
						new String[] { "ACHIEVEMENT", "NO", "DATE", "STUDNAME" },
						new int[] { R.id.lblAchievement, R.id.lblNo,
								R.id.lblDate, R.id.txtstudname });

				listview.setAdapter(adapter);

			}

			// *********************************************

			txtname.setText("" + studname);
			txtaddress.setText("" + address);
			// txtachievement.setText("" + achievementname);
			txtdob.setText(dob);
			txtfathermob.setText(fathermobile);
			txtmothermob.setText(mothermobile);
			// txtmobile.setText("+91 " + studentmobile);

			txtstandard.setText(standard);
			txtgender.setText(gender);

			txtacademic.setText(AcademicYear);
			txtfaculty.setText(FacultyName);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO your code to hide item here
		super.onCreateOptionsMenu(menu, inflater);
	}

}
