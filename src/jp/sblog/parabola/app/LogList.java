package jp.sblog.parabola.app;

import android.app.*;
import android.app.LauncherActivity.ListItem;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		
		/*
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String i = (String) lv.getItemAtPosition(position);
				ArrayAdapter<String> adapter = (ArrayAdapter<String>)lv.getAdapter();
				adapter.remove(i);
				return true;
			}
			
		});
		*/
		/*
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View item,
					int position, long id) {
				String i = (String) lv.getItemAtPosition(position);
				Log.d("LOGLOG", i);
				
				String[] columns = new String[]{"id", "hrs", "mins", "category", "title"}; 
				Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
				ArrayList<String> logs = new ArrayList<String>();
				int did = 0;
				while(cursor.moveToNext()){
					int ids = cursor.getInt(0);
					int hrs = cursor.getInt(1);
					int mins = cursor.getInt(2);
					String category = cursor.getString(3);
					String title = cursor.getString(4);
					String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
					String mstr = mins >= 10 ? mins + "" : "0" + mins;
					String out = hstr + ":" + mstr + " - " + title + "[" + category + "]";
					Log.d("LOGLOG", out);
					if( out.equals(i) ){
						did = ids;
						Log.d("LOGLOG", "equals");
						continue;
					}
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
				return true;
			}
		});
		*/
		readData();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		/*
		super.onListItemClick(l, v, position, id);
		int logid = idList.get(position);
		
		String[] columns = new String[]{"id", "hrs", "mins", "category", "title"}; 
		Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
		while(cursor.moveToNext()){
			int ids = cursor.getInt(0);
			if( ids == logid ){
				int hrs = cursor.getInt(1);
				int mins = cursor.getInt(2);
				String category = cursor.getString(3);
				String title = cursor.getString(4);
				String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
				String mstr = mins >= 10 ? mins + "" : "0" + mins;
				String out = hstr + ":" + mstr + " - " + title + "[" + category + "]";
				Toast.makeText(this, out, Toast.LENGTH_LONG).show();
				break;
			}
		}
		*/
	}
	
	private void readData(){
		
		// reading data
		idList = new ArrayList<Integer>();
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
			Log.d("LOGLOG", i);
			
			String[] columns = new String[]{"id", "hrs", "mins", "category", "title"}; 
			Cursor cursor = db.query("logdata", columns, null, null, null, null, "id");
			ArrayList<String> logs = new ArrayList<String>();
			did = 0;
			while(cursor.moveToNext()){
				int ids = cursor.getInt(0);
				int hrs = cursor.getInt(1);
				int mins = cursor.getInt(2);
				String category = cursor.getString(3);
				String title = cursor.getString(4);
				String hstr = hrs >= 10 ? hrs + "" : "0" + hrs;
				String mstr = mins >= 10 ? mins + "" : "0" + mins;
				String out = hstr + ":" + mstr + " - " + title + "[" + category + "]";
				Log.d("LOGLOG", out);
				if( out.equals(i) ){
					did = ids;
					Log.d("LOGLOG", "equals");
					continue;
				}
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
