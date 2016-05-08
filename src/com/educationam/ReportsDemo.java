package com.educationam;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class ReportsDemo extends TabActivity {

	private Context context=this;
	private String empid;
	private String empname;
	private String id;
	private String tblno;
	private int tab;
	private String placeid;
	private String edit;
	private Cursor c;
	private MenuItem todaySched;
	private String mm;
	private String dd;
	private String orderdate;
	private int parcelid;
	private String reorder;
	private String parcelids;
	private String tblsplit;
	private String ids;
	private String lan;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_view);
		
		setTitle("");
		
		
		
		TabHost tbHost=getTabHost();
		Intent ii=new Intent(context,ChartActivity.class);
		ii.putExtra("chart", "pie");
		
	
		tbHost.addTab(tbHost.newTabSpec("Tab1").setIndicator("PieChart").setContent(ii));
		
		ii=new Intent(context,ChartActivity.class);
		ii.putExtra("chart", "bar");
		 
		tbHost.addTab(tbHost.newTabSpec("Tab2").setIndicator("BarChart").setContent(ii));
		tbHost.setCurrentTabByTag("Tab2");
		

		ii=new Intent(context,ChartActivity.class);
		ii.putExtra("chart", "core");
		
		tbHost.addTab(tbHost.newTabSpec("Tab3").setIndicator("CoreChart").setContent(ii));
		
		ii=new Intent(context,ChartActivity.class);
		ii.putExtra("chart", "meter");
		 
		tbHost.addTab(tbHost.newTabSpec("Tab4").setIndicator("Meter").setContent(ii));
		
		
		
		tbHost.setCurrentTabByTag("Tab1");
	}

	
}
