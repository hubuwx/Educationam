package com.educationam;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "AndroidHivePref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// All Shared Preferences Keys
	private static final String IS_ACTIVATED = "IsActivated";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";
	public static final String KEY_STDNAMEOK = "stdname";

	public static final String KEY_EMPID = "empid";
	public static final String KEY_BRANCHID = "branchid";
	public static final String KEY_DEPTID = "deptid",

			KEY_RECEIVECODE = "reccode", KEY_ACT_STATUS = "actstatus", KEY_VERSTATUS = "verification_status";
	public static final String KEY_DEPTNAME = "deptname";
	public static final String KEY_PUSH_TITLE = "push_title";
	public static final String KEY_PUSH_DESCR = "push_descr";
	public static final String KEY_PUSH_DATE = "push_date";

	public static final String KEY_STUDNAME = "studname", KEY_STUDSTANDARD = "standard", KEY_STUDDOB = "studdob",
			KEY_STUDFATHERMOB = "fathermob", KEY_STUDMOTHERMOB = "mothermob", KEY_STUDSTUDENTMOB = "studentmob",
			KEY_STUDGENDER = "studgender", KEY_STUDADDRESS = "studaddress", KEY_ACADEMICYEAR = "AcademicYear",
			KEY_FACULTYNAME = "facultyname";

	public static final String KEY_CURRENT_DATE = "current_date", KEY_WEBSITE = "website";

	// Check For Activation
	public static final String KEY_CODE = "code", KEY_SMSURL = "smsurl";

	public static final String KEY_DEVICEID = "DeviceID", KEY_VERIFICATION_COUNTER = "ver_counter";

	public static final String KEY_LATTITUDE = "lattitude", KEY_LONGTITUDE = "longttude", KEY_ADDRESS = "address",
			KEY_MOBILE = "mobileno";

	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void StorePushNotification(String title, String descr, String dtime) {
		editor.putString(KEY_PUSH_TITLE, title);
		editor.putString(KEY_PUSH_DESCR, descr);

		editor.putString(KEY_PUSH_DATE, dtime);
		editor.commit();
	}

	public void CreateCurrentDate(String currendate) {
		editor.putString(KEY_CURRENT_DATE, currendate);
		editor.commit();

	}

	public void createUserSendSmsUrl(String code, String websiteurl) {

		editor.putString(KEY_CODE, code);
		editor.putString(KEY_SMSURL, websiteurl);// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile=9825681802&code=7692
		editor.commit();

	}

	public void CheckSMSVerificationActivity(String reccode, String actstatus) {

		editor.putString(KEY_RECEIVECODE, reccode);
		editor.putString(KEY_ACT_STATUS, actstatus);
		editor.commit();

	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, StartActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}

	}

	/**
	 * Create login session
	 */
	public void createLoginSession(String name, String email) {
		// Storing login value as TRUE
		// editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);

		// commit changes
		editor.commit();
	}

	/**
	 * Create login session
	 */
	// editor.putString(KEY_BRANCHID, branchid);
	public void createStudentSession(String deptid, String empid, String verstatus) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_EMPID, empid);

		// Storing email in pref
		editor.putString(KEY_DEPTID, deptid);

		editor.putString(KEY_ACT_STATUS, verstatus);

		// commit changes
		editor.commit();
	}

	public void createBranch(String branchid) {

		editor.putString(KEY_BRANCHID, branchid);

		// commit changes
		editor.commit();
	}

	public void createDeptName(String deptname) {

		editor.putString(KEY_DEPTNAME, deptname);

		// commit changes
		editor.commit();
	}

	public void createStudentNameSession(String stdname) {
		// Storing login value as TRUE

		// Storing email in pref

		editor.putString(KEY_STDNAMEOK, stdname);

		// commit changes
		editor.commit();
	}

	public void createLoginUserTypeSession(String type) {
		// Storing login value as TRUE

		// Storing email in pref

		editor.putString(KEY_TYPE, type);

		// commit changes
		editor.commit();
	}

	public void StoreStudentDetails(String studname, String standard, String dob, String fatmob, String mothermob,
			String studmob, String gender, String address, String academicyear, String facultyname) {

		// KEY_STUDNAME , KEY_STUDSTANDARD
		// ,KEY_STUDDOB,KEY_STUDFATHERMOB,KEY_STUDMOTHERMOB,KEY_STUDSTUDENTMOB,KEY_STUDGENDER

		editor.putString(KEY_STUDNAME, studname);
		editor.putString(KEY_STUDSTANDARD, standard);
		editor.putString(KEY_STUDDOB, dob);
		editor.putString(KEY_STUDFATHERMOB, fatmob);
		editor.putString(KEY_STUDMOTHERMOB, mothermob);
		editor.putString(KEY_STUDSTUDENTMOB, studmob);
		editor.putString(KEY_STUDGENDER, gender);
		editor.putString(KEY_STUDADDRESS, address);
		editor.putString(KEY_ACADEMICYEAR, academicyear);
		editor.putString(KEY_FACULTYNAME, facultyname);

		editor.commit();
	}

	/**
	 * Create Activation session
	 */
	public void createWebsite(String website, String deviceID) {
		// Storing login value as TRUE
		// editor.putBoolean(IS_ACTIVATED, true);

		// Storing name in pref
		editor.putString(KEY_WEBSITE, website);
		editor.putString(KEY_DEVICEID, deviceID);

		// commit changes
		editor.commit();
	}

	public void createContactUsDetails(String lattitude, String longtitude, String address) {
		// Storing login value as TRUE
		// editor.putBoolean(IS_ACTIVATED, true);

		// Storing name in pref
		editor.putString(KEY_LATTITUDE, lattitude);
		editor.putString(KEY_LONGTITUDE, longtitude);
		editor.putString(KEY_ADDRESS, address);

		// commit changes
		editor.commit();
	}

	public void createUserMobile(String mobile) {
		// Storing login value as TRUE

		if (mobile.length() == 10) {
			// Storing name in pref
			editor.putString(KEY_MOBILE, mobile);

		}

		// commit changes
		editor.commit();
	}

	/**
	 * Get stored session data
	 */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));

		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		user.put(KEY_CODE, pref.getString(KEY_CODE, "0"));

		user.put(KEY_SMSURL, pref.getString(KEY_SMSURL, ""));

		user.put(KEY_WEBSITE, pref.getString(KEY_WEBSITE, null));
		user.put(KEY_DEVICEID, pref.getString(KEY_DEVICEID, null));

		user.put(KEY_EMPID, pref.getString(KEY_EMPID, "0"));

		user.put(KEY_BRANCHID, pref.getString(KEY_BRANCHID, "0"));
		user.put(KEY_DEPTID, pref.getString(KEY_DEPTID, "0"));

		user.put(KEY_LATTITUDE, pref.getString(KEY_LATTITUDE, ""));
		user.put(KEY_LONGTITUDE, pref.getString(KEY_LONGTITUDE, ""));
		user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, ""));

		user.put(KEY_PUSH_DESCR, pref.getString(KEY_PUSH_DESCR, ""));
		user.put(KEY_PUSH_TITLE, pref.getString(KEY_PUSH_TITLE, ""));

		user.put(KEY_PUSH_DATE, pref.getString(KEY_PUSH_DATE, ""));

		user.put(KEY_CURRENT_DATE, pref.getString(KEY_CURRENT_DATE, "null"));

		user.put(KEY_RECEIVECODE, pref.getString(KEY_RECEIVECODE, "0"));
		user.put(KEY_ACT_STATUS, pref.getString(KEY_ACT_STATUS, "0"));

		user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, ""));

		user.put(KEY_VERIFICATION_COUNTER, pref.getString(KEY_VERIFICATION_COUNTER, "0"));

		user.put(KEY_TYPE, pref.getString(KEY_TYPE, "null"));
		user.put(KEY_STUDNAME, pref.getString(KEY_STUDNAME, ""));
		user.put(KEY_STUDSTANDARD, pref.getString(KEY_STUDSTANDARD, ""));
		user.put(KEY_STUDDOB, pref.getString(KEY_STUDDOB, ""));
		user.put(KEY_STUDFATHERMOB, pref.getString(KEY_STUDFATHERMOB, ""));
		user.put(KEY_STUDMOTHERMOB, pref.getString(KEY_STUDMOTHERMOB, ""));
		user.put(KEY_STUDSTUDENTMOB, pref.getString(KEY_STUDSTUDENTMOB, ""));
		user.put(KEY_STUDGENDER, pref.getString(KEY_STUDGENDER, ""));
		user.put(KEY_STUDADDRESS, pref.getString(KEY_STUDADDRESS, ""));

		user.put(KEY_VERSTATUS, pref.getString(KEY_VERSTATUS, "0"));

		user.put(KEY_FACULTYNAME, pref.getString(KEY_FACULTYNAME, ""));
		user.put(KEY_ACADEMICYEAR, pref.getString(KEY_ACADEMICYEAR, ""));

		// return user
		return user;
	}

	public void ClearDetails() {

		editor.clear();

		editor.commit();
		/*
		 * // After logout redirect user to Loing Activity Intent i = new
		 * Intent(_context, ActivationProcess.class); // Closing all the
		 * Activities i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * 
		 * // Add new Flag to start new Activity
		 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * 
		 * // Staring Login Activity _context.startActivity(i);
		 */

	}

	/**
	 * Clear session details
	 */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, StartActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Staring Login Activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	/**
	 * Quick check for Activation
	 **/
	// Get Login State
	public boolean isActivated() {
		return pref.getBoolean(IS_ACTIVATED, false);
	}

}
