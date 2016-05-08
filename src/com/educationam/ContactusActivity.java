package com.educationam;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ContactusActivity extends ActionBarActivity {

	private SessionManager sessionManager;
	private Context context = this;
	private HashMap<String, String> userDetails;
	private WebView webView;
	ProgressBar pgrShowAcademicSync;
	TextView lblShowAcademicText;
	ImageView imgError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		getSupportActionBar().show();
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
		setTitle("Contact us");
		// getActionBar().hide();
		sessionManager = new SessionManager(context);
		userDetails = new HashMap<String, String>();
		userDetails = sessionManager.getUserDetails();

		webView = (WebView) findViewById(R.id.webContactus);
		pgrShowAcademicSync = (ProgressBar) findViewById(R.id.pgrShowContactus);

		String url1 = "" + AllKeys.PANEL_GENERIC
				+ "page.aspx?type=contact&clientid=" + AllKeys.CLIENT_ID
				+ "&branch=" + userDetails.get(SessionManager.KEY_BRANCHID);
		pgrShowAcademicSync.setVisibility(View.VISIBLE);

		webView.setVisibility(View.GONE);
		startWebView(url1);

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
				// progressDialog = new ProgressDialog(AboutusActivity.this);
				// progressDialog.setMessage("Loading...");
				// progressDialog.show();
				// }

			}

			public void onPageFinished(WebView view, String url) {
				try {
					// if (progressDialog.isShowing()) {
					// progressDialog.dismiss();
					// progressDialog = null;
					// }
					pgrShowAcademicSync.setVisibility(View.GONE);

					webView.setVisibility(View.VISIBLE);
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

}
