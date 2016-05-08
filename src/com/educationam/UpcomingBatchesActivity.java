package com.educationam;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpcomingBatchesActivity extends ActionBarActivity {

	private WebView webView;
	ProgressBar pgrShowAcademicSync;
	TextView lblShowAcademicText;
	private SessionManager sessionManager;
	private Context context = this;
	private HashMap<String, String> userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upcoming_batches);
		getSupportActionBar().show();
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

		try {

			// Toast.makeText(getActivity(), "Please wait.... ",
			// Toast.LENGTH_SHORT).show();
			sessionManager = new SessionManager(context);
			userDetails = new HashMap<String, String>();
			userDetails = sessionManager.getUserDetails();
			webView = (WebView) findViewById(R.id.webUpcomingBatches);
			pgrShowAcademicSync = (ProgressBar) findViewById(R.id.pgrShowUpcomingBatches);

			String url1 = "" + AllKeys.PANEL_GENERIC
					+ "page.aspx?type=toppers&clientid=" + AllKeys.CLIENT_ID
					+ "&branch=" + userDetails.get(SessionManager.KEY_BRANCHID);
			pgrShowAcademicSync.setVisibility(View.VISIBLE);

			webView.setVisibility(View.GONE);
			startWebView(url1);
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
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {
				// if (progressDialog == null) {
				// // in standard case YourActivity.this
				// progressDialog = new ProgressDialog(getActivity());
				// progressDialog.setMessage("Loading...");
				// progressDialog.show();
				// }
			}

			public void onPageFinished(WebView view, String url) {
				try {
					pgrShowAcademicSync.setVisibility(View.GONE);

					webView.setVisibility(View.VISIBLE);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});

		// Javascript inabled on webview
		webView.getSettings().setJavaScriptEnabled(true);

		webView.reload();
		webView.loadUrl(url);

	}
}
