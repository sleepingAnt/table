package com.androidsafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBQuery {
	public Context context;
	public DBMain mDBMain;
	public SQLiteDatabase db;

	public DBQuery(Context context) {

		this.context = context;
	}

	public String stringQuery(String s, String table) {
		mDBMain = new DBMain(this.context);
		db = mDBMain.getWritableDatabase();
		String sql = "select " + s + " from " + table;
		Cursor cursor = db.rawQuery(sql, null);
		String result = "0";
		if (cursor.getCount() > 0) {
			cursor.moveToLast(); // 将指针移动到最后一条记录的位置
			int nameColumn = cursor.getColumnIndex(s); // 获取行号
			result = cursor.getString(nameColumn); // 获取行所对应的数据
			// Log.i("my", "success" + result);
		}
		if (db.isOpen()) {
			db.close();
		}
		return result;
	}

	public double doubleQuery(String s, String table) {
		mDBMain = new DBMain(this.context);
		db = mDBMain.getWritableDatabase();
		String sql = "select " + s + " from " + table;
		Cursor cursor = db.rawQuery(sql, null);
		double result = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToLast(); // 将指针移动到最后一条记录的位置
			int nameColumn = cursor.getColumnIndex(s); // 获取行号
			result = cursor.getDouble(nameColumn); // 获取行所对应的数据
			// Log.i("my", "success" + result);
		}
		if (db.isOpen()) {
			db.close();
		}
		return result;
	}

	public float floatQuery(String s, String table) {
		mDBMain = new DBMain(this.context);
		db = mDBMain.getWritableDatabase();
		String sql = "select " + s + " from " + table;
		Cursor cursor = db.rawQuery(sql, null);
		float result = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToLast(); // 将指针移动到最后一条记录的位置
			int nameColumn = cursor.getColumnIndex(s); // 获取行号
			result = cursor.getFloat(nameColumn); // 获取行所对应的数据
			// Log.i("my", "success" + result);
		}
		if (db.isOpen()) {
			db.close();
		}
		return result;
	}

	public int intQuery(String s, String table) {
		mDBMain = new DBMain(this.context);
		db = mDBMain.getWritableDatabase();
		String sql = "select " + s + " from " + table;
		Cursor cursor = db.rawQuery(sql, null);
		int result = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToLast(); // 将指针移动到最后一条记录的位置
			int nameColumn = cursor.getColumnIndex(s); // 获取行号
			result = cursor.getInt(nameColumn); // 获取行所对应的数据
			// Log.i("my", "success" + result);
		}
		if (db.isOpen()) {
			db.close();
		}
		return result;
	}
}
