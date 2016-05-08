package com.educationam;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ResultFrag extends Fragment {

	Activity a;
	private TableLayout tl;
	private int empid;
	private int cnt = 0;
	private Spinner spsub;
	ArrayList<String> listsub = new ArrayList<String>();
	ArrayList<String> listsubid = new ArrayList<String>();
	private String subid;

	private String subname;
	private String testdate;
	private String testname;
	private String totmark;
	private Button btn;
	private String subName;

	public static ResultFrag create() {
		ResultFrag f = new ResultFrag();
		return f;
	}

	/**
	 *
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.resultxml, container, false);
		tl = (TableLayout) v.findViewById(R.id.tables);

		spsub = (Spinner) v.findViewById(R.id.spinner1);

		btn = (Button) v.findViewById(R.id.button1);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// add data to ListView
		// add data to ListView
		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		Cursor csub = sd.rawQuery("select * from submaster", null);
		getActivity().startManagingCursor(csub);
		listsubid.clear();
		listsub.clear();
		listsub.add(0, "All");
		listsubid.add(0, "0");
		int spinnerPosition = 0;
		spsub.setSelection(spinnerPosition);

		while (csub.moveToNext()) {
			listsub.add(csub.getString(1));
			listsubid.add(csub.getString(0));
		}
		spsub.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listsub));

		spsub.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position >= 1) {
					subid = listsubid.get(position);
					subname = listsub.get(position);
					getalldata();
				}
				if (position == 0) {
					subid = listsubid.get(position);
					subname = listsub.get(position);

					getwholedata();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		if (savedInstanceState != null) {
			subid = savedInstanceState.getString("subid");
			int index = listsubid.indexOf(subid);
			spsub.setSelection(index);
			getalldata();

		}

	}

	public void getwholedata() {
		tl.removeAllViews();
		TableRow tr = new TableRow(getActivity());
		// tr.setBackgroundColor(Color.RED);
		tr.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView sno = new TextView(getActivity());
		sno.setId(201);
		sno.setText("No.");
		sno.setPadding(10, 15, 10, 15);
		sno.setTextSize(25);
		sno.setGravity(Gravity.CENTER);
		sno.setBackgroundResource(R.drawable.cell_shapes);
		sno.setTextColor(Color.RED);
		tr.addView(sno);

		TextView time0 = new TextView(getActivity());
		time0.setId(201);
		time0.setText("Date");
		time0.setPadding(10, 15, 10, 15);
		time0.setTextSize(25);
		time0.setGravity(Gravity.CENTER);
		time0.setBackgroundResource(R.drawable.cell_shapes);
		time0.setTextColor(Color.RED);
		tr.addView(time0);

		TextView time1 = new TextView(getActivity());
		time1.setId(201);
		time1.setText("TestName");
		time1.setPadding(20, 15, 20, 15);
		time1.setTextSize(25);
		time1.setGravity(Gravity.CENTER);
		time1.setBackgroundResource(R.drawable.cell_shapes);
		time1.setTextColor(Color.RED);
		tr.addView(time1);

		TextView time3 = new TextView(getActivity());
		time3.setId(201);
		time3.setText("Sub");
		time3.setTextColor(Color.RED);
		time3.setPadding(20, 15, 20, 15);
		time3.setTextSize(25);
		time3.setGravity(Gravity.CENTER);
		time3.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(time3);
		// pricelist.add(time2.getText().toString());

		TextView time2 = new TextView(getActivity());
		time2.setId(201);
		time2.setText("TotMarks");
		time2.setTextColor(Color.RED);
		time2.setPadding(20, 15, 20, 15);
		time2.setTextSize(25);
		time2.setGravity(Gravity.CENTER);
		time2.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(time2);

		TextView obtmrk = new TextView(getActivity());
		obtmrk.setId(201);
		obtmrk.setText("ObtMarks");
		obtmrk.setTextColor(Color.RED);
		obtmrk.setPadding(20, 15, 20, 15);
		obtmrk.setTextSize(25);
		obtmrk.setGravity(Gravity.CENTER);
		obtmrk.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(obtmrk);

		TextView per = new TextView(getActivity());
		per.setId(201);
		per.setText("Per");
		per.setTextColor(Color.RED);
		per.setPadding(20, 15, 20, 15);
		per.setTextSize(25);
		per.setGravity(Gravity.CENTER);
		per.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(per);
		//
		// TextView highmark = new TextView(getActivity());
		// highmark.setId(201);
		// highmark.setText("Highest");
		// highmark.setTextColor(Color.RED);
		// highmark.setPadding(20, 15, 20, 15);
		// highmark.setTextSize(25);
		// highmark.setGravity(Gravity.CENTER);
		// highmark.setBackgroundResource(R.drawable.cell_shapes);
		// tr.addView(highmark);

		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		/*
		 * Cursor c=sd.rawQuery("select * from studinfo", null);
		 * getActivity().startManagingCursor(c); if(c.getCount()>0) {
		 * while(c.moveToNext()) { empid=c.getInt(1); } }
		 */

		// Cursor c=sd.rawQuery("select * from testdetail where rollno="+empid
		// +" and subid="+subid+" order by testid DESC", null);
		Cursor c = sd.rawQuery("select * from testdetail", null);
		getActivity().startManagingCursor(c);
		while (c.moveToNext()) {

			String subID = c.getString(3);

			Cursor ck = sd.rawQuery("select * from submaster where subid="
					+ subID, null);

			while (ck.moveToNext()) {
				subName = ck.getString(1);
			}
			cnt++;
			tr = new TableRow(getActivity());
			// tr.setBackgroundColor(Color.RED);
			tr.setGravity(Gravity.CENTER_HORIZONTAL);

			sno = new TextView(getActivity());
			sno.setId(201);
			sno.setText("" + cnt);
			sno.setPadding(10, 10, 10, 10);
			sno.setTextSize(20);
			sno.setGravity(Gravity.CENTER);
			sno.setBackgroundResource(R.drawable.cell_shapes);
			sno.setTextColor(Color.BLACK);
			tr.addView(sno);

			Cursor ctestdtnm = sd.rawQuery(
					"select * from testmaster where testid=" + c.getString(0)
							+ " order by testdate ASC", null);
			getActivity().startManagingCursor(ctestdtnm);
			while (ctestdtnm.moveToNext()) {
				testdate = ctestdtnm.getString(1);
				testname = ctestdtnm.getString(2);
				totmark = ctestdtnm.getString(3);
			}
			time0 = new TextView(getActivity());
			time0.setId(201);
			time0.setText("" + testdate);
			time0.setPadding(10, 10, 10, 10);
			time0.setTextSize(20);
			time0.setGravity(Gravity.CENTER);
			time0.setBackgroundResource(R.drawable.cell_shapes);
			time0.setTextColor(Color.BLACK);
			tr.addView(time0);

			time1 = new TextView(getActivity());
			time1.setId(201);
			time1.setText("" + testname);
			time1.setPadding(20, 10, 20, 10);
			time1.setTextSize(20);
			time1.setGravity(Gravity.CENTER);
			time1.setBackgroundResource(R.drawable.cell_shapes);
			time1.setTextColor(Color.BLACK);
			tr.addView(time1);

			time3 = new TextView(getActivity());
			time3.setId(201);
			time3.setText("" + subName);
			time3.setTextColor(Color.BLACK);
			time3.setPadding(20, 10, 20, 10);
			time3.setTextSize(20);
			time3.setGravity(Gravity.CENTER);
			time3.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(time3);
			// pricelist.add(time2.getText().toString());

			time2 = new TextView(getActivity());
			time2.setId(201);
			time2.setText("" + totmark);
			time2.setTextColor(Color.BLACK);
			time2.setPadding(20, 10, 20, 10);
			time2.setTextSize(20);
			time2.setGravity(Gravity.CENTER);
			time2.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(time2);

			obtmrk = new TextView(getActivity());
			obtmrk.setId(201);
			obtmrk.setText("" + c.getInt(4));
			obtmrk.setTextColor(Color.BLACK);
			obtmrk.setPadding(20, 10, 20, 10);
			obtmrk.setTextSize(20);
			obtmrk.setGravity(Gravity.CENTER);
			obtmrk.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(obtmrk);

			per = new TextView(getActivity());
			per.setId(201);

			per.setText("" + c.getString(5));
			per.setTextColor(Color.BLACK);
			per.setPadding(20, 10, 20, 10);
			per.setTextSize(20);
			per.setGravity(Gravity.CENTER);
			per.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(per);

			// TextView hmark = new TextView(getActivity());
			// hmark.setId(201);
			//
			// hmark.setText("" + c.getString(6));
			// hmark.setTextColor(Color.BLACK);
			// hmark.setPadding(20, 10, 20, 10);
			// hmark.setTextSize(20);
			// hmark.setGravity(Gravity.CENTER);
			// hmark.setBackgroundResource(R.drawable.cell_shapes);
			// tr.addView(hmark);

			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		}

	}

	public void getalldata() {
		tl.removeAllViews();
		TableRow tr = new TableRow(getActivity());
		// tr.setBackgroundColor(Color.RED);
		tr.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView sno = new TextView(getActivity());
		sno.setId(201);
		sno.setText("No.");
		sno.setPadding(10, 15, 10, 15);
		sno.setTextSize(25);
		sno.setGravity(Gravity.CENTER);
		sno.setBackgroundResource(R.drawable.cell_shapes);
		sno.setTextColor(Color.RED);
		tr.addView(sno);

		TextView time0 = new TextView(getActivity());
		time0.setId(201);
		time0.setText("Date");
		time0.setPadding(10, 15, 10, 15);
		time0.setTextSize(25);
		time0.setGravity(Gravity.CENTER);
		time0.setBackgroundResource(R.drawable.cell_shapes);
		time0.setTextColor(Color.RED);
		tr.addView(time0);

		TextView time1 = new TextView(getActivity());
		time1.setId(201);
		time1.setText("TestName");
		time1.setPadding(20, 15, 20, 15);
		time1.setTextSize(25);
		time1.setGravity(Gravity.CENTER);
		time1.setBackgroundResource(R.drawable.cell_shapes);
		time1.setTextColor(Color.RED);
		tr.addView(time1);

		TextView time3 = new TextView(getActivity());
		time3.setId(201);
		time3.setText("Sub");
		time3.setTextColor(Color.RED);
		time3.setPadding(20, 15, 20, 15);
		time3.setTextSize(25);
		time3.setGravity(Gravity.CENTER);
		time3.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(time3);
		// pricelist.add(time2.getText().toString());

		TextView time2 = new TextView(getActivity());
		time2.setId(201);
		time2.setText("TotMarks");
		time2.setTextColor(Color.RED);
		time2.setPadding(20, 15, 20, 15);
		time2.setTextSize(25);
		time2.setGravity(Gravity.CENTER);
		time2.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(time2);

		TextView obtmrk = new TextView(getActivity());
		obtmrk.setId(201);
		obtmrk.setText("ObtMarks");
		obtmrk.setTextColor(Color.RED);
		obtmrk.setPadding(20, 15, 20, 15);
		obtmrk.setTextSize(25);
		obtmrk.setGravity(Gravity.CENTER);
		obtmrk.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(obtmrk);

		TextView per = new TextView(getActivity());
		per.setId(201);
		per.setText("Per");
		per.setTextColor(Color.RED);
		per.setPadding(20, 15, 20, 15);
		per.setTextSize(25);
		per.setGravity(Gravity.CENTER);
		per.setBackgroundResource(R.drawable.cell_shapes);
		tr.addView(per);

		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		dbhandler db = new dbhandler(getActivity());
		SQLiteDatabase sd = db.getReadableDatabase();

		/*
		 * Cursor c=sd.rawQuery("select * from studinfo", null);
		 * getActivity().startManagingCursor(c); if(c.getCount()>0) {
		 * while(c.moveToNext()) { empid=c.getInt(1); } }
		 */

		// Cursor c=sd.rawQuery("select * from testdetail where rollno="+empid
		// +" and subid="+subid+" order by testid DESC", null);
		Cursor c = sd.rawQuery("select * from testdetail where subid=" + subid
				+ " order by testid DESC", null);
		getActivity().startManagingCursor(c);
		while (c.moveToNext()) {
			cnt++;
			tr = new TableRow(getActivity());
			// tr.setBackgroundColor(Color.RED);
			tr.setGravity(Gravity.CENTER_HORIZONTAL);

			sno = new TextView(getActivity());
			sno.setId(201);
			sno.setText("" + cnt);
			sno.setPadding(10, 10, 10, 10);
			sno.setTextSize(20);
			sno.setGravity(Gravity.CENTER);
			sno.setBackgroundResource(R.drawable.cell_shapes);
			sno.setTextColor(Color.BLACK);
			tr.addView(sno);

			Cursor ctestdtnm = sd.rawQuery(
					"select * from testmaster where testid=" + c.getString(0),
					null);
			getActivity().startManagingCursor(ctestdtnm);
			while (ctestdtnm.moveToNext()) {
				testdate = ctestdtnm.getString(1);
				testname = ctestdtnm.getString(2);
				totmark = ctestdtnm.getString(3);
			}

			time0 = new TextView(getActivity());
			time0.setId(201);
			time0.setText("" + testdate);
			time0.setPadding(10, 10, 10, 10);
			time0.setTextSize(20);
			time0.setGravity(Gravity.CENTER);
			time0.setBackgroundResource(R.drawable.cell_shapes);
			time0.setTextColor(Color.BLACK);
			tr.addView(time0);

			time1 = new TextView(getActivity());
			time1.setId(201);
			time1.setText("" + testname);
			time1.setPadding(20, 10, 20, 10);
			time1.setTextSize(20);
			time1.setGravity(Gravity.CENTER);
			time1.setBackgroundResource(R.drawable.cell_shapes);
			time1.setTextColor(Color.BLACK);
			tr.addView(time1);

			time3 = new TextView(getActivity());
			time3.setId(201);
			time3.setText("" + subname);
			time3.setTextColor(Color.BLACK);
			time3.setPadding(20, 10, 20, 10);
			time3.setTextSize(20);
			time3.setGravity(Gravity.CENTER);
			time3.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(time3);
			// pricelist.add(time2.getText().toString());

			time2 = new TextView(getActivity());
			time2.setId(201);
			time2.setText("" + totmark);
			time2.setTextColor(Color.BLACK);
			time2.setPadding(20, 10, 20, 10);
			time2.setTextSize(20);
			time2.setGravity(Gravity.CENTER);
			time2.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(time2);

			obtmrk = new TextView(getActivity());
			obtmrk.setId(201);
			obtmrk.setText("" + c.getInt(4));
			obtmrk.setTextColor(Color.BLACK);
			obtmrk.setPadding(20, 10, 20, 10);
			obtmrk.setTextSize(20);
			obtmrk.setGravity(Gravity.CENTER);
			obtmrk.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(obtmrk);

			per = new TextView(getActivity());
			per.setId(201);

			per.setText("" + c.getString(5));
			per.setTextColor(Color.BLACK);
			per.setPadding(20, 10, 20, 10);
			per.setTextSize(20);
			per.setGravity(Gravity.CENTER);
			per.setBackgroundResource(R.drawable.cell_shapes);
			tr.addView(per);

			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("subid", "" + subid);
	}

}