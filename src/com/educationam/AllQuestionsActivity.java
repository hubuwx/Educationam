package com.educationam;

import java.util.ArrayList;

import com.google.android.youtube.player.internal.c;




import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AllQuestionsActivity extends ActionBarActivity {

	private String AppTitle="All Questions";
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;
	private Context context=this;
	private com.educationam.dbhandler db;
	private SQLiteDatabase sd;
	private ListView lstQuestions;
	private Button btnaskquestion;
	
	
	private ArrayList<HashMap<String, String>> allDetails;
	
	ArrayList<String> lstansurl=new ArrayList<String>();
	ArrayList<String> lstQues=new ArrayList<String>();
	ArrayList<String> lstQuesID=new ArrayList<String>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_questions);
		
		sessionmanager = new SessionManager(context);
		userDetails = new HashMap<String, String>();
		userDetails = sessionmanager.getUserDetails();

		 db=new dbhandler(context);
		 sd=db.getReadableDatabase();
		 sd = db.getWritableDatabase();
		 allDetails = new ArrayList<HashMap<String,String>>();
		 
		 
		/* try {
			 sd.delete(db.TABLE_ASKQUESTIONS, null, null);
			sd.execSQL("insert into "+ db.TABLE_ASKQUESTIONS +" values(1,'Question 1',1,'arpit','abcd.jpg','24-10-2015',1664,0,'')");
			 sd.execSQL("insert into "+ db.TABLE_ASKQUESTIONS +" values(2,'Question 2',1,'arpit','abcd.jpg','24-10-2015',1664,0,'')");
			 sd.execSQL("insert into "+ db.TABLE_ASKQUESTIONS +" values(3,'Question 3',1,'arpit','abcd.jpg','24-10-2015',1664,0,'')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 
		 

		
		
		
		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ AppTitle +"</font>"));

				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();
				getSupportActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

			} else {
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ AppTitle +"</font>"));
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
		
		lstQuestions = (ListView)findViewById(R.id.lstQuestions);
		
		btnaskquestion=(Button)findViewById(R.id.btnaskquestion);
		
		btnaskquestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(context , AskQuestionActivity.class);
				intent.putExtra("ans_status", "0");
				intent.putExtra("repeat_status", "0");
				intent.putExtra("visibility", "0");
				
				startActivity(intent);
				
			}
		});
		
		
		
		if(NetConnectivity.isOnline(context))
		{
			new GetAllQustionsOfStudentFromServer().execute();
		}
		else
		{
			FillDataOnListView();	
		}
		
		
		
		
		lstQuestions.setOnItemClickListener(new OnItemClickListener() { 
			   
	         

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					if(NetConnectivity.isOnline(context))
					{
						String data  = lstQues.get(position) +" "+lstansurl.get(position);
						//Toast.makeText(context, "Data :"+data, Toast.LENGTH_SHORT).show();
						
						Intent i=new Intent(AllQuestionsActivity.this,ShowTestSolution.class);
						
						i.putExtra("title", ""+lstQues.get(position));
						
						i.putExtra("solutionpath", ""+lstansurl.get(position));
						
						i.putExtra("visibility", "1");
						i.putExtra("questionid", ""+lstQuesID.get(position));
						
						startActivity(i);
						
					}
					else
					{
						String msg = AllKeys.INTERNET_ERROR_MSG;
						if(msg.contains("appname"))
						{
							msg= msg.replace("msgapp", ""+R.string.app_name);
						}
						
						Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
						 
					}
		
				} 
	        }); 

	}

	

	private void FillDataOnListView() {
		// TODO Auto-generated method stub
		
		try {
			String query ="select * from "+ db.TABLE_ASKQUESTIONS +" where "+ db.ASKQUESTIONS_ANS_STATUS +"=1";
			Cursor cc=sd.rawQuery(query, null);
			allDetails.clear();
			lstansurl.clear();
			lstQues.clear();
			lstQuesID.clear();
			
			while(cc.moveToNext())
			{
				HashMap<String, String> details = new HashMap<String, String>();
				
				details.put("QUESTION", ""+cc.getString(cc.getColumnIndex(db.ASKQUESTIONS_QUESTION)));
				details.put("DATE", ""+cc.getString(cc.getColumnIndex(db.ASKQUESTIONS_DATETIME)));
				
				lstansurl.add(""+AllKeys.WEBSITE+cc.getString(cc.getColumnIndex(db.ASKQUESTIONS_ANSURL)));
				lstQues.add(""+cc.getString(cc.getColumnIndex(db.ASKQUESTIONS_QUESTION)));
				
				lstQuesID.add(""+cc.getString(cc.getColumnIndex(db.ASKQUESTIONS_ID)));
				
				
				allDetails.add(details);
			
				
				
				
			}
			
			SimpleAdapter adapter = new SimpleAdapter(context, allDetails, R.layout.row_single_question, new String[] {"QUESTION","DATE"}, new int[]{R.id.txtques,R.id.txtdate});
			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_single_question, lstQues);
			lstQuestions.setAdapter(adapter);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(item.getItemId() ==android.R.id.home)
		{
			
			
				Intent i=new Intent(context,MainActivity.class);
				startActivity(i);
				onBackPressed();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	class GetAllQustionsOfStudentFromServer extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressDialog;
		private String url1;
		private JSONArray jarrayquestions;
		

		@Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
                    progressDialog = new ProgressDialog(AllQuestionsActivity.this);
                    progressDialog.setTitle("Please wait");
            progressDialog .setMessage("Questions are loading...");
            progressDialog .setCancelable(false);
            progressDialog .show();
            
        }

        @Override
        protected String doInBackground(String... params) {
        	
        	
            String qry="Select * from DataUrl";
			
    			Cursor cdata=sd.rawQuery(qry, null);
    			
    			while(cdata.moveToNext())
    			{
    				url1=cdata.getString(0);
    			}
    	      
    			JSONParser jParser1 = new JSONParser();
    	
    			// getting JSON string from URL
    			//url=url1+"json_data.aspx?type=testsolution&classid="+ userDetails.get(SessionManagement.KEY_DEPTID) +"";
    			String url=url1+"json_data.aspx?type=allstudques&studid="+ userDetails.get(SessionManager.KEY_EMPID) +"";
    			JSONObject jsonnotes = jParser1.getJSONFromUrl(url);
    			
    	if(jsonnotes!=null)
    	{
    			try 
    			{
    				// Getting Array of Contacts
    				jarrayquestions = jsonnotes.getJSONArray(AllKeys.ARRAY_ALLQUESTION);
    				
    				
    				
    				// looping through All Contacts
    				//db.execSQL("create table IF NOT EXISTS testsolution(id INTEGER PRIMARY KEY AUTOINCREMENT,filepath TEXT,title TEXT)");
    				sd.delete(db.TABLE_ASKQUESTIONS, null, null);
    				for(int i = 0; i < jarrayquestions.length(); i++)
    				{
    					JSONObject c = jarrayquestions.getJSONObject(i);
    					
    				//+"("+ ASKQUESTIONS_ID +" INTEGER PRIMARY KEY,"+ ASKQUESTIONS_QUESTION +" TEXT,"+ ASKQUESTIONS_FAC_ID +" INTEGER,"+ ASKQUESTIONS_FAC_NAME +" TEXT,"+ ASKQUESTIONS_IMAGE_NAME +" TEXT,"+ ASKQUESTIONS_DATETIME +" TEXT,"+ ASKQUESTIONS_STUDID +" INTEGER,"+ ASKQUESTIONS_ANS_STATUS +" INTEGER,"+ ASKQUESTIONS_ANSURL +" TEXT) ");
    					String ansurl = "StudentAnswer.aspx?type=answer&stud_id="+ c.getString(AllKeys.TAG_STUDENTID) +"&ques_id="+ c.getString(AllKeys.TAG_QUESTION_ID) +"";
    					
    					sd.execSQL("insert into "+ db.TABLE_ASKQUESTIONS +" values("+ c.getString(AllKeys.TAG_QUESTION_ID) +",'"+ c.getString(AllKeys.TAG_QUESTION) +"',"+ c.getString(AllKeys.TAG_FACULTYID) +",'"+ c.getString(AllKeys.TAG_FACULTYNAME) +"','img','"+ c.getString(AllKeys.TAG_DATETIME) +"',"+ c.getString(AllKeys.TAG_STUDENTID) +","+ c.getString(AllKeys.TAG_ANSWER_STATUS) +",'"+ ansurl +"')");
    					
    				
    					//lsttestsolution.add(new ListItem(""+c.getString("TestName"),s)); 
    					//arr.add(c.getString("NoteId"));
    				}
    				
    			      
    			   
    			} 
    			catch (JSONException e) 
    			{
    				e.printStackTrace();
    			}
    	}
		    
        	
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
    
	FillDataOnListView();
	    
            progressDialog.dismiss();
            
            
        }
    }
	
	
}
