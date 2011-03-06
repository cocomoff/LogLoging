package jp.sblog.parabola.app;

import android.app.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LogList extends ListActivity{
	// idlist
	ArrayList<Integer> idList;
	ListView lv;
	
	// database
	private LogDataHelper ldh;
	private SQLiteDatabase db;
	
	@Override
	public void onCreate(Bundle icile){
		super.onCreate(icile);
		setContentView(R.layout.list);
		idList = new ArrayList<Integer>();
		lv = getListView();
		registerForContextMenu(getListView());
		readData();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
	}
	
	private void readData(){	
		// reading data
		idList = new ArrayList<Integer>();
		ldh = new LogDataHelper(this);
		db = ldh.getReadableDatabase();
		String[] columns = new String[]{"id", "year", "month", "day", "hrs", "mins", "category", "title"}; 
		Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
		
		ArrayList<String> logs = new ArrayList<String>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			int year = cursor.getInt(1);
			int month = cursor.getInt(2);
			int day = cursor.getInt(3);
			int hrs = cursor.getInt(4);
			int mins = cursor.getInt(5);
			String category = cursor.getString(6);
			String title = cursor.getString(7);
			String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
			String mstr = mins >= 10 ? mins + "" : "0" + mins;
			String date = year + "/" + month + "/" + day;
			String out = hstr + ":" + mstr + " - " + title + "[" + category + "] - " + date ;
			idList.add(id);
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
	
	// long click
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 30, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 30:
			int did = idList.get(info.position);
			String i = (String) lv.getItemAtPosition(info.position);
			String[] columns = new String[]{"id", "year", "month", "day", "hrs", "mins", "category", "title"}; 
			Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
			ArrayList<String> logs = new ArrayList<String>();
			did = 0;
			while(cursor.moveToNext()){
				int ids = cursor.getInt(0);
				int year = cursor.getInt(1);
				int month = cursor.getInt(2);
				int day = cursor.getInt(3);
				int hrs = cursor.getInt(4);
				int mins = cursor.getInt(5);
				String category = cursor.getString(6);
				String title = cursor.getString(7);
				String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
				String mstr = mins >= 10 ? mins + "" : "0" + mins; 
				String out = hstr + ":" + mstr + " - " + title + "[" + category + "]";
				if( out.equals(i) )
					did = ids;
				logs.add(out);
			}
			
			// delete 
			db.delete("logdata", "id="+did, null);
			
			// make list
			String[] l = new String[logs.size()];
			int index = 0;
			for(String s : logs){
				l[index++] = s;
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, l);
			setListAdapter(adapter);
			Log.d("LOGLOG", "after");
			break;
		}
		return super.onContextItemSelected(item);
	}
}
