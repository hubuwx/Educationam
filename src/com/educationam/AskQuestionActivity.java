package com.educationam;



import com.educationam.AndroidMultiPartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class AskQuestionActivity extends ActionBarActivity {
	
	// LogCat tag
		private static final String TAG = AskQuestionActivity.class.getSimpleName();
		
	 
	    // Camera activity request codes
	    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	    
	    public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	 
	    private Uri fileUri; // file url to store image/video
	    

		private String SELECTEDFACULTYNAME="0";
		private String SELECTEDFACULTYID="0";
		private String ANS_STATUS="0";
		private String REPEAT_STATUS="0";
		
	    

	private String Title="Ask Question";
	private Context context=this;
	private Button btncapture;
	private Button btnsend;
	private Spinner spnFaculty;
	private EditText txtQuestion;
	private dbhandler db;
	private SessionManager sessionmanager;
	private SQLiteDatabase sd;
	private HashMap<String, String> userDetails;
	
	
	private ArrayList<String> lstFacname= new ArrayList<String>();
	private ArrayList<String> lstFacid = new ArrayList<String>();


	private String filePath;


	private TextView txtPercentage;


	private ProgressBar progressBar;


	private ImageView imgPreview;


	private VideoView vidPreview;

	long totalSize = 0;


	private String btnVISIBILITY="0";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ Title +"</font>"));
				

				
				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();


			} else {
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ Title +"</font>"));
				getActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().show();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			Intent in= getIntent();
			ANS_STATUS = ""+in.getStringExtra("ans_status");
			REPEAT_STATUS = ""+in.getStringExtra("repeat_status");
			btnVISIBILITY = ""+in.getStringExtra("visibility");
			
		}
		catch(Exception e)
		{
			Log.d("AskQuestinActivityIntent",""+e.getMessage());
		}
		
		btncapture = (Button)findViewById(R.id.btncapture);
		btnsend = (Button)findViewById(R.id.btnsend);
		spnFaculty  =(Spinner)findViewById(R.id.spnfaculty);
		txtQuestion = (EditText)findViewById(R.id.edtquestion);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
		//btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		vidPreview = (VideoView) findViewById(R.id.videoPreview);
		
		
		
		
		
		db = new dbhandler(context);

		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();
		
		sessionmanager = new SessionManager(context);
		userDetails  = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();
		
		
		
		
		
		
		
		btncapture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				captureImage();
				
			}
		});
		
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!txtQuestion.getText().toString().equals(""))
				{
					
					try {
						if(NetConnectivity.isOnline(context))
						{
							new UploadFileToServer().execute();	
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(AskQuestionActivity.this);
							builder.setMessage("Question has been submitted").setTitle("Info..")
									.setCancelable(false)
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											// do nothing
											Intent intent =new Intent(context,MainActivity.class);
											startActivity(intent);
											finish();
										}
									});
							AlertDialog alert = builder.create();
							alert.show();
							
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//SendQuesionToServer
				}
				else
				{
					Toast.makeText(context, "Please Enter Question", Toast.LENGTH_SHORT).show();
					
					Animation shake = AnimationUtils.loadAnimation(
							AskQuestionActivity.this, R.anim.shake);

					findViewById(R.id.edtquestion).startAnimation(shake);

					
				}
				
			}
		});
		
		
		
		
		String query = "select * from staffinfo";
		Cursor c = sd.rawQuery(query, null);
		
		if(c!=null)
		{
			lstFacid.clear();
			lstFacname.clear();
			while(c.moveToNext())
			{
				
				lstFacname.add(""+c.getString(c.getColumnIndex("facname")));
				lstFacid.add(""+c.getString(c.getColumnIndex("facid")));
				
				
			}
			
		}
		
		ArrayAdapter<String> facApdater =new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item	, lstFacname);
		
		spnFaculty.setAdapter(facApdater);
		
		spnFaculty.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String facname = lstFacname.get(position);
				
				SELECTEDFACULTYNAME = lstFacname.get(position);
				SELECTEDFACULTYID = lstFacid.get(position);
				
				//Toast.makeText(context, "Fac ID : "+lstFacid.get(position)+"Fac name:"+lstFacname.get(position), Toast.LENGTH_SHORT).show();
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		
		
		
		
		
	}
	
	 /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
 
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
 
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
 
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
 
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
 
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
 
        return mediaFile;
    }
	
    private void launchUploadActivity(boolean isImage){
    	
    	// image or video path that is captured in previous activity
    			filePath = fileUri.getPath();

    			// boolean flag to identify the media type, image or video
    		//	boolean isImage = i.getBooleanExtra("isImage", true);

    			if (filePath != null) {
    				// Displaying the image or video on the screen
    				previewMedia(isImage);
    			} else {
    				Toast.makeText(getApplicationContext(),
    						"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
    			}

    			
		
		
    	/*Intent i = new Intent(AskQuestionActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);*/
    }
    
	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		String imgname,ques;
		
		
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progressBar.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Making progress bar visible
			progressBar.setVisibility(View.VISIBLE);

			// updating progress bar value
			progressBar.setProgress(progress[0]);

			// updating percentage value
			txtPercentage.setText(String.valueOf(progress[0]) + "%");
		}

		@Override
		protected String doInBackground(Void... params) {
			
			
			File sourceFile;
			try {
				sourceFile = new File(filePath);
			 imgname =sourceFile.toString();
				imgname = imgname.substring(49,imgname.length());
				imgname = userDetails.get(SessionManager.KEY_EMPID)+imgname;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
///storage/emulated/0/Pictures/Android File Upload/IMG_20151125_162539.jpg
			// Adding file data to http body
			//entity.addPart("image", new FileBody(sourceFile));

		/*	String time =getDateTime();
			
			time = time.replace(" ", "%20");
			time = time.replace("-","%2D");
			time = time.replace(":","%3A");
			
			
			ques = ""+txtQuestion.getText().toString();
			ques = ques.replace(" ","%20");
			ques = ques.replace("!","%21");
			ques = ques.replace(" ","%22" );
			ques = ques.replace("#","%23");
			ques = ques.replace("$","%24");
			ques = ques.replace("%","%25");
			ques = ques.replace("&","%26");
			ques = ques.replace("'","%27");
			ques = ques.replace("(","%28");
			ques = ques.replace(")","%29");
			ques = ques.replace("-","%2D");
			ques = ques.replace(".","%2E" );
			ques = ques.replace(":","%3A");
			ques = ques.replace(";","%3B");
			ques = ques.replace( "?","%3F");
			ques = ques.replace("@","%40");	
			
			//fd
//http://uxledu.com/json_data.aspx?type=insertquestion&question=whatisdsadsadsaddsadas&facid=1&facname=arpit&imgname=gchv.jpg&datetime=11-24-2015%2012:12:12&studid=1614&ans_status=1			
			try {
				if(ANS_STATUS.equals("null"))
					ANS_STATUS="0";
				if(REPEAT_STATUS.equals("null"))
					REPEAT_STATUS="0";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query =AllKeys.WEBSITE+"json_data.aspx?type=insertquestion&question="+ ques +"&facid="+ SELECTEDFACULTYID +"&facname="+ SELECTEDFACULTYNAME +"&imgname="+ imgname +"&datetime="+ time +"&studid="+ userDetails.get(SessionManagement.KEY_EMPID) +"&ans_status=0&repeat="+ REPEAT_STATUS +"";
//http://uxledu.com/json_data.aspx?type=insertquestion&question=hi%2520sir&facid=1&facname=arpit&imgname=IMG_20151125_181435.jpg&datetime=11%2D25%2D2015%2018%3A14%3A49&studid=2026&ans_status=null&repeat=null			
			ServiceHandler sh =new ServiceHandler();
			String ans = sh.makeServiceCall(query, ServiceHandler.GET);
			System.out.print("Ans :"+ans);
*/			
			
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
				
				//ada
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				File sourceFile = new File(filePath);

				// Adding file data to http body
				entity.addPart("image", new FileBody(sourceFile));

				// Extra parameters if you want to pass to server
				entity.addPart("website",
						new StringBody(AllKeys.WEBSITE2));
				entity.addPart("email", new StringBody("sathishmicit2012@gmail.com"));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
					//File sourceFile;
					try {
						sourceFile = new File(filePath);
					 imgname =sourceFile.toString();
						imgname = imgname.substring(49,imgname.length());
						imgname = userDetails.get(SessionManager.KEY_EMPID)+imgname;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		///storage/emulated/0/Pictures/Android File Upload/IMG_20151125_162539.jpg
					// Adding file data to http body
					//entity.addPart("image", new FileBody(sourceFile));

					String time =getDateTime();
					
					time = time.replace(" ", "%20");
					time = time.replace("-","%2D");
					time = time.replace(":","%3A");
					
					
					ques = ""+txtQuestion.getText().toString();
					ques = ques.replace(" ","%20");
					ques = ques.replace("!","%21");
					ques = ques.replace("\"\"","%22" );
					ques = ques.replace("#","%23");
					ques = ques.replace("$","%24");
					ques = ques.replace("%","%25");
					ques = ques.replace("&","%26");
					ques = ques.replace("'","%27");
					ques = ques.replace("(","%28");
					ques = ques.replace(")","%29");
					ques = ques.replace("-","%2D");
					ques = ques.replace(".","%2E" );
					ques = ques.replace(":","%3A");
					ques = ques.replace(";","%3B");
					ques = ques.replace( "?","%3F");
					ques = ques.replace("@","%40");	
					
					//fd
		//http://uxledu.com/json_data.aspx?type=insertquestion&question=whatisdsadsadsaddsadas&facid=1&facname=arpit&imgname=gchv.jpg&datetime=11-24-2015%2012:12:12&studid=1614&ans_status=1			
					try {
						if(ANS_STATUS.equals("null"))
							ANS_STATUS="0";
						if(REPEAT_STATUS.equals("null"))
							REPEAT_STATUS="0";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String query =AllKeys.WEBSITE+"json_data.aspx?type=insertquestion&question="+ ques +"&facid="+ SELECTEDFACULTYID +"&facname="+ SELECTEDFACULTYNAME +"&imgname="+ imgname +"&datetime="+ time +"&studid="+ userDetails.get(SessionManager.KEY_EMPID) +"&ans_status=0&repeat="+ REPEAT_STATUS +"";
		//http://uxledu.com/json_data.aspx?type=insertquestion&question=hi%2520sir&facid=1&facname=arpit&imgname=IMG_20151125_181435.jpg&datetime=11%2D25%2D2015%2018%3A14%3A49&studid=2026&ans_status=null&repeat=null			
					ServiceHandler sh =new ServiceHandler();
					String ans = sh.makeServiceCall(query, ServiceHandler.GET);
					System.out.print("Ans :"+ans);

				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);

			// showing the server response in an alert dialog
			showAlert(result);

			super.onPostExecute(result);
		}

	}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Question has been submitted").setTitle("Question Info")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
						Intent intent =new Intent(context,MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
				
		Date date = new Date();
		return dateFormat.format(date);
	}


    /**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia(boolean isImage) {
		// Checking whether captured media is image or video
		if (isImage) {
			imgPreview.setVisibility(View.VISIBLE);
			vidPreview.setVisibility(View.GONE);
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			imgPreview.setImageBitmap(bitmap);
		} else {
			imgPreview.setVisibility(View.GONE);
			vidPreview.setVisibility(View.VISIBLE);
			vidPreview.setVideoPath(filePath);
			// start playing
			vidPreview.start();
		}
	}

	
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
 
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }
 
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
 
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
 
    
 
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                
            	// successfully captured the image
                // launching upload activity
            	launchUploadActivity(true);
            	
            	
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                
            	// video successfully recorded
                // launching upload activity
            	launchUploadActivity(false);
            
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if(item.getItemId() ==android.R.id.home)
		{
			
			
				Intent i=new Intent(context,MainActivity.class);
				startActivity(i);
				finish();
				//onBackPressed();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
