package com.educationam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyAlarmService extends Service
{
    
	   private NotificationManager mManager;
	private String name;
	private String eid;
	private int reqcode;
	 
	    @Override
	    public IBinder onBind(Intent arg0)
	    {
	       // TODO Auto-generated method stub
	        return null;
	    }
	 
	    @Override
	    public void onCreate()
	    {
	       // TODO Auto-generated method stub 
	       super.onCreate();
	    }
	 
	   @SuppressWarnings("static-access")
	   @Override
	   public void onStart(Intent intent, int startId)
	   {
	       super.onStart(intent, startId);
	       name=intent.getStringExtra("name");
	       eid=intent.getStringExtra("eid");
	      reqcode=Integer.parseInt(eid);
	       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
	       Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
	     
	       Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());
	       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),reqcode, intent1,PendingIntent.FLAG_ONE_SHOT);
	       notification.flags |= Notification.FLAG_AUTO_CANCEL;
	       notification.setLatestEventInfo(this.getApplicationContext(), "Happy Birthday", name+"This is a test message!", pendingNotificationIntent);
	       notification.defaults |= Notification.DEFAULT_SOUND;
	       mManager.notify(0, notification);
	    }
	 
	    @Override
	    public void onDestroy()
	    {
	        // TODO Auto-generated method stub
	        super.onDestroy();
	    }
	 
	}