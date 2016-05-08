package com.educationam;

import static com.educationam.CommonUtilities.SENDER_ID;
import static com.educationam.CommonUtilities.displayMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "Your device registred with GCM");
		ServerUtilities.register(context, LoginActivity.MOBILE, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message");
		String title = intent.getExtras().getString("title");

		System.out.println("MESSAGE : " + message + " TITLE :" + title);
		String cdate = getDateTime();
		String query = "insert into Notification_Mst values(null,'" + title
				+ "','" + message + "','" + cdate + "')";
		dbhandler db = new dbhandler(context);
		SQLiteDatabase sd = db.getWritableDatabase();
		sd.execSQL(query);

		System.out.print("Current Date : " + cdate);
		System.out.print("Not Query : " + query);

		String dismessage = title + "GSS" + message;
		displayMessage(context, dismessage);

		// notifies user

		// dbhandler db=new dbhandler(context);
		// SQLiteDatabase sd=db.getWritableDatabase();
		// sd.execSQL("INSERT into notificationmsg values('','"+message+"')");
		displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
		generateNotification(context, message, title);
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
				Locale.getDefault());

		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	private static void generateNotification(Context context, String message,
			String title) {

		int icon = R.drawable.ic_launcher;

		int mNotificationId = 001;
		Intent notificationIntent = new Intent(context,
				NotificationActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		Notification notification = mBuilder
				.setSmallIcon(icon)
				.setTicker("EDUCATIONAM")
				.setWhen(0)
				.setAutoCancel(true)
				.setContentTitle("" + title)

				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message))
				.setContentIntent(resultPendingIntent)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.ic_launcher))
				.setContentText(message)
				.addAction(icon, "EDUCATIONAM", resultPendingIntent).build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(mNotificationId, notification);

	}

}
