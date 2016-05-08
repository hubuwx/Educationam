package com.educationam;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class ListStudActAdapter  extends ArrayAdapter<HolidayItem> {
	 
	  private ArrayList<HolidayItem> countryList;
	 
	  public ListStudActAdapter(Context context, int textViewResourceId,
	    ArrayList<HolidayItem> countryList) {
	   super(context, textViewResourceId, countryList);
	   this.countryList = new ArrayList<HolidayItem>();
	   this.countryList.addAll(countryList);
	  }
	 
	  private class ViewHolder {
	 
	  
	
	public TextView title;
	public TextView day;
	public TextView date;
	   
	  }
	 
	  @Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
	 
	   ViewHolder holder = null;
	   Log.v("ConvertView", String.valueOf(position));
	 
	   if (convertView == null) {
	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   convertView = vi.inflate(R.layout.listrowsa, null);
	 
	   holder = new ViewHolder();
	 
	   holder.title= (TextView) convertView.findViewById(R.id.textView2);
	   holder.day= (TextView) convertView.findViewById(R.id.textView3);
	   holder.date=(TextView)convertView.findViewById(R.id.textView1);
	  
	   convertView.setTag(holder);
	
	   }
	   else 
	   {
	    holder = (ViewHolder) convertView.getTag();
	   }
	 
	   final HolidayItem country = countryList.get(position);

	   holder.title.setText(country.getTitel());
	   holder.day.setText(country.getDay());
	   holder.date.setText(country.getDate());
	  
	 
	   return convertView;
	 
	  }
	 
	 }

