package com.educationam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GallaryFrag extends Fragment {
	ArrayList gallerylist;

	private String url;
	private JSONArray jarraygallery;
	private String testname, facultyname, date, sub;
	private dbhandler db;
	private SQLiteDatabase sd;
	private GalleryAlbumAdapter adapter;
	private ListView listviewalbum;
	ImageItemAlbum tum;
	private Object albumId;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	public static GallaryFrag create() {
		GallaryFrag f = new GallaryFrag();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gallaryxml, container, false);

		try {
			listviewalbum = (ListView) v.findViewById(R.id.listviewAlbum);

			gallerylist = new ArrayList<HashMap<String, String>>();
			db = new dbhandler(getActivity());
			sd = db.getReadableDatabase();
			sd = db.getWritableDatabase();
			sessionmanager = new SessionManager(getActivity());
			userDetails = new HashMap<String, String>();
			userDetails = sessionmanager.getUserDetails();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		new GetGalleryFromServer().execute();

		try {
			listviewalbum.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					String albumId = ((TextView) arg1.findViewById(R.id.textIdAlbum)).getText().toString();

					// Toast.makeText(getActivity(), albumId,
					// Toast.LENGTH_SHORT)
					// .show();
					Intent i = new Intent(getActivity(), AlbumUnderPhotosActivity.class);
					i.putExtra("ID", albumId);
					startActivity(i);

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class GetGalleryFromServer extends AsyncTask<String, Void, String> {
		private String url1;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			// url = AllKeys.WEBSITE +
			// "Json_data.aspx?type=upcomingtest&deptid="
			// + deptid;
			// url = AllKeys.WEBSITE + "json_data.aspx?type=album";
			String url = AllKeys.WEBSITE + "GetJSONForAlbum?clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID);
			// JSONObject json = jParser.getJSONFromUrl(url);
			ServiceHandler sh = new ServiceHandler();
			String str_stud_events = sh.makeServiceCall(url, ServiceHandler.GET);

			if (str_stud_events != null && !str_stud_events.equals("")) {
				try {
					JSONObject json = new JSONObject(str_stud_events);
					if (json != null) {
						try {
							// Getting Array of Contacts
							jarraygallery = json.getJSONArray("Album");

							// looping through All Contacts
							sd.delete("album", null, null);
							for (int i = 0; i < jarraygallery.length(); i++) {
								JSONObject c = jarraygallery.getJSONObject(i);

								String albumid = c.getString("AlbumID");
								String albumname = c.getString("AlbumName");
								String albumimage = c.getString("Image");
								try {
									sd.execSQL("INSERT INTO album VALUES(null,'" + c.getString("AlbumID") + "','"
											+ c.getString("AlbumName") + "','" + c.getString("Image") + "') ");
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								tum = new ImageItemAlbum();

								tum.setId(albumid);
								tum.setTitle(albumname);
								tum.setImage(albumimage);
								gallerylist.add(tum);
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("Albums json error", e.getMessage());
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			pDialog.cancel();

			// backupDB();
			adapter = new GalleryAlbumAdapter(getActivity(), R.layout.main_album_item, gallerylist);
			listviewalbum.setAdapter(new GalleryAlbumAdapter(getActivity(), R.layout.main_album_item, gallerylist));
			// getAllData();

		}

	}

	// ***************************************
	// Code For Backup Current Database
	public void backupDB() {

		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + getActivity().getPackageName() + "/databases/" + dbhandler.db;
		String backupDBPath = dbhandler.db;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(getActivity(), "DataBase Exported!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
