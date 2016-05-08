package com.educationam;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoTrackActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


	private VideoView videoview1;
	private VideoView videoView;
	private int pos;
	private ProgressDialog progressDialog;
	private String url;
	private String[] vid;
	private String videoid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_video_track);

		try
		{

		}
		catch(Exception e)
		{

			System.out.println("Error :"+e.getMessage());
		}
		url=getIntent().getStringExtra("url");

		vid=url.split("/");
		videoid=vid[vid.length-1];
		YouTubePlayerView youTubeView = (YouTubePlayerView)
				findViewById(R.id.videoView1);
		youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);




	}



	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_video_track,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		pos=videoView.getCurrentPosition();
		videoView.pause();
		// etc.  
		super.onSaveInstanceState(savedInstanceState);  
	}  
	@Override  
	public void onRestoreInstanceState(Bundle savedInstanceState) {  
		super.onRestoreInstanceState(savedInstanceState);  
		// Restore UI state from the savedInstanceState.  
		// This bundle has also been passed to onCreate.  
		videoView.seekTo(pos);
		videoView.start();
	}



	public class DeveloperKey {
		/**
		 * Please replace this with a valid API key which is enabled for the
		 * YouTube Data API v3 service. Go to the
		 * <a href=�https://code.google.com/apis/console/�>Google APIs Console</a> to
		 * register a new developer key.
		 */
		public static final String DEVELOPER_KEY = "AIzaSyCqJms4ee4f3RlhQsJIdUUeZr1vO4Ak348";//AIzaSyCwRnzcFNUeO_20J5bsdy0B5F4gBd92jkU
	}


	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) player.cueVideo(videoid); // your video to play
	}
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1){
	}
}
