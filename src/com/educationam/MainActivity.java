package com.educationam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.ActionBarDrawerToggle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	AsyncTask<Void, Void, Void> mRegisterTask;
	boolean doubleBackToExitPressedOnce = false;
	private String[] mMenuTitles;
	private ListView mDrawerList;
	LinearLayout llpoweredbycolor;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int pos = 100;
	private GridView gridview;
	String[] str = { "a", "d", "sad", "asd", "sda", "asdf", "asd", "asdfasd",
			"sadf", "", "asdf", "" };
	private Context context = this;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdaptor customGridAdapter;
	private GridView gridView;
	private String url;
	private String url1;
	private String deptid;
	private String empid;
	private JSONArray jarrayholiday;
	private String date;
	private MenuItem nametext;
	private String testid;
	private JSONObject jsonnotes;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	String time;
	ProgressBar prgShow;
	Menu mymenu;
	private ArrayList<String> lstMessage = new ArrayList<String>();
	String AppTitle = "Educationam";
	String ThemeColor = "";// #FF8C00
	private String CurrentDate = "";
	JSONParser json = new JSONParser();
	private dbhandler db;
	private SQLiteDatabase sd;
	TextView lblSyncText;
	String welcome_msg, type;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {

			db = new dbhandler(context);
			sd = db.getReadableDatabase();
			sd = db.getWritableDatabase();
			ThemeColor = "#FF8C00";
			// sd.delete("Notification_Mst", null, null);

			sessionmanager = new SessionManager(context);
			userDetails = new HashMap<String, String>();
			userDetails = sessionmanager.getUserDetails();
			llpoweredbycolor = (LinearLayout) findViewById(R.id.llpoweredbycolor);
			type = userDetails.get(SessionManager.KEY_TYPE);
			// prgShow = (ProgressBar) findViewById(R.id.pgrShow);
			// lblSyncText = (TextView) findViewById(R.id.lblSyncText);

			/*
			 * if (type == "F" || type.equals("F")) { ThemeColor = "#8BC34A";
			 * 
			 * llpoweredbycolor.setBackgroundColor(Color.rgb(139, 195, 74)); }
			 * else { ThemeColor = "#FF8C00";
			 * llpoweredbycolor.setBackgroundColor(Color.rgb(255, 140, 0)); }
			 */
			// #8BC34A
			try {
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();
				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(ThemeColor)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				/*
				 * deptid = "" + ii.getStringExtra("DEPTID"); empid = "" +
				 * ii.getStringExtra("STUDENTID");
				 */

				deptid = userDetails.get(SessionManager.KEY_DEPTID);
				empid = userDetails.get(SessionManager.KEY_EMPID);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (Integer.parseInt(userDetails.get(SessionManager.KEY_EMPID)) > 0
				&& !userDetails.get(SessionManager.KEY_EMPID).equals("null")) {

			try {
				time = getDateTime();
				if (time.contains(userDetails
						.get(SessionManager.KEY_CURRENT_DATE))
						&& !userDetails.get(SessionManager.KEY_CURRENT_DATE)
								.equals("")) {
					// Toast.makeText(context,
					// "Contain",Toast.LENGTH_SHORT).show();

				} else {
					if (NetConnectivity.isOnline(context)) {
						final ProgressDialog progress = ProgressDialog.show(
								MainActivity.this, "", "Please wait....", true,
								false);

						new Thread(new Runnable() {
							public void run() {
								// Write Statements Here

								CurrentDate = time.substring(0, 10);
								sessionmanager.CreateCurrentDate(""
										+ CurrentDate);

								time = time.replace(" ", "%20");
								time = time.replace("-", "%2D");
								time = time.replace(":", "%3A");

								String LoginCheckUrl = AllKeys.WEBSITE
										+ "json_data.aspx?type=tracklogin&empid="
										+ userDetails
												.get(SessionManager.KEY_EMPID)
										+ "&logintime=" + time + "";

								json.getJSONFromUrl(LoginCheckUrl);
								// System.out.print("Ans :" + ans);
								progress.cancel();
							}
						}).start();

						// Toast.makeText(context,
						// "Not Contain"+time,Toast.LENGTH_SHORT).show();
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * gridview=(GridView)findViewById(R.id.gridView1);
		 * 
		 * gridview.setAdapter(new
		 * ArrayAdapter<String>(context,android.R.layout.
		 * simple_list_item_1,str));
		 */

		// /////////Complete Checking GCM //////////
		try {
			// registerReceiver(mHandleMessageReceiver, new
			// IntentFilter(DISPLAY_MESSAGE_ACTION));
		} catch (Exception e) {
			System.out.print("Err : " + e.getMessage());
		}
		Bitmap notification = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.inbox_notification);

		Paint circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.RED);

		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setAlpha(255);
		circlePaint.setStrokeWidth(10);

		Paint textPaint = new Paint();
		textPaint.setStyle(Style.FILL_AND_STROKE);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(12);
		textPaint.setFakeBoldText(true);

		Bitmap attendece = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.attendance);
		Bitmap performance = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.performance);
		Bitmap homework = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.homework);
		Bitmap social = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.social);
		Bitmap planner = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.planner);

		Bitmap testsolution = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.test_solution);
		Bitmap gallery = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.gallery);
		Bitmap Achievers = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.achievers);
		Bitmap events = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.events);
		Bitmap upcomingtests = BitmapFactory.decodeResource(
				this.getResources(), R.drawable.upcomming_exam);
		Bitmap leave = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.request);
		Bitmap parent = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.request);
		Bitmap student = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.request);
		// gridArray.add(new Item(batch, "Batch Request"));
		// gridArray.add(new Item(leave, "Leave Request"));

		gridArray.clear();
		gridArray.add(new Item(notification, "Notification"));

		gridArray.add(new Item(Achievers, "Achievers"));
		gridArray.add(new Item(attendece, "Attendance"));
		gridArray.add(new Item(upcomingtests, "Upcoming Tests"));
		gridArray.add(new Item(performance, "Marks"));
		gridArray.add(new Item(testsolution, "Test Solution"));
		gridArray.add(new Item(homework, "Homework"));
		gridArray.add(new Item(events, "Events"));
		gridArray.add(new Item(social, "Gyan Capsule"));
		gridArray.add(new Item(planner, "Planner"));
		gridArray.add(new Item(leave, "Request"));
		gridArray.add(new Item(student, "Follow Us"));
		// gridArray.add(new Item(social, "Follow Us"));
		// if (type == "F" || type.equals("F")) {
		//
		// gridArray.add(new Item(parent, "Parent Meeting"));
		// } else {
		// gridArray.add(new Item(student, "Follow Us"));
		// }
		// gridArray.add(new Item(homework, "Homework"));
		// // gridArray.add(new Item(achievements, "Achievements"));
		//
		// gridArray.add(new Item(event, "Academic Calendar"));
		//
		// gridArray.add(new Item(businfo, "Bus Info."));
		//
		// gridArray.add(new Item(feedback, "Feedback"));
		//
		// gridArray.add(new Item(parentlinks, "Useful Links"));
		//
		// gridArray.add(new Item(leave, "Leave Details"));

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdaptor(context,
				R.layout.gridrow, gridArray);
		gridView.setAdapter(customGridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			private int HELLO_ID;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mDrawerToggle.setDrawerIndicatorEnabled(false);
				// Highlight the selected item, update the title, and close the
				// drawer
				mDrawerList.setItemChecked(position, true);
				// setTitle(mPlanetTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
				pos = position;
				if (position == 0) {

					// notification

					Intent intent = new Intent(context,
							NotificationActivity.class);
					startActivity(intent);
				}

				else if (position == 1) {

					// attendance

				} else if (position == 2) {
					// peroformance

					setFragment(new AttendenceFrag());

					getSupportActionBar().show();

				} else if (position == 3) {

					// homework

					setTitle("Upcoming Tests");
					setFragment(new UpcomingTests());
					getSupportActionBar().show();

				} else if (position == 4) {

					// Upcoming Tests
					setTitle("Marks");
					setFragment(new ResultFrag());
					getSupportActionBar().show();

				} else if (position == 5) {

					// test solution
					setTitle("Test Solutions");
					setFragment(new TestSolution());
					getSupportActionBar().show();

					/*
					 * setTitle("Student Activity"); setFragment(new
					 * SActivityFrag()); getSupportActionBar().show();
					 */
				}

				else if (position == 6) {

					// Gallery
					setTitle("Homework");
					setFragment(new FragmentHomework());
					getSupportActionBar().show();

					// Toast.makeText(context, "Online Test In Process",
					// Toast.LENGTH_SHORT).show();
					/*
					 * setTitle("Notice Board"); setFragment(new
					 * NoticeBoardActivity()); getSupportActionBar().show();
					 */

				} else if (position == 7) {

					// event
					setTitle("Events");
					setFragment(new Events());
					getSupportActionBar().show();

					// Toast.makeText(context, "Notification Hub",
					// Toast.LENGTH_SHORT).show();

					// Notification Hub
					/*
					 * setTitle("Links"); setFragment(new LinksActivity());
					 * getSupportActionBar().show();
					 */

				} else if (position == 8) {

					// Social

					// Toast.makeText(context, "Feedback ", Toast.LENGTH_SHORT)
					// .show();
					// Toast.makeText(context, "Ask Question",
					// Toast.LENGTH_SHORT).show();

				} else if (position == 9) {

					setTitle("Planner");
					setFragment(new TimeTableActivity());
					getSupportActionBar().show();

				} else if (position == 10) {

					Intent intent = new Intent(context, LeaveActivity.class);
					startActivity(intent);

				} else if (position == 11) {
					setTitle("Follow Us");
					setFragment(new Social());
					getSupportActionBar().show();

				}
				// if (type == "F" || type.equals("F")) {
				//
				// setTitle("Parent Meeting");
				// setFragment(new ParentComplaintsActivity());
				// getSupportActionBar().show();
				// } else {
				// setTitle("Extra Batch Request");
				// setFragment(new BatchRequestActivity());
				// getSupportActionBar().show();
				// }

			}

		});

		mTitle = mDrawerTitle = getTitle();

		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// mDrawerList.setDividerHeight(2);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// Prevent the drawer from opening when the user swipes from the left
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mMenuTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.i_drawer, /*
							 * nav drawer icon to replace 'Up' caret
							 */
		R.string.drawer_open) {

			/**
			 * Called when a drawer has settled in a completely closed state.
			 */
			public void onDrawerClosed(View view) {
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				supportInvalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			mDrawerToggle.setDrawerIndicatorEnabled(true);
		} else {
			mDrawerToggle.setDrawerIndicatorEnabled(false);
		}
	}

	@Override
	public void onBackPressed() {
		try {
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit",
					Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.mymenu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		nametext = menu.findItem(R.id.action_name);

		dbhandler db = new dbhandler(context);
		SQLiteDatabase sd = db.getReadableDatabase();

		String srr = "select * from studinfo where empid="
				+ userDetails.get(sessionmanager.KEY_EMPID);
		Cursor cdept = sd.rawQuery("select * from studinfo where empid="
				+ userDetails.get(sessionmanager.KEY_EMPID), null);
		startManagingCursor(cdept);
		while (cdept.moveToNext()) {

			String name = cdept.getString(2);

			nametext.setTitle(cdept.getString(2));
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		// Handle action buttons
		switch (item.getItemId()) {
		case android.R.id.home:
			// onBackPressed();

			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);
			} else {
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
				if (drawerOpen) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
			return true;

		case R.id.action_profile:

			setFragment(new FragmentProfile());

			return true;
		case R.id.action_backup:
			backupDB();
			return true;
		case R.id.action_restore:
			importDB();
			return true;
		case R.id.action_sync:
			try {
				// new SyncDetailsTask().executFse();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		case R.id.action_logout:
			context.deleteDatabase(dbhandler.db);
			sessionmanager.logoutUser();
			Intent intent = new Intent(context, StartActivity.class);
			startActivity(intent);
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showDialog() {
		final Dialog dialog = new Dialog(MainActivity.this);

		dialog.setContentView(R.layout.error_dialog);
		dialog.setTitle("No Internet Connection");

		final ImageView imgError = (ImageView) dialog
				.findViewById(R.id.imgError);

		dialog.show();
	}

	private void showWelcome() {
		final Dialog dialog = new Dialog(MainActivity.this);

		dialog.setContentView(R.layout.thanksmessagedialog);
		dialog.setTitle("Radint Umra");

		final TextView txtWelcome = (TextView) dialog
				.findViewById(R.id.txtTanksMessage);

		txtWelcome.setText("Welcome");
		dialog.show();
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			pos = position;
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		switch (position) {

		case 0:
			// setTitle(""+mMenuTitles[position]);
			// Create a new fragment and show it
			setFragment(new FragmentProfile());
			break;
		case 1:
			setTitle("" + mMenuTitles[position]);
			Intent i = new Intent(context, AboutusActivity.class);
			startActivity(i);
			finish();
			break;
		case 2:
			setTitle("" + mMenuTitles[position]);
			// Create a new fragment and show it
			Intent ii = new Intent(context, ContactusActivity.class);
			startActivity(ii);
			break;

		}

		mDrawerToggle.setDrawerIndicatorEnabled(false);

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		// setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void setFragment(Fragment fragment) {

		FrameLayout fm = (FrameLayout) findViewById(R.id.content_frame);
		fm.setBackgroundColor(Color.BLACK);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		ft.replace(R.id.content_frame, fragment);
		gridArray.clear();

		gridView.setAdapter(new CustomGridViewAdaptor(context,
				android.R.layout.simple_list_item_1, gridArray));
		ft.addToBackStack(null);
		// Commit the transaction
		ft.commit();

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void backupDB() {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + context.getPackageName()
				+ "/databases/" + dbhandler.db + "";
		String backupDBPath = dbhandler.db;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(MainActivity.this, "DB Exported!", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importDB() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "//data//" + "parentapp.com"
						+ "//databases//" + "studdatas.db";
				String backupDBPath = "studdb.db";
				File backupDB = new File(data, currentDBPath);
				File currentDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getApplicationContext(), "Import Successful!",
						Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(), "Import Failed!",
					Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mTitle.equals("Student Information") || (mTitle.equals("Parent"))) {
		} else
			outState.putString("pos", "" + pos);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.

		String poss = savedInstanceState.getString("pos");

		if (poss != null) {
			pos = Integer.parseInt(poss);
			selectItem(pos);
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// do something on back.
			super.onBackPressed();
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + AppTitle
						+ "</font>"));
				// turn on the Navigation Drawer image; this is called in the
				// LowerLevelFragments
				getSupportActionBar().show();

				mDrawerToggle.setDrawerIndicatorEnabled(true);

				Bitmap notification = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.inbox_notification);

				Paint circlePaint = new Paint();
				circlePaint.setAntiAlias(true);
				circlePaint.setColor(Color.RED);

				circlePaint.setStyle(Paint.Style.FILL);
				circlePaint.setAlpha(255);
				circlePaint.setStrokeWidth(10);

				Paint textPaint = new Paint();
				textPaint.setStyle(Style.FILL_AND_STROKE);
				textPaint.setColor(Color.WHITE);
				textPaint.setTextSize(12);
				textPaint.setFakeBoldText(true);

				Bitmap attendece = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.attendance);
				Bitmap performance = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.performance);
				Bitmap homework = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.homework);
				Bitmap social = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.social);
				Bitmap planner = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.planner);

				Bitmap testsolution = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.test_solution);
				Bitmap gallery = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.gallery);
				Bitmap Achievers = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.achievers);
				Bitmap events = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.events);
				Bitmap upcomingtests = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.upcomming_exam);
				Bitmap leave = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.request);
				Bitmap parent = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.request);
				Bitmap student = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.request);
				// gridArray.add(new Item(batch, "Batch Request"));
				// gridArray.add(new Item(leave, "Leave Request"));

				gridArray.clear();
				gridArray.add(new Item(notification, "Notification"));

				gridArray.add(new Item(Achievers, "Achievers"));
				gridArray.add(new Item(attendece, "Attendance"));
				gridArray.add(new Item(upcomingtests, "Upcoming Tests"));
				gridArray.add(new Item(performance, "Marks"));
				gridArray.add(new Item(testsolution, "Test Solution"));
				gridArray.add(new Item(homework, "Homework"));
				gridArray.add(new Item(events, "Events"));
				gridArray.add(new Item(social, "Gyan Capsule"));
				gridArray.add(new Item(planner, "Planner"));
				gridArray.add(new Item(leave, "Request"));
				gridArray.add(new Item(student, "Follow Us"));
				/*
				 * if (type == "F" || type.equals("F")) {
				 * 
				 * gridArray.add(new Item(parent, "Parent Meeting")); } else {
				 * gridArray.add(new Item(student, "Extra Batch Request")); }
				 */
				customGridAdapter = new CustomGridViewAdaptor(context,
						R.layout.gridrow, gridArray);
				gridView.setAdapter(customGridAdapter);

			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void clearBackStack() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager
					.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			// PaymyreviewGSSThis sms Only From satish sad sad asnd nas dns and
			// sand nsa dnsa ndsandnsa dnsa dns nd sand nsa dn sdn sand san dnsa
			// dnsa nd asd
			// String
			// gg=""+userDetails.get(SessionManagement.KEY_GCMACTIVATION);
			// if(!newMessage.equals("") && gg.equals("true"))
			String query;
			if (newMessage.contains("GSS")) {

				// Waking up mobile if it is sleeping
				WakeLocker.acquire(getApplicationContext());
				// Showing received message
				// lblMessage.append(newMessage + "\n");

				// sd.execSQL("insert into NotificationMst values(null,'DNS IT
				// EXPERTS','"+
				// newMessage +"','datetime()','05:10')");
				String cdate = getDateTime();

				sessionmanager.StorePushNotification("", newMessage, cdate);
				// Notification_Mst(id INTEGER PRIMARY KEY AUTOINCREMENT,header
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
						+ lstMessage.get(0) + "','" + lstMessage.get(1) + "','"
						+ cdate + "')";

				System.out.print("Current Date : " + cdate);

				// sd.execSQL("insert into NotificationMst
				// values(null,'paymyreview.in','"+
				// newMessage +"','"+ cdate +"','05:10')");
				// Toast.makeText(getApplicationContext(), "New Message: " +
				// newMessage, Toast.LENGTH_LONG).show();
				// Releasing wake lock
				WakeLocker.release();

			}

		}
	};

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM-dd-yyyy HH:mm:ss", Locale.getDefault());

		Date date = new Date();
		return dateFormat.format(date);
	}

	// App rating

	public static class AppRater {
		private final static String APP_TITLE = "Radiant app";// App Name
		private final static String APP_PNAME = "com.educationam";// Package
																	// Name

		private final static int DAYS_UNTIL_PROMPT = 3;// Min number of days
		private final static int LAUNCHES_UNTIL_PROMPT = 3;// Min number of
															// launches

		public static void app_launched(Context mContext) {
			SharedPreferences prefs = mContext.getSharedPreferences("apprater",
					0);
			// if (prefs.getBoolean("dontshowagain", false)) { return ; }

			SharedPreferences.Editor editor = prefs.edit();

			// Increment launch counter
			long launch_count = prefs.getLong("launch_count", 0) + 1;
			editor.putLong("launch_count", launch_count);

			// Get date of first launch
			Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
			if (date_firstLaunch == 0) {
				date_firstLaunch = System.currentTimeMillis();
				editor.putLong("date_firstlaunch", date_firstLaunch);
			}
			showRateDialog(mContext, editor);
			// Wait at least n days before opening
			if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
				if (System.currentTimeMillis() >= date_firstLaunch
						+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
					showRateDialog(mContext, editor);
				}
			}

			editor.commit();
		}

		public static void showRateDialog(final Context mContext,
				final SharedPreferences.Editor editor) {
			final Dialog dialog = new Dialog(mContext);
			dialog.setTitle("Rate " + APP_TITLE);

			LinearLayout ll = new LinearLayout(mContext);
			ll.setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			// params.setMargins(left, top, right, bottom);
			params.setMargins(8, 8, 8, 8);

			TextView tv = new TextView(mContext);
			tv.setText("If you enjoy using "
					+ APP_TITLE
					+ ", please take a moment to rate it. Thanks for your support!");
			tv.setWidth(240);
			tv.setPadding(4, 0, 4, 10);
			tv.setLayoutParams(params);
			ll.addView(tv);

			Button b1 = new Button(mContext);
			b1.setBackgroundResource(R.color.dark_blue);
			b1.setTextColor(Color.parseColor("#FFFFFF"));
			b1.setText("Rate " + APP_TITLE);

			b1.setLayoutParams(params);
			b1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=" + APP_PNAME)));
					dialog.dismiss();
				}
			});
			ll.addView(b1);

			Button b2 = new Button(mContext);
			b2.setText("Remind me later");
			b2.setBackgroundResource(R.color.dark_blue);
			b2.setTextColor(Color.parseColor("#FFFFFF"));
			b2.setLayoutParams(params);
			b2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			ll.addView(b2);

			Button b3 = new Button(mContext);
			b3.setBackgroundResource(R.color.dark_blue);
			b3.setTextColor(Color.parseColor("#FFFFFF"));
			b3.setText("No, thanks");
			b3.setLayoutParams(params);
			b3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (editor != null) {
						editor.putBoolean("dontshowagain", true);
						editor.commit();
					}
					dialog.dismiss();
				}
			});
			ll.addView(b3);

			dialog.setContentView(ll);
			dialog.show();
		}
	}

	// Complete app rating

}
