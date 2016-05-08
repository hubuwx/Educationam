package com.educationam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.os.Build;

public class AdminNotesActivity extends ActionBarActivity {

	public Context context = this;
	private ListView listView;
	private ArrayList<ListItem> noteslist = new ArrayList<ListItem>();
	ArrayList<String> arr = new ArrayList<String>();
	private Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_notes);

		getSupportActionBar().show();

		listView = (ListView) findViewById(R.id.listView1);

		Cursor curnotes = getdata();
		getallData(curnotes);

		listView.setAdapter(new CustomAdapter(context,
				android.R.layout.simple_list_item_1, noteslist));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, AddNotesActivity.class);
				i.putExtra("id", "" + arr.get(position));
				i.putExtra("Edit", "true");
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_notes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_addnote) {
			Intent i = new Intent(context, AddNotesActivity.class);
			i.putExtra("id", "false");
			i.putExtra("Edit", "true");
			startActivity(i);

			return true;
		}
		if (id == R.id.action_backup) {
			backupDB();

			return true;
		}
		if (id == R.id.action_importall) {
			importDB();
			return false;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_admin_notes,
					container, false);
			return rootView;
		}
	}

	public Cursor getdata() {
		dbhandler db = new dbhandler(context);
		SQLiteDatabase sd = db.getReadableDatabase();

		c = sd.rawQuery("SELECT * FROM NOTES Order by id DESC", null);
		startManagingCursor(c);

		return c;
	}

	public void getallData(Cursor c) {
		noteslist.clear();
		arr.clear();
		while (c.moveToNext()) {
			noteslist.add(new ListItem(c.getString(1), c.getString(3)));
			arr.add("" + c.getInt(0));
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// do something on back.

			Intent intent = new Intent(context, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		return false;
	}

	public void backupDB() {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + "com.pravin.dboard"
				+ "/databases/studdata.db";
		String backupDBPath = "studdata.db";
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Restore Function
	private void importDB() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "//data//" + "com.pravin.dboard"
						+ "//databases//" + "studdata.db";
				String backupDBPath = "hotel.db";
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

}
