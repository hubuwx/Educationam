package com.educationam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

public class FragmentEvents extends Fragment {

	private WebView wvsocial;
	private Context context = getActivity();

	ProgressBar pgrShowAcademicSync;
	TextView lblShowAcademicText;

	public FragmentEvents() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_socialmedia,
				container, false);
		try {

			// Toast.makeText(getActivity(), "Please wait.... ",
			// Toast.LENGTH_SHORT).show();
			wvsocial = (WebView) rootView.findViewById(R.id.wvsocial);
			pgrShowAcademicSync = (ProgressBar) rootView
					.findViewById(R.id.pgrShowAcademicSync);
			lblShowAcademicText = (TextView) rootView
					.findViewById(R.id.lblSyncTextAcademic);

			String url1 = "" + AllKeys.WEBSITE_EVENTS;
			pgrShowAcademicSync.setVisibility(View.VISIBLE);
			lblShowAcademicText.setVisibility(View.VISIBLE);
			wvsocial.setVisibility(View.GONE);
			startWebView(url1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
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
					lblShowAcademicText.setVisibility(View.GONE);
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}