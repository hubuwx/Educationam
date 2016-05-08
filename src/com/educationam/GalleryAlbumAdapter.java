package com.educationam;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GalleryAlbumAdapter extends ArrayAdapter<ImageItemAlbum> {
	ArrayList<ImageItemAlbum> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public GalleryAlbumAdapter(Context context, int resource,
			ArrayList<ImageItemAlbum> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.albumImageview = (ImageView) v.findViewById(R.id.textAlbum);
			holder.textName = (TextView) v.findViewById(R.id.textName);
			holder.textId = (TextView) v.findViewById(R.id.textIdAlbum);
			holder.listAlbum = (ListView) v.findViewById(R.id.listviewAlbum);

			
			v.setTag(holder);

		} else {
			holder = (ViewHolder) v.getTag();
		}
		try {
			holder.albumImageview.setImageResource(R.drawable.ic_launcher);

			// new
			// DownloadImageTask(holder.albumImageview).execute(actorList.get(
			// position).getImage());
			getImage(actorList.get(position).getImage());
			holder.textName.setText(actorList.get(position).getTitle());
			holder.textId.setText(actorList.get(position).getId());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return v;

	}

	static class ViewHolder {
		public ImageView albumImageview;
		public TextView textName;
		public TextView textId;
		public ListView listAlbum;

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	public void getImage(String link) {
		try {

			URL url = new URL(link);
			// try this url =
			// "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
			HttpGet httpRequest = null;

			httpRequest = new HttpGet(url.toURI());

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
			InputStream input = b_entity.getContent();

			Bitmap bitmap = BitmapFactory.decodeStream(input);

			holder.albumImageview.setImageBitmap(bitmap);

		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

}
