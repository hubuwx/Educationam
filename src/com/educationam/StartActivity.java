package com.educationam;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class StartActivity extends ActionBarActivity {

	private ExpandableListView mexpandablistview;
	ArrayList<Parent> arrayParents = new ArrayList<Parent>();
	ArrayList<String> arrayChildren = new ArrayList<String>();
	private Context context = this;
	private JSONParser jParser;
	private String url;
	private JSONObject json2;
	private JSONArray jarrayholiday;
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdaptorForStart customGridAdapter;
	private Timer timer;
	private TimerTask timerTask;
	private Handler handler = new Handler();
	private int interval = 3000;
	private int BannerCounter = 0;
	// private int[] myImages = new int[] { R.drawable.banner1,
	// R.drawable.banner2, R.drawable.banner3, R.drawable.banner4,
	// R.drawable.banner5 };
	private ViewPager viewPager;
	private TextView txtdns;
	static final String[] GRID_DATA = new String[] { "Windows", "iOS",
			"Android", "Blackberry", "Java", "JQuery", };
	SQLiteDatabase sd;
	private WebView wv;
	private Button btnlogin;
	private ImageView imglogin;
	private TextView txtnet;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		String title = "Educationam";
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title
						+ "</font>"));

				// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				// getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().hide();
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
				getActionBar().hide();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// getSupportActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#44223a")));
		// getSupportActionBar().hide();
		gridView = (GridView) findViewById(R.id.startgird);
		txtdns = (TextView) findViewById(R.id.txtdns);
		mexpandablistview = (ExpandableListView) findViewById(R.id.expandableListView1);
		Typeface font = Typeface.createFromAsset(getAssets(),
				"OpenSans-Light.ttf");// RobotoCondensed-Light.ttf
		txtdns.setTypeface(font);
		// initViewPager();
		dbhandler db = new dbhandler(context);
		sd = db.getReadableDatabase();
		jParser = new JSONParser();

		// btnlogin = (Button) findViewById(R.id.btnlogin);

		txtnet = (TextView) findViewById(R.id.txtnet);
		wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);

		if (NetConnectivity.isOnline(context)) {
			txtnet.setVisibility(View.GONE);
			wv.loadUrl(AllKeys.WEBSITE + "mobcontent.aspx?type=menu");
		} else {

		}
		try {
		} catch (Exception e) {
			System.out.print("Errror :" + e.getMessage());
		}

		Bitmap about = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_aboutus);
		Bitmap contact = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_contactus);
		Bitmap login = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.loginmine);

		gridArray.clear();
		gridArray.add(new Item(about, "About us"));
		gridArray.add(new Item(contact, "Contact us"));
		gridArray.add(new Item(login, "Login"));
		gridView = (GridView) findViewById(R.id.startgird);
		customGridAdapter = new CustomGridViewAdaptorForStart(context,
				R.layout.grid_row_start, gridArray);
		gridView.setAdapter(customGridAdapter);

		// String path = "android.resource://" + getPackageName() + "/"
		// + R.raw.video;
		// videoView.setVideoPath(path);
		// videoView.setMediaController(new
		// MediaController(StartActivity.this));
		// videoView.start();
		// videoView.requestFocus();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			private int HELLO_ID;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					if (NetConnectivity.isOnline(StartActivity.this)) {
						Intent i = new Intent(StartActivity.this,
								AboutusActivity.class);
						startActivity(i);
					} else {
						showDialog();
					}

				}

				else if (position == 1) {

					if (NetConnectivity.isOnline(StartActivity.this)) {
						Intent i = new Intent(StartActivity.this,
								ContactusActivity.class);
						startActivity(i);
					} else {
						showDialog();
					}
					// Intent i = new Intent(StartActivity.this,
					// ContactusActivity.class);
					// startActivity(i);
				} else if (position == 2) {
					Intent ii = new Intent(context, LoginActivity.class);
					ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(ii);
					finish();
				}
			}
		});

		mexpandablistview.setAdapter(new com.educationam.ExpandableListAdapter(
				context, arrayParents));
		mexpandablistview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub

				Parent p = arrayParents.get(groupPosition);
				if (p.getTitle().contains("Login")) {

					Intent i = new Intent(context, LoginActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					// finish();
					// }
				}
				if (p.getTitle().contains("Video")) {
					Intent i = new Intent(context, VideoActivity.class);
					startActivity(i);
				}
				return false;
			}
		});

	}

	public void FillDataonExpandalbeListview() {
		Cursor cs = sd.rawQuery("select * from MobileTitle", null);
		startManagingCursor(cs);
		if (cs.getCount() > 0) {
			populatelists();
		} else {

			populatelist();
		}
	}

	public void populatelist() {
		arrayParents.clear();
		Parent parent = new Parent();
		dbhandler db = new dbhandler(context);
		SQLiteDatabase sd = db.getReadableDatabase();

		// Cursor cdept=sd.rawQuery("select * from studinfo", null);
		// startManagingCursor(cdept);

		for (int i = 0; i < 5; i++) {
			parent = new Parent();

			{
				arrayChildren.add(" Title " + i);
			}
			parent.setArrayChildren(arrayChildren);

			// in this array we add the Parent object. We will use the
			// arrayParents at the setAdapter
			arrayParents.add(parent);
		}

		parent = new Parent();
		parent.setTitle("      Video");
		arrayChildren = new ArrayList<String>();
		parent.setArrayChildren(arrayChildren);
		arrayParents.add(parent);
		parent = new Parent();
		parent.setTitle("      Login");
		arrayChildren = new ArrayList<String>();
		parent.setArrayChildren(arrayChildren);
		arrayParents.add(parent);

	}

	private void showDialog() {
		final Dialog dialog = new Dialog(StartActivity.this);

		dialog.setContentView(R.layout.error_dialog);
		dialog.setTitle("No Internet Connection");

		final ImageView imgError = (ImageView) dialog
				.findViewById(R.id.imgError);

		dialog.show();
	}

	public void populatelists() {
		arrayParents.clear();
		Parent parent = new Parent();
		dbhandler db = new dbhandler(context);
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor cdept = sd.rawQuery("select * from MobileTitle", null);
		startManagingCursor(cdept);

		while (cdept.moveToNext()) {
			parent = new Parent();

			parent.setTitle("      " + cdept.getString(1));

			parent.setArrayChildren(arrayChildren);

			arrayChildren = new ArrayList<String>();
			arrayChildren.clear();
			// Cursor
			// c=sd.rawQuery("select * from time_tb_mst where day='"+days[i]+"'
			// and deptid="+deptid,
			// null);
			// startManagingCursor(c);
			// while(c.moveToNext())

			{
				arrayChildren.add("  " + cdept.getString(2));
			}
			parent.setArrayChildren(arrayChildren);

			// in this array we add the Parent object. We will use the
			// arrayParents at the setAdapter
			arrayParents.add(parent);
		}

		/*
		 * parent=new Parent(); parent.setTitle("      Video");
		 * arrayChildren=new ArrayList<String>();
		 * parent.setArrayChildren(arrayChildren); arrayParents.add(parent);
		 */
		parent = new Parent();
		parent.setTitle("      Login");
		arrayChildren = new ArrayList<String>();
		parent.setArrayChildren(arrayChildren);
		arrayParents.add(parent);
		mexpandablistview.setAdapter(new com.educationam.ExpandableListAdapter(
				context, arrayParents));

	}

	private void initViewPager() {
		// viewPager = (ViewPager)findViewById(R.id.view_pager);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				viewPager.setCurrentItem(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(BannerCounter);

	}

	// //For Loading the mobile site pages /////

	// //Complete loading pages from over the website////

	@Override
	protected void onResume() {
		super.onResume();
		stopTimer();
		startTimer();
	}

	@Override
	protected void onStop() {
		stopTimer();
		super.onStop();
	}

	private void startTimer() {
		if (timer == null) {

			timer = new Timer();
			timerTask = new MyTimerTask();
			timer.schedule(timerTask, 0, interval);
		}
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public class MyTimerTask extends TimerTask {

		private Runnable runnable = new Runnable() {
			public void run() {
				try {

					if (BannerCounter == 5) {
						BannerCounter = 0;
					} else {
						BannerCounter++;
					}
					viewPager.setCurrentItem(BannerCounter);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		public void run() {
			handler.post(runnable);
		}
	}

	private class ImagePagerAdapter extends PagerAdapter {
		// private ImageLoader imageLoader;

		public ImagePagerAdapter() {

		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView imageView = new ImageView(context);
			float screenWidth = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth();

			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			// imageView.setImageResource(myImages[position]);
			// imageView.setScaleType(ScaleType.CENTER_CROP);

			// imageloader.DisplayImage(listimg.get(position), imageView);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "", 1000).show();
				}
			});
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == ((ImageView) arg1);

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// do something on back.
			StartActivity.this.finish();// try activityname.finish instead of
										// this
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
