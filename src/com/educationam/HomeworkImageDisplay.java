package com.educationam;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class HomeworkImageDisplay extends ActionBarActivity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homework_image_display);

		Bundle bundle = getIntent().getExtras();
		thumbnailTop = bundle.getInt("top");
		thumbnailLeft = bundle.getInt("left");
		thumbnailWidth = bundle.getInt("width");
		thumbnailHeight = bundle.getInt("height");

		// String title = bundle.getString("title");
		String image = bundle.getString("IMAGELINK");

		// initialize and set the image description
		titleTextView = (TextView) findViewById(R.id.grid_item_home_title_detail);
		// titleTextView.setText(Html.fromHtml(title));
		// Set image url
		imageView = (ImageView) findViewById(R.id.grid_item_home_image_detail);
		Picasso.with(this).load(image).into(imageView);
		// Set the background color to black
		frameLayout = (FrameLayout) findViewById(R.id.main_background_home);
		colorDrawable = new ColorDrawable(Color.BLACK);
		// frameLayout.setBackground(colorDrawable);

		// Only run the animation if we're coming from the parent activity, not
		// if
		// we're recreated automatically by the window manager (e.g., device
		// rotation)
		if (savedInstanceState == null) {
			ViewTreeObserver observer = imageView.getViewTreeObserver();
			observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

				@Override
				public boolean onPreDraw() {
					imageView.getViewTreeObserver().removeOnPreDrawListener(
							this);

					// Figure out where the thumbnail and full size versions
					// are, relative
					// to the screen and each other
					int[] screenLocation = new int[2];
					imageView.getLocationOnScreen(screenLocation);
					mLeftDelta = thumbnailLeft - screenLocation[0];
					mTopDelta = thumbnailTop - screenLocation[1];

					// Scale factors to make the large version the same size as
					// the thumbnail
					mWidthScale = (float) thumbnailWidth / imageView.getWidth();
					mHeightScale = (float) thumbnailHeight
							/ imageView.getHeight();

					enterAnimation();

					return true;
				}
			});
		}
	}

	@SuppressLint("NewApi")
	public void enterAnimation() {

		// Set starting values for properties we're going to animate. These
		// values scale and position the full size version down to the thumbnail
		// size/location, from which we'll animate it back up
		imageView.setPivotX(0);
		imageView.setPivotY(0);
		imageView.setScaleX(mWidthScale);
		imageView.setScaleY(mHeightScale);
		imageView.setTranslationX(mLeftDelta);
		imageView.setTranslationY(mTopDelta);

		// interpolator where the rate of change starts out quickly and then
		// decelerates.
		TimeInterpolator sDecelerator = new DecelerateInterpolator();

		// Animate scale and translation to go from thumbnail to full size
		imageView.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1)
				.translationX(0).translationY(0).setInterpolator(sDecelerator);

		// Fade in the black background
		ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0,
				255);
		bgAnim.setDuration(ANIM_DURATION);
		bgAnim.start();

	}

	/**
	 * The exit animation is basically a reverse of the enter animation. This
	 * Animate image back to thumbnail size/location as relieved from bundle.
	 *
	 * @param endAction
	 *            This action gets run after the animation completes (this is
	 *            when we actually switch activities)
	 */
	@SuppressLint("NewApi")
	public void exitAnimation(final Runnable endAction) {

		TimeInterpolator sInterpolator = new AccelerateInterpolator();
		imageView.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale)
				.scaleY(mHeightScale).translationX(mLeftDelta)
				.translationY(mTopDelta).setInterpolator(sInterpolator)
				.withEndAction(endAction);

		// Fade out background
		ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
		bgAnim.setDuration(ANIM_DURATION);
		bgAnim.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		try {
			try {
				Intent myIntent = new Intent(HomeworkImageDisplay.this,
						MainActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				// myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myIntent);
				finish();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setFragment(Fragment fragment) {

		FrameLayout fm = (FrameLayout) findViewById(R.id.content_frame);
		fm.setBackgroundColor(Color.BLACK);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		ft.replace(R.id.content_frame, fragment);

		// Commit the transaction
		ft.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homework_image_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
