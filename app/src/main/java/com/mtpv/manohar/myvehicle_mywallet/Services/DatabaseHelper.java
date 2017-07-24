package com.mtpv.manohar.myvehicle_mywallet.Services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DatabaseAdapter.DATABASE_CREATE);
	}
	
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//Logw("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		//db.execSQL("DROP TABLE IF EXISTS" + "TEMPLATE");
		//onCreate(db);
		
		Log.w("TaskDBAdapter", "Upgrading from version" +oldVersion+ "to" +newVersion+ "which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + "TEMPLATE");
		onCreate(db);
		
	}

}