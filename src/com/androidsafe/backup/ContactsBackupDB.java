package com.androidsafe.backup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsBackupDB extends SQLiteOpenHelper {
	private static final String dbName = "contacts.db";
	private SQLiteDatabase db;

	public ContactsBackupDB(Context context) {
		super(context, dbName, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		sdb.execSQL("create table tab_contacts (name varchar(20),phone_num varchar(20));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
		sdb.execSQL("drop table if exists tab_contacts");
		onCreate(sdb);
	}

	// 添加联系人
	public boolean addPeople(String name, String phone_num) {
		boolean result = false;
		try {
			db = this.getWritableDatabase();
			String StrSql = String
					.format("insert into tab_contacts (name,phone_num) values ('%s','%s')",
							name, phone_num);
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
