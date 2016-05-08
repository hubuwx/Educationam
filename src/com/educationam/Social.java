package com.educationam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Social extends android.support.v4.app.Fragment {

	ImageView imgFb, imgYoutube, imgInstagram, imgTwitter;

	public static TestSolution create() {
		TestSolution f = new TestSolution();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.social_frag, container, false);

		imgFb = (ImageView) v.findViewById(R.id.imgFb);
		imgYoutube = (ImageView) v.findViewById(R.id.imgYoutube);
		imgInstagram = (ImageView) v.findViewById(R.id.imgInstagram);
		imgTwitter = (ImageView) v.findViewById(R.id.imgTwitter);

		imgFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), SocialWebActivity.class);
				i.putExtra("url", "https://www.facebook.com/Educationam/");
				startActivity(i);
			}
		});
		imgYoutube.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), SocialWebActivity.class);
				i.putExtra("url", "https://www.youtube.com/c/educationam");
				startActivity(i);
			}
		});
		imgInstagram.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), SocialWebActivity.class);
				i.putExtra("url", "https://www.instagram.com/educationam_official");
				startActivity(i);
			}
		});
		imgTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), SocialWebActivity.class);
				i.putExtra("url", "https://www.twitter.com/educationam");
				startActivity(i);
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
