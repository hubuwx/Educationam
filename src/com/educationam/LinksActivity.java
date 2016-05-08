package com.educationam;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.os.Build;

public class LinksActivity extends Fragment {

	public static LinksActivity create() {
		LinksActivity f = new LinksActivity();
        return f;
    }
	private ExpandableListView mExpandableList;
	 ArrayList<Parent> arrayParents = new ArrayList<Parent>();
    ArrayList<String> arrayChildren = new ArrayList<String>();
    ArrayList<String> arraylinkid = new ArrayList<String>();
    String[] category={"Category1","Category2","Category3","Category4","Category5"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_links, container, false);
      
       // listview=(ListView)v.findViewById(R.id.listView1);
        mExpandableList=(ExpandableListView)v.findViewById(R.id.expandableListView1);
        populatelist();
	       
        
        
		 mExpandableList.setAdapter(new ExpandableListAdapter(getActivity(),arrayParents)); 
        return v;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState); 
           mExpandableList.setOnChildClickListener(new OnChildClickListener() {
			
			private String linkurl;

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				dbhandler db=new dbhandler(getActivity());
				SQLiteDatabase sd=db.getReadableDatabase();
				Cursor c1=sd.rawQuery("select * from uselink where linkid="+arraylinkid.get(childPosition), null);
		    	 getActivity().startManagingCursor(c1);
		    	while(c1.moveToNext())
		    	{
		    		linkurl=c1.getString(3);
		    		//WebView web=new WebView(getActivity());
		    		//web.loadUrl(""+c1.getString(3));
		    	}
		    	Intent web=new Intent(getActivity(),LinkWebview.class);
		    	web.putExtra("URL", linkurl);
		    	startActivity(web);
				return false;
			}
		});
        
    }
    
    public void populatelist()
	{
    	 arrayParents.clear();
    	 Parent parent = new Parent();
    	 dbhandler db=new dbhandler(getActivity());
    	 SQLiteDatabase sd=db.getReadableDatabase();
    	 Cursor c=sd.rawQuery("select * from linkcategory", null);
    	 getActivity().startManagingCursor(c);
    	while(c.moveToNext())
    	{
    		parent=new Parent();
    		
             parent.setTitle("      "+c.getString(1));
            
            
            
             
             parent.setArrayChildren(arrayChildren);

            // arrayParents.add(parent);
            // arrparentrn.add(rn);
            //int row=0;
			arrayChildren = new ArrayList<String>();
			arrayChildren.clear();
			Cursor c1=sd.rawQuery("select * from uselink where catid="+c.getString(0), null);
	    	 getActivity().startManagingCursor(c1);
	    	while(c1.moveToNext())
			{
	    		arraylinkid.add(c1.getString(0));
				arrayChildren.add("         "+c1.getString(2));
			}
			parent.setArrayChildren(arrayChildren);

			//in this array we add the Parent object. We will use the arrayParents at the setAdapter
			arrayParents.add(parent);
    	}
    	

	}
 
    
}
