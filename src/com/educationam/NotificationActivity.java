package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NotificationActivity extends ListActivity {

	ListView lst;

	dbhandler db;
	SQLiteDatabase sd;
	Cursor c;
	private String ThemeColor;
	ArrayList<HashMap<String, String>> allNotifications;
	private SessionManager session;
	private HashMap<String, String> userdetails;
	private Context context = this;

	private TextView back, lblnotificationpoweredby;

	private String type;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		setTitle("Notifications");

		session = new SessionManager(getApplicationContext());
		userdetails = new HashMap<String, String>();
		userdetails = session.getUserDetails();
		type = userdetails.get(SessionManager.KEY_TYPE);
		back = (TextView) findViewById(R.id.back);
		lblnotificationpoweredby = (TextView) findViewById(R.id.lblnotificationpoweredby);
		/*if (type == "F" || type.equals("F")) {

			back.setBackgroundColor(Color.rgb(139, 195, 74));
			lblnotificationpoweredby.setBackgroundColor(Color.rgb(139, 195, 74));
		} else {

			back.setBackgroundColor(Color.rgb(255, 140, 0));
			lblnotificationpoweredby.setBackgroundColor(Color.rgb(255, 140, 0));
		}*/

		lst = getListView();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();

			}
		});

		allNotifications = new ArrayList<HashMap<String, String>>();

		db = new dbhandler(context);
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		FillNotifications();

	}

	public void FillNotifications() {

		// String qq="Select * from NotificationMst";
		String qq = "select * from Notification_Mst order by id DESC limit 15";
		System.out.print("Notification Query :" + qq);
		// select * from (select * from tblmessage order by sortfield ASC limit
		// 10) order by sortfield DESC
		c = sd.rawQuery(qq, null);
		while (c.moveToNext()) {
			HashMap<String, String> details = new HashMap<String, String>();
			details.put("HEADER", c.getString(1));
			details.put("NOTIFICATION", c.getString(2));
			details.put("DATE", c.getString(3));

			allNotifications.add(details);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, allNotifications, R.layout.list_single_notification,
				new String[] { "HEADER", "NOTIFICATION", "DATE" },
				new int[] { R.id.txtheading, R.id.txtnote, R.id.txtdate });
		lst.setAdapter(adapter);

	}

}
