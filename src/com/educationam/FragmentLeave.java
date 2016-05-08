package com.educationam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class FragmentLeave extends android.support.v4.app.Fragment {

	public static FragmentLeave create() {
		FragmentLeave f = new FragmentLeave();
		return f;
	}

	private String SELECTEDREASON = "";
	private Button btnfrom;
	private Button btnto;
	private EditText txtreason;
	private Button btnsubmit;
	private Button btnclear;
	private Spinner spnReason;

	ArrayList<String> arrType = new ArrayList<String>();
	private SessionManager session;
	private HashMap<String, String> userdetails;
	private dbhandler db;
	private Context context = getActivity();
	private SQLiteDatabase sd;

	private String startDate;
	private int spm;
	private int y;
	private int d;
	private int ty;
	private int tm;
	private int td;
	private String enddate;
	private int m;
	protected String showsdate;
	private String endsdate;

	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID1 = 1112;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_home_work,
				container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		session = new SessionManager(getActivity());
		userdetails = new HashMap<String, String>();

		userdetails = session.getUserDetails();

		db = new dbhandler(getActivity());
		sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();

		btnfrom = (Button) rootView.findViewById(R.id.btnfrom);
		btnto = (Button) rootView.findViewById(R.id.btnto);

		txtreason = (EditText) rootView.findViewById(R.id.txtreason);

		btnsubmit = (Button) rootView.findViewById(R.id.btnsend);
		btnclear = (Button) rootView.findViewById(R.id.btnclear);

		spnReason = (Spinner) rootView.findViewById(R.id.spntype);

		arrType.clear();
		arrType.add("Sickness");
		arrType.add("Traveling");
		arrType.add("Family Function");
		arrType.add("Other");

		try {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
					android.R.layout.simple_spinner_dropdown_item, arrType);

			spnReason.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		spnReason.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				SELECTEDREASON = arrType.get(arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		

		final Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DAY_OF_MONTH);

		spm=m+1;
		if(spm<=9)
		{
			String mm="0"+spm;
			startDate=y+"-"+mm+"-"+d;
			showsdate=d+"-"+mm+"-"+y;
		}
		else
		{
			startDate=y+"-"+spm+"-"+d;
			showsdate=y+"-"+d+"-"+spm;
		}
		btnfrom.setText(showsdate);
		
		
		ty = cal.get(Calendar.YEAR);
		tm = cal.get(Calendar.MONTH);
		td = cal.get(Calendar.DAY_OF_MONTH);

		spm=tm+1;
		if(spm<=9)
		{
			String mm="0"+spm;
			enddate=ty+"-"+mm+"-"+td;
			endsdate=td+"-"+mm+"-"+ty;
		}
		else
		{
			enddate=ty+"-"+spm+"-"+td;
			endsdate=td+"-"+spm+"-"+ty;
		}
		btnto.setText(endsdate);

		

		btnfrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().showDialog(DATE_PICKER_ID);
			}
		});

		btnto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().showDialog(DATE_PICKER_ID1);
			}
		});
		
		
		
		return rootView;
	}
	


	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			return new DatePickerDialog(getActivity(), pickerListener,y,m,d);
		case DATE_PICKER_ID1:
			return new DatePickerDialog(getActivity(), pickerListener1,ty,tm,td);
		}
		return null;

	}
	
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			y  = selectedYear;
			m = selectedMonth;
			d   = selectedDay;

			// Show selected date

			//   String startDate;
			int spm=m+1;
			if(spm<=9)
			{
				String mm="0"+spm;
				startDate=y+"-"+mm+"-"+d;
				showsdate=d+"-"+mm+"-"+y;
			}
			else
			{
				startDate=y+"-"+spm+"-"+d;
				showsdate=d+"-"+spm+"-"+y;
			}
			btnfrom.setText(showsdate);

		}
	};
	

	private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {


		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			ty  = selectedYear;
			tm = selectedMonth;
			td   = selectedDay;

			// Show selected date

			//   String startDate;
			int spm=tm+1;
			if(spm<=9)
			{
				String mm="0"+spm;
				enddate=ty+"-"+mm+"-"+td;
				endsdate=td+"-"+mm+"-"+ty;
			}
			else
			{
				enddate=ty+"-"+spm+"-"+td;
				endsdate=td+"-"+spm+"-"+ty;
			}
			btnto.setText(endsdate);

		}
	};




}
