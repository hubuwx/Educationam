package com.educationam;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ParentLinksWebActivity extends ActionBarActivity {
	WebView wv;
	String getParentLink;
	ProgressDialog progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent_links_web);
		
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

		Intent i = getIntent();
		getParentLink = i.getStringExtra("parent");

		// if (android.os.Build.VERSION.SDK_INT <11) {
		// // getSupportActionBar().setTitle("IMS");
		// setTitle(Html.fromHtml("<font color='#ffffff'>Parent Links</font>"));
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#16a086")));
		// //getActionBar().setDisplayHomeAsUpEnabled(true);
		// //getActionBar().setHomeButtonEnabled(true);
		// getActionBar().show();
		//
		// }
		// else
		// {
		// // getActionBar().setTitle("IMS");
		// setTitle(Html.fromHtml("<font color='#ffffff'>Parent Links</font>"));
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#16a086")));
		// //getActionBar().setDisplayHomeAsUpEnabled(true);
		// //getActionBar().setHomeButtonEnabled(true);
		// getActionBar().show();
		//
		// }
		wv = (WebView) findViewById(R.id.webviewparent);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);
		progressBar = ProgressDialog.show(ParentLinksWebActivity.this, "",
				"loading...");
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		wv.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				Toast.makeText(ParentLinksWebActivity.this,
						"Oh no! " + description, Toast.LENGTH_SHORT).show();
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialog.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}
		});

		wv.loadUrl(getParentLink);
	}

}
