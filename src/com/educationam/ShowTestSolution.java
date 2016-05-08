package com.educationam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class ShowTestSolution extends ActionBarActivity {

	private WebView wvtest;
	private String title;
	private String solutionpath;
	private Button btnaskquestion;
	private String btnvisibility = "0";
	private String questionid;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_test_solution);

		try {
			Intent in = getIntent();

			title = in.getStringExtra("title");
			solutionpath = in.getStringExtra("solutionpath");
			btnvisibility = in.getStringExtra("visibility");
			questionid = in.getStringExtra("questionid");

			// setTitle(""+title);
		} catch (Exception e) {
			System.out.print("Error :" + e.getMessage());
		}

		try {
			if (android.os.Build.VERSION.SDK_INT > 11) {
				// getSupportActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title + "</font>"));

				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().show();
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));

			} else {
				// getActionBar().setTitle("IMS");
				setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title + "</font>"));
				getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(AllKeys.THEME_COLOR)));
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().show();

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		wvtest = (WebView) findViewById(R.id.wvtest);

		btnaskquestion = (Button) findViewById(R.id.btnaskquestion);
		wvtest.getSettings().setJavaScriptEnabled(true);
		wvtest.getSettings().setBuiltInZoomControls(true);
		wvtest.getSettings().setSupportZoom(true);

		// wvtest.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		if (btnvisibility.equals("0")) {
			btnaskquestion.setVisibility(View.GONE);

			String Data = "https://docs.google.com/gview?embedded=true&url=" + solutionpath;
			// Toast.makeText(getApplicationContext(), "Solution
			// PAth:"+solutionpath, Toast.LENGTH_SHORT).show();
			wvtest.reload();
			ShowTestSolution.this.wvtest.loadUrl("https://docs.google.com/gview?embedded=true&url=" + solutionpath);

			Toast.makeText(getApplicationContext(), "Please wait solution loading..", Toast.LENGTH_LONG).show();
		} else if (btnvisibility.equals("contact")) {
			btnaskquestion.setVisibility(View.GONE);

			// String Data =
			// "https://docs.google.com/gview?embedded=true&url="+solutionpath;
			// Toast.makeText(getApplicationContext(), "Solution
			// PAth:"+solutionpath, Toast.LENGTH_SHORT).show();

			wvtest.reload();
			wvtest.loadUrl("" + solutionpath);

		} else {
			ShowTestSolution.this.btnaskquestion.setVisibility(View.VISIBLE);

			// String Data =
			// "https://docs.google.com/gview?embedded=true&url="+solutionpath;
			// Toast.makeText(getApplicationContext(), "Solution
			// PAth:"+solutionpath, Toast.LENGTH_SHORT).show();
			wvtest.reload();
			ShowTestSolution.this.wvtest.loadUrl("" + solutionpath);

		}

		btnaskquestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent inten = new Intent(getApplicationContext(), AskQuestionActivity.class);
				inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				inten.putExtra("ans_status", "1");
				inten.putExtra("repeat_status", "" + questionid);
				inten.putExtra("visibility", "0");
				startActivity(inten);
				finish();

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

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
		if (item.getItemId() == android.R.id.home) {

			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);

			onBackPressed();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
