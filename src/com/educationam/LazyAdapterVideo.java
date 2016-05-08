package com.educationam;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.renderscript.Script.KernelID;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapterVideo extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    AllKeys keys;
    
    public LazyAdapterVideo(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row_single_video, null);

        TextView schoolname = (TextView)vi.findViewById(R.id.schoolname); // title
        TextView schooladdress = (TextView)vi.findViewById(R.id.schooladdress); // artist name
        
        TextView schoolid = (TextView)vi.findViewById(R.id.schoolid); // duration
        ImageView school_logo=(ImageView)vi.findViewById(R.id.school_logo); // thumb image
        
        HashMap<String, String> schoolsdata = new HashMap<String, String>();
        schoolsdata = data.get(position);
        
        // Setting all values in listview
        schoolname.setText(schoolsdata.get(keys.TAG_VIDEONAME));
        schooladdress.setText(schoolsdata.get(keys.TAG_VIDEOURL));
        
       //schoolid.setText(schoolsdata.get(keys.TAG_SCHOOL_ID));
        schoolid.setText(schoolsdata.get(keys.TAG_VIDEOID));
        
        imageLoader.DisplayImage(schoolsdata.get(keys.TAG_THUMBNAIL), school_logo);
         return vi;
    }
}