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

public class ListAdapter extends ArrayAdapter<HomeWork> {

	private ArrayList<HomeWork> countryList;

	public ListAdapter(Context context, int textViewResourceId,
			ArrayList<HomeWork> countryList) {
		super(context, textViewResourceId, countryList);
		this.countryList = new ArrayList<HomeWork>();
		this.countryList.addAll(countryList);
	}

	private class ViewHolder {

		public TextView homework;
		public TextView faculty;
		public TextView date;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		Log.v("ConvertView", String.valueOf(position));

		try {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.activity_home_work, null);

				holder = new ViewHolder();

				holder.homework = (TextView) convertView
						.findViewById(R.id.lblHomeWOrk);
				holder.faculty = (TextView) convertView
						.findViewById(R.id.lblFaculty);
				holder.date = (TextView) convertView.findViewById(R.id.lblDate);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final HomeWork country = countryList.get(position);
			

			holder.homework.setText(country.getHomework());
			holder.faculty.setText(country.getFaculty());
			holder.date.setText(country.getDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;

	}

}
