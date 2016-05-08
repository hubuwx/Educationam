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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
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

public class FeedbackFragment extends Fragment {

	private EditText txtfeedback;
	private TextView txterror;
	private Context context = getActivity();
	private LinearLayout LL;
	private TextView txtcancel;
	private TextView txtsend;
	private String feedback = "";

	SessionManager sessionManager;
	HashMap<String, String> userDetails;
	private LinearLayout LL1;
	private LinearLayout LL2;
	private RatingBar ratingUser;

	public FeedbackFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);

		sessionManager = new SessionManager(getActivity());
		userDetails = new HashMap<String, String>();
		userDetails = sessionManager.getUserDetails();

		try {

			txterror = (TextView) rootView.findViewById(R.id.txterror);
			ratingUser = (RatingBar) rootView.findViewById(R.id.ratingBar1user);
			txtfeedback = (EditText) rootView.findViewById(R.id.txtfeedback);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LL = (LinearLayout) rootView.findViewById(R.id.ff);

		LL1 = (LinearLayout) rootView.findViewById(R.id.ll1);

		LL2 = (LinearLayout) rootView.findViewById(R.id.ll2);

		LL.setVisibility(View.GONE);

		txtcancel = (TextView) rootView.findViewById(R.id.txtcancel);
		txtsend = (TextView) rootView.findViewById(R.id.txtsend);

		txtcancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		txtsend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_feedback, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement

		if (id == R.id.action_submit) {
			if (txtfeedback.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "Please Enter Feedback",
						Toast.LENGTH_SHORT).show();
			} else {
				if (NetConnectivity.isOnline(getActivity())) {
					try {

						new SendUserFeedBackToServer().execute();

					} catch (Exception e) {
						System.out.print("Errorr :" + e.getMessage());
						e.printStackTrace();
					}
				} else {

					txterror.setVisibility(View.VISIBLE);

				}

			}

		}

		return super.onOptionsItemSelected(item);
	}

	private class SendUserFeedBackToServer extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		String ans = "";
		private String vresval;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// /Write here statements

			try {
				ServiceHandler sh = new ServiceHandler();

				feedback = txtfeedback.getText().toString();

				feedback = feedback.replace(" ", "%20");
				feedback = feedback.replace("!", "%21");
				feedback = feedback.replace(" ", "%22");
				feedback = feedback.replace("#", "%23");
				feedback = feedback.replace("$", "%24");
				feedback = feedback.replace("%", "%25");
				feedback = feedback.replace("&", "%26");
				feedback = feedback.replace("'", "%27");
				feedback = feedback.replace("(", "%28");
				feedback = feedback.replace(")", "%29");
				feedback = feedback.replace("-", "%2D");
				feedback = feedback.replace(":", "%3A");
				feedback = feedback.replace(".", "%2E");
				feedback = feedback.replace(";", "%3B");
				feedback = feedback.replace("?", "%3F");
				feedback = feedback.replace("@", "%40");

				// String url = AllKeys.TAG_WEBSITE + "?action=feedback&uid="
				String url = AllKeys.WEBSITE+"GetJSONForFeedback?empid="
						+ userDetails.get(SessionManager.KEY_EMPID)
						+ "&feedback="
						+ feedback
						+ "&rate="
						+ ratingUser.getRating() + "&clientid="+AllKeys.CLIENT_ID;
//				String url1 = AllKeys.WEBSITE
//						+ "JSON_data.aspx?type=feedback&empid="
//						+ userDetails.get(SessionManager.KEY_EMPID)
//						+ "&Feedback=" + feedback + "&rate="
//						+ ratingUser.getRating() + "";// &uid="
				Log.d("FeedbackUrl : ", url);
				// http://radiant.dnsitexperts.com/JSON_data.aspx?type=feedback&empid=432&Feedback=hi%2520vvh%2520cghh%2520vgg&rate=3.5
				String ans = sh.makeServiceCall(url, ServiceHandler.GET);
				Log.d("Feedback Response : ", ans);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Feedback Error :", e.getMessage());
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			txtfeedback.setText("");
			// txtdescr.setText("");
			// txterror.setVisibility(View.VISIBLE);
			// txterror.setText("Thank You, Your feddback has been submitted");

			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Radiant");
			alert.setIcon(R.drawable.ic_launcher);
			alert.setMessage("Thanks For Feedback");

			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					// TODO Auto-generated method stub

					Intent ii = new Intent(getActivity(), MainActivity.class);
					startActivity(ii);
					getActivity().finish();

				}

			});
			alert.show();

			// Write statement after background process execution
		}
	}

}