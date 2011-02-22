package jp.sblog.parabola.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class LogLogingMain extends Activity {
	
	// Preference file
	public static final String PREFERENCES_FILE_NAME = "Preference";
	
	// Category
	private Spinner spinner;
	
	// Data
	private LogDataHelper ldh;
	private SQLiteDatabase db;
	
	private int total;
	private int hours;
	private int mins;
	
	private void initCategory(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("Listening");
		adapter.add("Kindle");
		adapter.add("Other");
		adapter.add("Proc/Make");
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
		SharedPreferences settings = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		// pass
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Preference
		readPref();
		
		// initialize
		total = hours = mins = 0;
		initCategory();
		ldh = new LogDataHelper(this);
		db = ldh.getWritableDatabase();
		
		// set event
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
		
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// thorw data
				TextView titlev = (TextView)findViewById(R.id.tasktitle); 
				String title = titlev.getText().toString();
				String str = (String)spinner.getSelectedItem();
				saveData(title, str, total);
				titlev.setText("");
				total = mins = hours = 0;
				resetTimer();
			}
		});
	}

	private void saveData(String title, String category, int total){
		int h = total / 60;
		int m = total % 60;
		
		if (title == "") 
			title = "no title task";
		
		ContentValues values = new ContentValues();
		values.put("hrs", h);
		values.put("mins", m);
		values.put("category", category);
		values.put("title", title);
		db.insert("logdata", null, values);
	}

	// Menu
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "óöó");
		menu.add(0, 10, 0, "ê›íË");
		return true;
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		super.onMenuItemSelected(featureId, item);
		switch(item.getItemId()){
		case 0:
			Intent intent = new Intent(LogLogingMain.this, LogList.class);
			startActivityForResult(intent, 0);
			// Toast.makeText(LogLogingMain.this, "ñ¢é¿ëïÇ»Ç§", Toast.LENGTH_LONG).show();
			break;
		case 10:
			Toast.makeText(LogLogingMain.this, "ñ¢é¿ëïÇ»Ç§", Toast.LENGTH_LONG).show();
			break;
		}
		return true;
	}
	
}