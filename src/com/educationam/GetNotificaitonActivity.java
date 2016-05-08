package com.educationam;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class GetNotificaitonActivity extends ActionBarActivity {

	private String msg;
	private Context context=this;
	private String response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_notificaiton);
		msg=getIntent().getStringExtra("msg");
		
		//Toast.makeText(context, ""+msg, 1000).show();
		
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompts, null);

		
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    private String studmobile;

				@Override
				public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
			    	dialog.cancel();
			    	msg=userInput.getText().toString();
			    	dbhandler db=new dbhandler(context);
			    	SQLiteDatabase sd=db.getReadableDatabase();
			    	
			    	Cursor c=sd.rawQuery("select * from mobile", null);
;			    			startManagingCursor(c);
					while(c.moveToNext())
					{
						studmobile=c.getString(0);
						
					}
					String api="http://bulksms.dnsitexperts.com/DontSendsendsms.php?username=modulus&pass=dns@123&senderid=DNSITE&dest_mobileno="+studmobile+"&message="+msg; 
					
					loadview(api);
			    	
			    		
					
			    	
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    @Override
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
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
			View rootView = inflater.inflate(
					R.layout.fragment_get_notificaiton, container, false);
			return rootView;
		}
	}
	
	public void loadview(String url)
	{
		String []arrmessage=url.split(" ");
		String msg = "";
		for(int i=0;i<arrmessage.length;i++)
		{
			if(i==arrmessage.length-1)
				msg=msg+arrmessage[i];
			else
				msg=msg+arrmessage[i]+"%20";
		}
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(msg);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			//Toast.makeText(context, "SMS sent ", Toast.LENGTH_LONG).show();
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	

}
