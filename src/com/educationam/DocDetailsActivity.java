package com.educationam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DocDetailsActivity extends ActionBarActivity {

	String mainfile, docfile, imgfile;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_details);
		getSupportActionBar().show();
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
		webView = (WebView) findViewById(R.id.webDocFileView);

		try {
			Intent i = getIntent();
			docfile = i.getStringExtra("FILE");

			ConnectionDetector cd = new ConnectionDetector(
					DocDetailsActivity.this);

			if (cd.isConnectingToInternet() == true) {
				// new DownloadWebPageTask().execute("");
				// getalldata();
				String link = "https://docs.google.com/viewer?url=" + docfile;

				startWebView("https://docs.google.com/viewer?url=" + docfile);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startWebView(String url) {

		// Create new webview Client to show progress dialog
		// When opening a url or click on link

		webView.setWebViewClient(new WebViewClient() {
			ProgressDialog progressDialog;

			// If you will not use this method url links are opeen in new brower
			// not in webview

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					// in standard case YourActivity.this
					progressDialog = new ProgressDialog(DocDetailsActivity.this);
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
			}

			public void onPageFinished(WebView view, String url) {
				try {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		// Javascript inabled on webview
		webView.getSettings().setJavaScriptEnabled(true);

		// Other webview options
		/*
		 * webView.getSettings().setLoadWithOverviewMode(true);
		 * webView.getSettings().setUseWideViewPort(true);
		 * webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		 * webView.setScrollbarFadingEnabled(false);
		 * webView.getSettings().setBuiltInZoomControls(true);
		 */

		/*
		 * String summary =
		 * "<html><body>You scored <b>192</b> points.</body></html>";
		 * webview.loadData(summary, "text/html", null);
		 */

		// Load url in webview
		// webView.reload();
		webView.loadUrl(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doc_details, menu);
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
