package com.educationam;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;

    private Integer[] mImageIds =  { R.drawable.ic_action_delete, R.drawable.ic_action_edit,

			R.drawable.ic_drawer, R.drawable.ic_launcher,

			R.drawable.notes, R.drawable.saveicon,

			R.drawable.sactivity, R.drawable.newcontent,

			R.drawable.results, R.drawable.notes ,

			R.drawable.newcontent, R.drawable.saveicon,

			};


    public GalleryImageAdapter(Context context)
    {
        mContext = context;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
  

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 ImageView i = new ImageView(mContext);

	        i.setImageResource(mImageIds[position]);
	        i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	   
	        i.setScaleType(ImageView.ScaleType.FIT_XY);

	        return i;
		
	}
}
