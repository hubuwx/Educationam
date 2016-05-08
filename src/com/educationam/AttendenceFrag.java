package com.educationam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AttendenceFrag extends Fragment implements OnClickListener {

	Activity a;
	private TextView currentMonth;
	// private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, year;
	private ArrayList<String> lstpresent = new ArrayList<String>();
	private ArrayList<String> lstabsent = new ArrayList<String>();
	private ArrayList<String> lstholidays = new ArrayList<String>();
	private ArrayList<String> lsttest = new ArrayList<String>();
	private ArrayList<String> lstall = new ArrayList<String>();

	private final DateFormat dateFormatter = new DateFormat();

	private GridView gridday;
	private static final String dateTemplate = "MMMM yyyy";
	String[] days = { "M", "T", "W", "T", "F", "S", "S" };
	private TextView presentdays;
	private TextView absentdays;
	private TextView holidays;
	private TextView testdays;

	private TextView totaldays;

	private TextView percentage;

	// ArrayList<HolidayItem> holidaylist=new ArrayList<HolidayItem>();
	public static AttendenceFrag create() {
		AttendenceFrag f = new AttendenceFrag();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.attendencexml, container, false);

		// selectedDayMonthYearButton =
		// (Button)v.findViewById(R.id.selectedDayMonthYears);
		// selectedDayMonthYearButton =
		// (Button)getActivity().findViewById(R.id.selectedDayMonthYear);
		// selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) v.findViewById(R.id.prevMonth);
		// prevMonth.setOnClickListener(this);

		currentMonth = (TextView) v.findViewById(R.id.currentMonths);
		presentdays = (TextView) v.findViewById(R.id.presentdays);
		absentdays = (TextView) v.findViewById(R.id.absentdays);
		holidays = (TextView) v.findViewById(R.id.lblholidays);
		testdays = (TextView) v.findViewById(R.id.lbltestDays);
		totaldays = (TextView) v.findViewById(R.id.totaldays);
		percentage = (TextView) v.findViewById(R.id.percentage);

		// currentMonth.setText(DateFormat.format(dateTemplate,
		// _calendar.getTime()));

		nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
		calendarView = (GridView) v.findViewById(R.id.calendar);
		gridday = (GridView) v.findViewById(R.id.gridView1);
		// nextMonth.setOnClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);

		// selectedDayMonthYearButton =
		// (Button)getActivity().findViewById(R.id.selectedDayMonthYears);
		// selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) getActivity().findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) getActivity().findViewById(R.id.currentMonths);
		currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) getActivity().findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);
		String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		CustomGrid adapterq = new CustomGrid(getActivity(), days);
		gridday.setAdapter(adapterq);

		// Initialised
		// gridday.setAdapter(new
		// ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_2,days));
		// gridday.setAdapter(new MyAdapter(getActivity(), days));
		adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}

			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}

			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, String> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		private String d;
		private char dc;
		private String m;
		private String thistheday;

		// Days in Current Month

		public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
			super();
			this._context = getActivity();
			this.list = new ArrayList<String>();

			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

			// Print Month
			printMonth(month, year);
			// Find Number of Events

			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
			Log.d("Present Days : ", "" + lstpresent.size());
			Log.d("Absent Days : ", "" + lstabsent.size());
			Log.d("All Days : ", "" + lstall.size());
			Log.d("Holidays : ", "" + lstholidays.size());
			Log.d("Test Days : ", "" + lsttest.size());
			try {
				double per = (Double.parseDouble("" + lstpresent.size()) * 100)
						/ Double.parseDouble("" + lstall.size());
				Log.d("Percentage :", "" + per);
				String result = String.format("%.2f", per);

				Log.d("Percentage Result :", "" + result);

				presentdays.setText("" + lstpresent.size());
				absentdays.setText("" + lstabsent.size());
				totaldays.setText("" + lstall.size());
				holidays.setText("" + lstholidays.size());
				testdays.setText("" + lsttest.size());
				if (!result.equals("NaN")) {
					percentage.setText("" + result + " %");
				} else {
					percentage.setText("0");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Error ", e.getMessage());
				e.printStackTrace();
			}

			// System.out.print("Present Days : "+lstpresent.size());
			// System.out.print("Absent Days : "+lstabsent.size());
			// System.out.print("All Days : "+lstall.size());

		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);

			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " "
						+ String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-"
						+ getMonthAsString(prevMonth) + "-" + prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, String> findNumberOfEventsPerMonth(int year, int month) {
			HashMap<String, String> map = new HashMap<String, String>();
			dbhandler db = new dbhandler(getActivity());
			SQLiteDatabase sd = db.getReadableDatabase();
			if (month < 10) {
				m = "0" + month;
			} else
				m = "" + month;

			Cursor c = sd.rawQuery("select * from studattendence where date like '%" + year + "-" + m + "%'", null);
			getActivity().startManagingCursor(c);

			lstabsent.clear();
			lstpresent.clear();
			lstholidays.clear();
			lsttest.clear();
			lstall.clear();

			while (c.moveToNext()) {
				String days[] = c.getString(0).split("-");
				int day = Integer.parseInt(days[2]);
				if (day < 10) {
					d = "0" + day;
				} else {
					d = "" + day;
				}
				if (c.getString(1).equals("P")) {
					lstpresent.add("P");
					lstall.add("P");
				} else if (c.getString(1).equals("A")) {
					lstabsent.add("A");
					lstall.add("A");

				} else if (c.getString(1).equals("H")) {
					// lstabsent.add("H");
					lstholidays.add("H");
					lstall.add("H");

				} else if (c.getString(1).equals("T")) {
					// lstabsent.add("T");
					lsttest.add("T");
					lstall.add("T");

				}
				map.put("" + d, c.getString(1));
			}
			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}
			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);
			num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
			// num_events_per_day.setText(" A ");
			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			int dayofdate = Integer.parseInt(theday);
			if (dayofdate <= 9) {
				thistheday = "0" + theday;
			} else
				thistheday = theday;
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(thistheday)) {
					num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
					Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
					String numEvents = eventsPerMonthMap.get(thistheday);

					if (numEvents.equals("P")) {
						num_events_per_day.setTextColor(Color.rgb(37, 105, 35));
						num_events_per_day.setText(numEvents.toString());
						num_events_per_day.setTextSize(18);
						num_events_per_day.setTypeface(boldTypeface);
					}
					if (numEvents.equals("A")) {
						num_events_per_day.setTextColor(Color.rgb(208, 45, 45));
						num_events_per_day.setText(numEvents.toString());
						num_events_per_day.setTextSize(18);
						num_events_per_day.setTypeface(boldTypeface);
					}
					if (numEvents.equals("H")) {
						num_events_per_day.setTextColor(Color.rgb(204, 102, 0));
						num_events_per_day.setText(numEvents.toString());
						num_events_per_day.setTextSize(18);
						num_events_per_day.setTypeface(boldTypeface);
					}
					if (numEvents.equals("T")) {
						num_events_per_day.setTextColor(Color.rgb(58, 48, 148));
						num_events_per_day.setText(numEvents.toString());
						num_events_per_day.setTextSize(18);
						num_events_per_day.setTypeface(boldTypeface);
					}
					// num_events_per_day.setText("P");
				}
			}

			// Set the Day GridCell

			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources().getColor(R.color.lightgray));
				num_events_per_day.setText("");
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(R.color.lightgray02));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			// selectedDayMonthYearButton.setText("Selected: " +
			// date_month_year);
			Log.e("Selected date", date_month_year);
			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				Log.d(tag, "Parsed Date: " + parsedDate.toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	public class CustomGrid extends BaseAdapter {
		private Context mContext;
		private final String[] web;

		public CustomGrid(Context c, String[] web) {
			mContext = c;

			this.web = web;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return web.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				grid = new View(mContext);
				grid = inflater.inflate(R.layout.grid_item, null);
				TextView textView = (TextView) grid.findViewById(R.id.textView1);

				textView.setText(web[position]);
			} else {
				grid = (View) convertView;
				TextView textView = (TextView) grid.findViewById(R.id.textView1);

				textView.setText(web[position]);
			}
			return grid;
		}
	}

}
