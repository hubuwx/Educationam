package com.educationam;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhandler extends SQLiteOpenHelper {

	public static final String db = "studdb19.db";

	public static final String TABLE_ASKQUESTIONS = "askquestions";
	public static final String ASKQUESTIONS_ID = "questionid";
	public static final String ASKQUESTIONS_QUESTION = "question";
	public static final String ASKQUESTIONS_FAC_ID = "facid";
	public static final String ASKQUESTIONS_FAC_NAME = "facname";
	public static final String ASKQUESTIONS_IMAGE_NAME = "imagname";
	public static final String ASKQUESTIONS_DATETIME = "datetime";
	public static final String ASKQUESTIONS_STUDID = "studid";
	public static final String ASKQUESTIONS_ANS_STATUS = "ans_status";
	public static final String ASKQUESTIONS_ANSURL = "ansurl";

	// BusMst Related Keys

	public dbhandler(Context context) {
		super(context, db, null, 37);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {

			db.execSQL("create table IF NOT EXISTS staffinfo(facid INTEGER,facname TEXT)");

			db.execSQL("CREATE TABLE IF NOT EXISTS NOTES(id INTEGER PRIMARY KEY AUTOINCREMENT not null,notetitle TEXT,notedesc TEXT,notedate DATETIME,notetime TEXT)");
			db.execSQL("create table IF NOT EXISTS holiday(holidayid INTEGER,holidaynname TEXT,hdate TEXT,hday TEXT)");
			db.execSQL("create table IF NOT EXISTS busmst(id INTEGER,busname TEXT,drivername TEXT,vehicleno TEXT,busincharge TEXT,inchargeno TEXT,mobileno TEXT)");

			db.execSQL("create table IF NOT EXISTS staffinfo(facid INTEGER,facname TEXT)");
			db.execSQL("create table IF NOT EXISTS stdmaster(stdid INTEGER,stdname TEXT)");
			db.execSQL("create table IF NOT EXISTS submaster(subid INTEGER,subname TEXT)");
			db.execSQL("create table IF NOT EXISTS verificaton(otp TEXT,stdid TEXT)");
			db.execSQL("create table IF NOT EXISTS categorydownload(id TEXT,catname TEXT)");
			db.execSQL("create table IF NOT EXISTS downloads(id TEXT,title TEXT,catid TEXT,Link TEXT,FileName TEXT)");
			db.execSQL("create table IF NOT EXISTS upcomingtest(id TEXT,testname TEXT,subject TEXT,date datetime)");

			db.execSQL("create table IF NOT EXISTS DeptMaster(DEPTID INTEGER,DEPTNAME TEXT)");

			db.execSQL("create table IF NOT EXISTS dataurl(url TEXT)");
			db.execSQL("create table IF NOT EXISTS studinfo(deptid INTEGER,empid INTEGER,name TEXT,type TEXT,branchid TEXT,deptname TEXT)");
			db.execSQL("create table IF NOT EXISTS studattendence(date DATETIME,Flag TEXT)");
			db.execSQL("create table IF NOT EXISTS album(id INTEGER,albumid TEXT,name TEXT,image TEXT)");
			db.execSQL("create table IF NOT EXISTS albumphoto(photoid INTEGER,thumb TEXT,image TEXT)");
			db.execSQL("create table IF NOT EXISTS time_tb_mst(deptid INTEGER,time TEXT,day TEXT,subject TEXT)");
			db.execSQL("create table IF NOT EXISTS homework(id INTEGER PRIMARY KEY AUTOINCREMENT,homework TEXT,faculty TEXT,date TEXT,homeworktype TEXT,imagename TEXT,subject TEXT)");

			db.execSQL("create table IF NOT EXISTS achievementsmst(id INTEGER PRIMARY KEY AUTOINCREMENT,achievement TEXT,faculty TEXT,date TEXT,studname TEXT)");

			db.execSQL("create table IF NOT EXISTS remarkmst(id INTEGER PRIMARY KEY AUTOINCREMENT,date TEXT,faculty TEXT,rematk TEXT)");
			db.execSQL("create table IF NOT EXISTS event(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,time TEXT,desc TEXT,date TEXT)");

			db.execSQL("create table IF NOT EXISTS uselink(linkid INTEGER,catid INTEGER,linkname TEXT,link TEXT,linkdesc TEXT)");
			db.execSQL("create table IF NOT EXISTS linkcategory(catid INTEGER,categoryname TEXT)");
			db.execSQL("create table IF NOT EXISTS submaster(subid INTEGER,subname TEXT)");
			db.execSQL("create table IF NOT EXISTS noticeboard(id INTEGER,shortdesc TEXT,longdesc TEXT,date TEXT,isshown TEXT)");
			db.execSQL("create table IF NOT EXISTS studactivity(id INTEGER,activityname TEXT,date TEXT,desc TEXT)");

			db.execSQL("create table IF NOT EXISTS appoinment(id INTEGER,date DATETIME,TIME TEXT,reason TEXT,deptid TEXT,empid TEXT,flag TEXT)");

			db.execSQL("create table IF NOT EXISTS Video(videoid INTEGER,videoname TEXT,videourl TEXT,Tumbcode TEXT,deptid INTEGER,subid INTEGER,chapter TEXT)");

			db.execSQL("create table IF NOT EXISTS MobileTitle(titleid INTEGER,title TEXT,content TEXT)");
			db.execSQL("create table IF NOT EXISTS notificationmsg(title TEXT,content TEXT)");
			db.execSQL("create table IF NOT EXISTS Notification_Mst(id INTEGER PRIMARY KEY AUTOINCREMENT,header TEXT,notification TEXT,ndate text)");

			db.execSQL("create table IF NOT EXISTS testsolution(id INTEGER PRIMARY KEY AUTOINCREMENT,filepath TEXT,title TEXT)");

			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ASKQUESTIONS + "("
					+ ASKQUESTIONS_ID + " INTEGER PRIMARY KEY,"
					+ ASKQUESTIONS_QUESTION + " TEXT," + ASKQUESTIONS_FAC_ID
					+ " INTEGER," + ASKQUESTIONS_FAC_NAME + " TEXT,"
					+ ASKQUESTIONS_IMAGE_NAME + " TEXT,"
					+ ASKQUESTIONS_DATETIME + " TEXT," + ASKQUESTIONS_STUDID
					+ " INTEGER," + ASKQUESTIONS_ANS_STATUS + " INTEGER,"
					+ ASKQUESTIONS_ANSURL + " TEXT) ");

			db.execSQL("create table IF NOT EXISTS birthday(studid INTEGER,name TEXT,bdate DATETIME)");
			db.execSQL("create table  IF NOT EXISTS testmaster(testid INTEGER,testdate TEXT,testname TEXT,totmark TEXT,passingmrk INTEGER,stdId INTEGER,SUbject TEXT)");
			db.execSQL("create table IF NOT EXISTS  testdetail(testid INTEGER,rollno INTEGER,name TEXT,subid INTEGER,submak TEXT,OBTmak TEXt,highmark INTEGER,stdId INTEGER)");

		} catch (SQLException e) {
			// TODO Auto-generated catch blockF
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASKQUESTIONS);
			// db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ASKQUESTIONS);

			db.setVersion(newVersion);
			onCreate(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getAllLabels() {
		List<String> labels = new ArrayList<String>();

		// Select All Query
		try {
			String selectQuery = "SELECT  * FROM submaster";

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					labels.add(cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// returning lables
		return labels;
	}

}
