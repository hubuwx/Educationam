package com.educationam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class SocialWebActivity extends ActionBarActivity {

	private WebView wvsocial;
	ProgressBar pgrShowAcademicSync;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_web);

		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

		Intent i = getIntent();
		url = i.getStringExtra("url");
		wvsocial = (WebView) findViewById(R.id.wvsociallinks);
		pgrShowAcademicSync = (ProgressBar) findViewById(R.id.pgrShowLinks);
		pgrShowAcademicSync.setVisibility(View.VISIBLE);
		wvsocial.setVisibility(View.GONE);
		startWebView(url);
	}

	private void startWebView(String url) {

		// Create new webview Client to show progress dialog
		// When opening a url or click on link

		wvsocial.setWebViewClient(new WebViewClient() {
			ProgressDialog progressDialog;

			// If you will not use this method url links are opeen in new brower
			// not in webview
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {

			}

			public void onPageFinished(WebView view, String url) {
				try {
					pgrShowAcademicSync.setVisibility(View.GONE);
					wvsocial.setVisibility(View.VISIBLE);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		// Javascript inabled on webview
		wvsocial.getSettings().setJavaScriptEnabled(true);

		wvsocial.reload();
		wvsocial.loadUrl(url);

	}
}
