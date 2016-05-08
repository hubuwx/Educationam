package com.educationam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
	 private LayoutInflater inflater;
	    private ArrayList<Parent> mParent;
		private int childPosition;
		private int groupPosition;
		private String reason;

		/*private int groupPosition;
		private int childPosition;
	 */
	     public ExpandableListAdapter(Context context, ArrayList<Parent> parent){
	        mParent = parent;
	        inflater = LayoutInflater.from(context);
	    }
	 
	 
	    @Override
	    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
	    public int getGroupCount() {
	        return mParent.size();
	    }
	 
	    @Override
	    //counts the number of children items so the list knows how many times calls getChildView() method
	    public int getChildrenCount(int i) {
	        return mParent.get(i).getArrayChildren().size();
	    }
	 
	    @Override
	    //gets the title of each parent/group
	    public Object getGroup(int i) {
	        return mParent.get(i).getTitle();
	        
	    }
	 
	   
	    @Override
	    //gets the name of each item
	    public Object getChild(int i, int i1) {
	        return mParent.get(i).getArrayChildren().get(i1);
	    }
	 
	    @Override
	    public long getGroupId(int i) {
	        return i;
	    }
	 
	    @Override
	    public long getChildId(int i, int i1) {
	        return i1;
	    }
	 
	    @Override
	    public boolean hasStableIds() {
	        return true;
	    }
	    
	    public class ViewHolder {
	        protected int childPosition;
	        protected int groupPosition;
	        protected Button button;
	    }
	 
	    @Override
	    //in this method you must set the text to see the parent/group on the list
	    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
	 
	        ViewHolder holder = new ViewHolder();
	        holder.groupPosition = groupPosition; 
	 
	        if (view == null) {
	            view = inflater.inflate(R.layout.parentxml, viewGroup,false);
	        }
	 
	        TextView textView = (TextView) view.findViewById(R.id.textView111);
	        //"i" is the position of the parent/group in the list
	        textView.setText(getGroup(i).toString());

	        view.setTag(holder);
	 
	        //return the entire view
	        return view;
	    }
	 
	    @Override
	    //in this method you must set the text to see the children on the list
	    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
	 
	        ViewHolder holder = new ViewHolder();
	        holder.childPosition = childPosition;
	        holder.groupPosition = groupPosition;
	 
	        if (view == null) {
	            view = inflater.inflate(R.layout.listrowsa, viewGroup,false);
	        }
	 
	        TextView textView = (TextView) view.findViewById(R.id.textView1);
	        //TextView textView1 = (TextView) view.findViewById(R.id.textView2);
	        
	        //"i" is the position of the parent/group in the list and 
	        //"i1" is the position of the child
	        textView.setText(mParent.get(i).getArrayChildren().get(i1));
	        
	       
	        view.setTag(holder);
	 
	        //return the entire view
	        return view;
	    }
	 
	    @Override
	    public boolean isChildSelectable(int i, int i1) {
	        return true;
	    }
	 
	    @Override
	    public void registerDataSetObserver(DataSetObserver observer) {
	        /* used to make the notifyDataSetChanged() method work */
	        super.registerDataSetObserver(observer);
	    }
	 
	// Intentionally put on comment, if you need on click deactivate it
	/*  @Override
	    public void onClick(View view) {
	        ViewHolder holder = (ViewHolder)view.getTag();
	        if (view.getId() == holder.button.getId()){
	 
	           // DO YOUR ACTION
	        }
	    }*/
	 
	 
	
	}