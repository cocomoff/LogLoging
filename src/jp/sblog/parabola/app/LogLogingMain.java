package jp.sblog.parabola.app;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class LogLogingMain extends Activity {
	
	// Preference file
	public static final String PREFERENCES_FILE_NAME = "Preference";
	
	// Category
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	
	// Data
	private LogDataHelper ldh;
	private SQLiteDatabase db;
	
	// TaskData
	private int total;
	private int hours;
	private int mins;
	private int year;
	private int month;
	private int day;
	
	/*
	private boolean isExist(String text){
		Cursor cursor = db.query("category", new String[]{"id", "cname"}, null, null, null, null, "id");
		while(cursor.moveToNext()){
			if(cursor.getString(1).equals(text))
				return true;
		}
		return false;
	}
	*/
	
	private void initCategory(){
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner)findViewById(R.id.Category);
		spinner.setAdapter(adapter);
	}
	
	private void resetTimer(){
		hours = total / 60;
		mins  = total % 60;
		
		String hstr = hours >= 10 ? hours + "" : "0" + hours;
		String mstr = mins >= 10 ? mins + "" : "0" + mins;
		
		String times = hstr + ":" + mstr ;
		TextView l = (TextView)findViewById(R.id.timetext);
		l.setText(times);
	}
	
	private void readPref(){
		// SharedPreferences settings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
		// SharedPreferences.Editor editor = settings.edit();
	}
	
	private void initDate(){
		Calendar cal = Calendar.getInstance();
		Log.d("LOGLOG", Calendar.YEAR + "");
		year = cal.YEAR;
		month = cal.MONTH + 1;
		day = cal.DAY_OF_MONTH;
		TextView tv = (TextView)findViewById(R.id.datetext);
		tv.setText(String.format("%04d/%02d/%02d", year, month, day));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Preference -- not implemented
		readPref();
		initDate();
		
		// initialize
		total = hours = mins = 0;
		ldh = new LogDataHelper(this);
		db = ldh.getWritableDatabase();
		initCategory();
		setEvents();
	}
	
	private void setEvents(){
		Button b1 = (Button)findViewById(R.id.button1);
		Button b2 = (Button)findViewById(R.id.button2);
		Button b3 = (Button)findViewById(R.id.button3);
		Button b4 = (Button)findViewById(R.id.button4);
		Button done = (Button)findViewById(R.id.submit);
		
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				total += 5;
				resetTimer();
			}
		});
		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				total += 10;
				resetTimer();
			}
		});
		
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				total += 30;
				resetTimer();
			}
		});
		
		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				total += 60;
				resetTimer();
			}
		});
		
		// Done Tasks
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// throw data
				TextView titlev = (TextView)findViewById(R.id.tasktitle); 
				String title = titlev.getText().toString();
				String str = (String)spinner.getSelectedItem();
				saveData(title, str, total, year, month, day);
				titlev.setText("");
				total = mins = hours = 0;
				resetTimer();
			}
		});
		
		// Refresh Category
		TextView tv1 = (TextView)findViewById(R.id.text_category);
		tv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// category data.
				Cursor cursor = db.query("category", new String[]{"id", "cname"}, null, null, null, null, "id");
				ArrayList<String> cList = new ArrayList<String>();
				adapter = new ArrayAdapter<String>(LogLogingMain.this, android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				while(cursor.moveToNext()){
					String cname = cursor.getString(1);
					cList.add(cname);
				}
				for(String s : cList){
					Log.d("LOGLOG", s);
					adapter.add(s);
				}
				spinner.setAdapter(adapter);
			}
		});
		
		// Set Date
		TextView tv2 = (TextView)findViewById(R.id.text_date);
		// get default year, month , date
		Calendar now = Calendar.getInstance();
		final DatePickerDialog dialog = new DatePickerDialog(LogLogingMain.this, listener, now.YEAR, now.MONTH+1, now.DATE);
		tv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			TextView tv = (TextView)findViewById(R.id.datetext);
			tv.setText(String.format("%04d/%02d/%02d", year, monthOfYear+1, dayOfMonth));
		}
	};

	// Data to Database
	private void saveData(String title, String category, int total, int year, int month, int day){
		int h = total / 60;
		int m = total % 60;
		
		String s = title.equals("") ? "no title" : title;
		ContentValues values = new ContentValues();
		values.put("hrs", h);
		values.put("mins", m);
		values.put("year", year);
		values.put("month", month);
		values.put("day", day);
		values.put("category", category);
		values.put("title", s);
		db.insert("logdata", null, values);
	}

	// Menu Settings
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0,  0, 0, "óöó");
		menu.add(0, 10, 0, "ÉJÉeÉSÉäê›íË");
		menu.add(0, 20, 0, "ê›íË");
		return true;
	}
	
	// Menu Events
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		super.onMenuItemSelected(featureId, item);
		Intent intent;
		switch(item.getItemId()){
		case 0:
			intent = new Intent(LogLogingMain.this, LogList.class);
			startActivityForResult(intent, 0);
			break;
		case 10:
			intent = new Intent(LogLogingMain.this, CManage.class);
			startActivity(intent);
			break;
		case 20:
			Toast.makeText(LogLogingMain.this, "ñ¢é¿ëïÇ»Ç§", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}
}