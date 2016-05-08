package com.educationam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ChartActivity extends ActionBarActivity {

	private String chart;
	private WebView web;
	private String html;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_chart);

		chart = getIntent().getStringExtra("chart");

		web = (WebView) findViewById(R.id.webView1);
		// WebView web=new WebView(context);
		web.getSettings().setJavaScriptEnabled(true);

		if (chart.equals("pie")) {
			html = "<html>"
					+ "<head>"
					+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
					+ "<script type=\"text/javascript\">"
					+ "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
					+ "google.setOnLoadCallback(drawChart);"
					+ "function drawChart() {"
					+ "var data = google.visualization.arrayToDataTable(["
					+ "['Task', 'Hours per Day'],"
					+ "['Present',     11],"
					+ "['Absent',      7],"
					+ "['Sunday',  5],"
					+ "['Holiday', 2],"
					+

					"]);"
					+

					"var options = {"
					+ "title: 'Student Daily Attendece',"
					+ "is3D: true,"
					+ "};"
					+

					"var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));"
					+ "chart.draw(data, options);"
					+ "}"
					+ "</script>"
					+ "</head>"
					+ "<body>"
					+ "<div id=\"piechart_3d\" style=\"width: 900px; height: 500px;\"></div>"
					+ "</body>" + "</html>";
		} else if (chart.equals("bar")) {
			html = "<html>"
					+ "<head>"
					+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
					+ "<script type=\"text/javascript\">"
					+ "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
					+ "google.setOnLoadCallback(drawChart);"
					+ "function drawChart() {"
					+ "var data = google.visualization.arrayToDataTable(["
					+ "['Year', 'Absent', 'Present'],"
					+ "['2004',  1000,      400],"
					+ "['2005',  1170,      460],"
					+ "['2006',  660,       1120],"
					+ "['2007',  1030,      540]"
					+ "]);"
					+

					"var options = {"
					+ "title: 'Student Performance',"
					+ " vAxis: {title: 'Year',  titleTextStyle: {color: 'red'}}"
					+ "};"
					+

					"var chart = new google.visualization.BarChart(document.getElementById('chart_div'));"
					+ "chart.draw(data, options);"
					+ "}"
					+ "</script>"
					+ "</head>"
					+ "<body>"
					+ "<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
					+ "</body>" + "</html>";
		} else if (chart.equals("core")) {
			html = "<html>"
					+ "<head>"
					+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
					+ "<script type=\"text/javascript\">"
					+ "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
					+ "google.setOnLoadCallback(drawChart);"
					+ "function drawChart() {"
					+

					"var data = google.visualization.arrayToDataTable(["
					+ "['Absent', 'Present', 'Holiday', 'Sunday', 'General',"
					+ "'Western', 'Literature', { role: 'annotation' } ],"
					+ "['2010', 10, 24, 20, 32, 18, 5, ''],"
					+ "['2020', 16, 22, 23, 30, 16, 9, ''],"
					+ "['2030', 28, 19, 29, 30, 12, 13, ''],"
					+ "]);"
					+

					"var options = {"
					+ "width: 600,"
					+ "height: 400,"
					+ "legend: { position: 'top', maxLines: 3 },"
					+ "bar: { groupWidth: '75%' },"
					+ "isStacked: true,"
					+ "};"
					+

					"var chart = new google.visualization.BarChart(document.getElementById('chart_div'));"
					+ "chart.draw(data, options);"
					+ "}"
					+ "</script>"
					+ "</head>"
					+ "<body>"
					+ "<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
					+ "</body>" + "</html>";
		} else {
			html = "<html>"
					+ "<head>"
					+ "<script type='text/javascript' src='https://www.google.com/jsapi'></script>"
					+ "<script type='text/javascript'>"
					+ "google.load('visualization', '1', {packages:['gauge']});"
					+ "google.setOnLoadCallback(drawChart);"
					+ "function drawChart() {"
					+ "var data = google.visualization.arrayToDataTable(["
					+ "['Label', 'Value'],"
					+ "['Absent', 80],"
					+ "['Present', 55],"
					+ "['Holiday', 68]"
					+ "]);"
					+

					"var options = {"
					+ "width: 400, height: 120,"
					+ "redFrom: 90, redTo: 100,"
					+ "yellowFrom:75, yellowTo: 90,"
					+ "minorTicks: 5"
					+ "};"
					+ "var chart = new google.visualization.Gauge(document.getElementById('chart_div'));"
					+ "chart.draw(data, options);" + "}" + "</script>"
					+ "</head>" + "<body>" + "<div id='chart_div'></div>"
					+ "</body>" + "</html>";
		}
		web.loadData("" + html, "text/html", "UTF-8");
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_chart,
					container, false);
			return rootView;
		}
	}

}
