package com.educationam;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

/**
 * @author Prabu
 *
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
	private static PendingIntent intent;
	private String name;
	private String eid;

	@Override
	public void onReceive(Context context, Intent intent) {
		/*
		 * String id=intent.getStringExtra("id"); String
		 * type=intent.getStringExtra("Type"); String
		 * warn=intent.getStringExtra("warn");
		 * 
		 * String phone=intent.getStringExtra("phone"); String
		 * msg=intent.getStringExtra("msg");
		 */
		/*
		 * if((type.equals("Sms"))) { //SmsManager smsManager =
		 * SmsManager.getDefault(); //smsManager.sendTextMessage(""+phone, null,
		 * ""+msg, null, null); dbhandler db=new dbhandler(context);
		 * SQLiteDatabase sd=db.getReadableDatabase();
		 * 
		 * sd.execSQL("update newsms set status=1 where smsid="+id);
		 * 
		 * 
		 * } else
		 */

		name = intent.getStringExtra("name");
		eid = intent.getStringExtra("eid");
		generateNotification(context, name, eid);

		// Vibrate the mobile phone
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
	}

	@SuppressLint("NewApi")
	private static void generateNotification(Context context, String message,
			String type) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context,
				GetNotificaitonActivity.class);
		notificationIntent.putExtra("msg", message);
		// AlertDialogManager alert=new AlertDialogManager();
		// alert.showAlertDialog(context, "Birhtday", "Happy Birthday "+message,
		// true);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent = PendingIntent.getActivity(context, 0, notificationIntent,
				PendingIntent.FLAG_ONE_SHOT);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// notification.sound=
		// Uri.parse("android.resource://com.pravin.schedularproj/" +
		// R.raw.reminder);
		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "reminder.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

}
