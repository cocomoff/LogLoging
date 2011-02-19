package jp.sblog.parabola.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogDataHelper extends SQLiteOpenHelper{

	public LogDataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String DATABASE_NAME = "log";
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_TABLE_SQL = 
		"create table logdata (" +
		"id integer primary key autoincrement," +
		"hrs integer not null," +
		"mins integer not null," +
		"category text not null," +
		"title text not null)";
	private static final String DROP_TABLE_SQL = 
		"drop table if exists logdata";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oV, int nV) {
		if(nV > oV){
			db.execSQL(DROP_TABLE_SQL);
			onCreate(db);
		}
	}

	
}
