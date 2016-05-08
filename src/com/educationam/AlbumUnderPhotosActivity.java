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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.educationam.adapter.GridViewImageAdapter;

public class AlbumUnderPhotosActivity extends Activity {

	private static final String TAG = AlbumUnderPhotosActivity.class.getSimpleName();
	private GridView mGridView;
	private ProgressBar mProgressBar;
	private GridViewImageAdapter mGridAdapter;
	private ArrayList<GridItemUderAlbum> mGridData;
	private String FEED_URL = "";
	String idget;
	GridItemUderAlbum tum;
	private dbhandler db;
	private SQLiteDatabase sd;
	private String url;
	private JSONArray jarraygallery;
	ArrayList gallerylist;
	TextView back;
	private String bigImageLink;
	private SessionManager sessionmanager;
	private HashMap<String, String> userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_under_photos);

		try {
			mGridView = (GridView) findViewById(R.id.gridViewAlbumUnderPhoto);
			mProgressBar = (ProgressBar) findViewById(R.id.progressBarAlbumUnderPhoto);

			gallerylist = new ArrayList<HashMap<String, String>>();
			db = new dbhandler(getApplicationContext());
			sd = db.getReadableDatabase();
			sd = db.getWritableDatabase();
			// Initialize with empty data
			Intent i = getIntent();
			idget = i.getStringExtra("ID");
			new GetGalleryFromServer().execute();
			back = (TextView) findViewById(R.id.back);
			sessionmanager = new SessionManager(getApplicationContext());
			userDetails = new HashMap<String, String>();
			userDetails = sessionmanager.getUserDetails();
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					finish();
				}
			});

			mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
					try {

						tum = (GridItemUderAlbum) parent.getItemAtPosition(position);
						Intent intent = new Intent(AlbumUnderPhotosActivity.this, DetailsOfPhoto.class);
						ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
						String photoId = ((TextView) v.findViewById(R.id.grid_item_id)).getText().toString();
						String qry = "SELECT * FROM albumphoto WHERE photoid=" + photoId;
						Cursor cr = sd.rawQuery(qry, null);
						while (cr.moveToNext()) {
							bigImageLink = cr.getString(2);
						}
						Log.d("link", bigImageLink);

						// Interesting data to pass across are the
						// thumbnail
						// size/location, the
						// resourceId of the source bitmap, the picture
						// description, and the
						// orientation (to avoid returning back to an
						// obsolete configuration if
						// the device rotates again in the meantime)

						int[] screenLocation = new int[2];
						imageView.getLocationOnScreen(screenLocation);

						// Pass the image title and url to
						// DetailsActivity

						intent.putExtra("left", screenLocation[0]).putExtra("top", screenLocation[1])
								.putExtra("width", imageView.getWidth()).putExtra("height", imageView.getHeight())

								.putExtra("image", bigImageLink);

						// Start details activity
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// *****************************

	class GetGalleryFromServer extends AsyncTask<Void, Void, Void> {
		private String url1;

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(AlbumUnderPhotosActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			pDialog.setCancelable(false);
			// mProgressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... urls) {

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL=
			// url = AllKeys.WEBSITE +
			// "Json_data.aspx?type=upcomingtest&deptid="
			// + deptid;

			String url = AllKeys.WEBSITE + "GetJSONForAlbumPhoto?clientid=" + AllKeys.CLIENT_ID + "&branch="
					+ userDetails.get(SessionManager.KEY_BRANCHID) + "&albumid=" + idget;

			ServiceHandler sh = new ServiceHandler();
			String str_stud_events = sh.makeServiceCall(url, ServiceHandler.GET);

			if (str_stud_events != null && !str_stud_events.equals("")) {

				try {
					JSONObject json = new JSONObject(str_stud_events);
					if (json != null) {
						try {
							// Getting Array of Contacts
							jarraygallery = json.getJSONArray("AlbumPhoto");

							// looping through All Contacts
							sd.delete("albumphoto", null, null);
							for (int i = 0; i < jarraygallery.length(); i++) {
								JSONObject c = jarraygallery.getJSONObject(i);

								String albumphotoid = c.getString("PhotoID");
								String albumphotothumb = c.getString("Thumb");
								String albumphotoimage = c.getString("Image");

								// String qry = "INSERT INTO album
								// VALUES(null,'"
								// + c.getString("AlbumID") + "','"
								// + c.getString("AlbumName") + "','"
								// + c.getString("Image") + "') ";
								//
								// Log.d("query", qry);

								try {
									sd.execSQL("INSERT INTO albumphoto VALUES(" + "'" + c.getString("PhotoID") + "','"
											+ c.getString("Thumb") + "','" + c.getString("Image") + "') ");
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								tum = new GridItemUderAlbum();

								tum.setIdUnderAlbum(albumphotoid);
								// tum.setThumbUnderAlbum(albumphotothumb);
								tum.setImageUnderAlbum(albumphotoimage);
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
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// backupDB();
			if (pDialog.isShowing()) {

				pDialog.dismiss();
				pDialog.cancel();

			}
			mGridAdapter = new GridViewImageAdapter(getApplicationContext(), R.layout.grid_item_album_laoyut,
					gallerylist);
			mGridView.setAdapter(
					new GridViewImageAdapter(getApplicationContext(), R.layout.grid_item_album_laoyut, gallerylist));

		}
	}

	public void backupDB() {

		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + getApplicationContext().getPackageName() + "/databases/" + dbhandler.db;
		String backupDBPath = dbhandler.db;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(getApplicationContext(), "DataBase Exported!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// *****************************

}
