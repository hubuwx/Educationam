package com.educationam;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class LinkWebview extends Activity{
	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linkwebview);

		String url=getIntent().getStringExtra("URL");
		wv=(WebView)findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(url);	
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
