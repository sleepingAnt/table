package com.androidsafe.backup;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CallLogDB extends SQLiteOpenHelper {
	private static final String dbName = "calllog.db";
	private SQLiteDatabase db;

	public CallLogDB(Context context) {
		super(context, dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		sdb.execSQL("create table tab_calllog (name varchar(20),phone_num varchar(20),time date,duration int);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists tab_calllog");
		onCreate(db);
	}

	// 添加记录
	public boolean addCallLog(String name, String phone_num,String time,int duration) {
		boolean result = false;
		try {
			db = this.getWritableDatabase();
			String StrSql = String
					.format("insert into tab_calllog (name,phone_num,time,duration) values ('%s','%s','%s','%d')",
							name, phone_num,time,duration);
			db.execSQL(StrSql);
			result = true;
			closeDB();
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

	// 关闭数据库
	public void closeDB() {
		if (db.isOpen()) {
			db.close();
		}
	}

}
