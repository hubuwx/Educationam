package com.educationam;

import java.io.InputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailsOfPhoto extends Activity {
	private static final int ANIM_DURATION = 600;
	private TextView titleTextView;
	private ImageView imageView;

	private int mLeftDelta;
	private int mTopDelta;
	private float mWidthScale;
	private float mHeightScale;
	private FrameLayout frameLayout;
	private ColorDrawable colorDrawable;
	private int thumbnailTop;
	private int thumbnailLeft;
	private int thumbnailWidth;
	private int thumbnailHeight;
	ProgressBar pgrFullImageview;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// Setting details screen layout
		setContentView(R.layout.activity_details_of_photo);
		Intent i = getIntent();
		String image = i.getStringExtra("IMAGELINK");
		imageView = (ImageView) findViewById(R.id.grid_item_image_detail);
		BitmapDrawable bmd = (BitmapDrawable) getDrawableFromUrl(image);
		Bitmap bitmap = bmd.getBitmap();
		imageView.setImageBitmap(bitmap);
	}

	public Drawable getDrawableFromUrl(String imgUrl) {
		if (imgUrl == null || imgUrl.equals(""))
			return null;
		try {
			URL url = new URL(imgUrl);
			InputStream in = url.openStream();
			Drawable d = Drawable.createFromStream(in, imgUrl);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
