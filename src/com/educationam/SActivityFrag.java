package com.educationam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SActivityFrag extends Fragment{
	
	Activity a;
	private ListView listview;
	AlertDialogManager am=new AlertDialogManager();
	private ArrayList<HolidayItem> studactlist=new ArrayList<HolidayItem>();
	ArrayList<String> arr=new ArrayList<String>();
	private String s;
	public static SActivityFrag create() {
		SActivityFrag f = new SActivityFrag();
        return f;
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sactivityxml, container, false);
      
        listview=(ListView)v.findViewById(R.id.listView1);
		
        return v;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState); 
           
        dbhandler db=new dbhandler(getActivity());
		SQLiteDatabase sd=db.getReadableDatabase();
	
        //add data to ListView 
        ConnectionDetector cd=new ConnectionDetector(getActivity());
        
		 if(cd.isConnectingToInternet()==true)
	     {
			new DownloadWebPageTask().execute("");
			
	     }
		 else
		 {
			showdata();
	
		       
		 }
      
           
        listview.setOnItemClickListener(new OnItemClickListener() { 
   
         

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HolidayItem sa=studactlist.get(position);
			
				am.showAlertDialog(getActivity(), sa.getDay(), sa.getTitel(), false);
			} 
        }); 
           
    }
    
public void showdata()
{
    
    dbhandler db=new dbhandler(getActivity());
	SQLiteDatabase sd=db.getReadableDatabase();
	Cursor c= sd.rawQuery("select * from studactivity Order by id DESC", null);
	getActivity().startManagingCursor(c);
	 while(c.moveToNext())
	 {
		 SimpleDateFormat sf=new SimpleDateFormat("dd MMM yyyy");
		 String dt=c.getString(2);
		 if(dt.contains("-"))
		 {
			 
			 String[] dts=dt.split("-");
			 if(dts.length>2)
			 { 
				 int y=Integer.parseInt(dts[0]);
				 int m=Integer.parseInt(dts[1]);
				 int d=Integer.parseInt(dts[2]);
				 m=m-1;
				 y=y-1900;
				  s=sf.format(new Date(y, m, d));
			 }
		 }
		 else
			 s=dt;
		 studactlist.add(new HolidayItem(""+c.getString(1), c.getString(3),""+s));
	 }
	 
	 
     listview.setAdapter(new ListStudActAdapter(getActivity(), android.R.layout.simple_list_item_1, studactlist)); 
		 }
  	 
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		  

		private String url1;
		private ProgressDialog pDialog;
		private JSONArray jarrayholiday;
		private String url;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Loading....");
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
	    protected String doInBackground(String... urls) {
	      String response = "";
	     

	     
	      
	      return response;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	 String qry="Select * from DataUrl";
				dbhandler db=new dbhandler(getActivity());
				SQLiteDatabase sd=db.getReadableDatabase();
				Cursor cdata=sd.rawQuery(qry, null);
				getActivity().startManagingCursor(cdata);
				while(cdata.moveToNext())
				{
					url1=cdata.getString(0);
				}
		      
				JSONParser jParser = new JSONParser();
		
				// getting JSON string from URL
				url=url1+"json_data.aspx?type=Activity";
				JSONObject json = jParser.getJSONFromUrl(url);
				
				
		if(json!=null)
		{
				try 
				{
					// Getting Array of Contacts
					jarrayholiday = json.getJSONArray("Activity");
					
					
					
					// looping through All Contacts
					sd.delete("studactivity", null, null);
					for(int i = 0; i < jarrayholiday.length(); i++)
					{
						JSONObject c = jarrayholiday.getJSONObject(i);
					
						sd.execSQL("INSERT INTO studactivity VALUES('"+c.getString("ActivityId")+"','"+c.getString("ActivityName")+"','"+c.getString("ActivityDate")+"','"+c.getString("Description")+"') ");
						 SimpleDateFormat sf=new SimpleDateFormat("dd MMM yyyy");
						 String dt=c.getString("ActivityDate");
						 if(dt.contains("-"))
						 {
							 
							 String[] dts=dt.split("-");
							 if(dts.length>2)
							 { 
								 int y=Integer.parseInt(dts[0]);
								 int m=Integer.parseInt(dts[1]);
								 int d=Integer.parseInt(dts[2]);
								 m=m-1;
								 y=y-1900;
								  s=sf.format(new Date(y, m, d));
							 }
						 }
						 else
							 s=dt;
						studactlist.add(new HolidayItem(""+c.getString("ActivityName"), ""+c.getString("Description"), ""+s));
						arr.add(c.getString("ActivityId"));
					}
					
				     listview.setAdapter(new ListStudActAdapter(getActivity(), android.R.layout.simple_list_item_1, studactlist)); 
				    pDialog.dismiss();
				    pDialog.cancel();
				} 
				catch (JSONException e) 
				{
					 pDialog.dismiss();
					    pDialog.cancel();
					e.printStackTrace();
				}
		}
		else
		{
			showdata();
		}
		     
	    }
	  }
  	 


}
