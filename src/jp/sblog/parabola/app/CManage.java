package jp.sblog.parabola.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class CManage extends Activity {

	private ListView lv = null;
	private LogDataHelper ldh = null;
	private SQLiteDatabase db = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		// init
		lv = (ListView)findViewById(R.id.list);
		ldh = new LogDataHelper(this);
		db = ldh.getReadableDatabase();
		registerForContextMenu(lv);
		
		// database -> List ; category
		readData();
		
		// Event to Button
		Button b = (Button)findViewById(R.id.ok_button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText t = (EditText)findViewById(R.id.cname);
				String text = t.getText().toString();
				
				if(!isExist(text)){
					ContentValues values = new ContentValues();
					values.put("cname", text);
					db.insert("category", null, values);
					// read
					readData();
					// reset
					t.setText("");
				} else {
					Toast.makeText(CManage.this, "Duplication of Category name", Toast.LENGTH_SHORT).show();
					t.setText("");
				}
			}
		});
	}
	
	private boolean isExist(String text){
		
		Cursor cursor = db.query("category", new String[]{"id", "cname"}, null, null, null, null, "id");
		while(cursor.moveToNext()){
			if(cursor.getString(1).equals(text))
				return true;
		}
		return false;
	}
	
	
	private void readData(){
		// reading data
		// LogDataHelper ldh = new LogDataHelper(this);
		// SQLiteDatabase db = ldh.getReadableDatabase();
		String[] columns = new String[]{"id", "cname"}; 
		Cursor cursor = db.query("category", columns, null, null, null, null, "id");
		
		ArrayList<String> logs = new ArrayList<String>();
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			String category = cursor.getString(1);
			Log.d("CManage", "--" + category);
			logs.add(category);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		for( String s : logs )
			adapter.add(s);
		lv.setAdapter(adapter);
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
			String i = (String) lv.getItemAtPosition(info.position);
			
			String[] columns = new String[]{"id", "cname"}; 
			Cursor cursor = db.query("category", columns, null, null, null, null, "id");
			ArrayList<String> logs = new ArrayList<String>();

			while(cursor.moveToNext()){
				int ids = cursor.getInt(0);
				String cn = cursor.getString(1);
				if( cn.equals(i) ){
					// delate target
					db.delete("category", "id="+ids, null);
					break;
				}
			}
			readData();
		}
		return super.onContextItemSelected(item);
	}
	
}