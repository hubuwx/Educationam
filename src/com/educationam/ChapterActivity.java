package com.educationam;



import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ChapterActivity extends ActionBarActivity {

	private  Context context=this;
	private List<Item> myItems = new ArrayList<Item>();
	private ListView lstmenu;
	ArrayList<String> listsubid=new ArrayList<String>();
	private String subid;
	private ArrayList<String> listvideourl=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		lstmenu=(ListView)findViewById(R.id.listView1);
		subid=getIntent().getStringExtra("subid");
		dbhandler db=new dbhandler(context);
		SQLiteDatabase sd=db.getReadableDatabase();
		
		Cursor csub=sd.rawQuery("select * from Video where subid="+subid, null);
		startManagingCursor(csub);
		Bitmap b = null;
		while(csub.moveToNext())
		{
			myItems.add(new Item(b, ""+csub.getString(1)));
			listsubid.add(csub.getString(5));
			listvideourl.add(csub.getString(2));
		}
		
		ArrayAdapter<Item> adapter = new MyListAdapter();
		
		lstmenu.setAdapter(adapter);
       registerClickCallback();

		
	}
	
	
	private void registerClickCallback() {
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			private Item pos;

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				//pos = myItems.get(position);
				String data=listvideourl.get(position);
				Intent i=new Intent(context,VideoTrackActivity.class);
				i.putExtra("url", listvideourl.get(position));
				startActivity(i);
			}
		});
	}
	private class MyListAdapter extends ArrayAdapter<Item> {
		

		public MyListAdapter() {
			super(context, R.layout.fragment_video, myItems);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.fragment_video, parent, false);
			}
			
			// Find the car to work with.
			Item currentItem = myItems.get(position);
			
			// Fill the view
			
			
			// Make:
			TextView makeText = (TextView) itemView.findViewById(R.id.textView1);
			makeText.setText(currentItem.title);
		

			return itemView;
		}				
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
			View rootView = inflater.inflate(R.layout.fragment_chapter,
					container, false);
			return rootView;
		}
	}

}
