package com.educationam;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter  extends ArrayAdapter<ListItem> {
		 
		  private ArrayList<ListItem> countryList;
		 
		  public CustomAdapter(Context context, int textViewResourceId,
		    ArrayList<ListItem> countryList) {
		   super(context, textViewResourceId, countryList);
		   this.countryList = new ArrayList<ListItem>();
		   this.countryList.addAll(countryList);
		  }
		 
		  private class ViewHolder {
		 
		   TextView listrn;
		   TextView listname;
		
		public ImageView imgarrow;
		   
		  }
		 
		  @Override
		  public View getView(final int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.listnotesrow, null);
		 
		   holder = new ViewHolder();
		 
		   holder.listrn= (TextView) convertView.findViewById(R.id.textView1);
		   holder.listname= (TextView) convertView.findViewById(R.id.textView2);
		   holder.imgarrow=(ImageView)convertView.findViewById(R.id.imageView1);
		  
		   convertView.setTag(holder);
		
		   }
		   else 
		   {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   final ListItem country = countryList.get(position);
	
		   holder.listrn.setText(country.getTitel());
		   holder.listname.setText(country.getTime());
		  
		 
		   return convertView;
		 
		  }
		 
		 }

