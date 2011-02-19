package jp.sblog.parabola.app;

import android.app.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class LogList extends ListActivity{
	
	private LogDataHelper ldh;
	private SQLiteDatabase db;
	
	@Override
	public void onCreate(Bundle icile){
		super.onCreate(icile);
		setContentView(R.layout.list);
		
		
		readData();
	}
	
	private void readData(){
		
		// reading data
		
		ldh = new LogDataHelper(this);
		db = ldh.getReadableDatabase();
		String[] columns = new String[]{"id", "hrs", "mins", "category", "title"}; 
		Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
		
		ArrayList<String> logs = new ArrayList<String>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			int hrs = cursor.getInt(1);
			int mins = cursor.getInt(2);
			String category = cursor.getString(3);
			String title = cursor.getString(4);
			String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
			String mstr = mins >= 10 ? mins + "" : "0" + mins;
			String out = hstr + ":" + mstr + " - " + title + "[" + category + "]";
			logs.add(out);
		}
		
		// make list
		String[] l = new String[logs.size()];
		int index = 0;
		for(String s : logs){
			l[index++] = s;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, l);
		setListAdapter(adapter);
	}
}
