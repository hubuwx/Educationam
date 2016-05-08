package com.educationam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	private String name;
	private String eid;

	@Override
	public void onReceive(Context context, Intent intent) {
		name = intent.getStringExtra("name");
		eid = intent.getStringExtra("eid");
		Intent service1 = new Intent(context, MyAlarmService.class);
		service1.putExtra("name", name);
		service1.putExtra("eid", eid);
		context.startService(service1);

	}

}
