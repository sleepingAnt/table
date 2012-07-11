package com.androidsafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMain extends SQLiteOpenHelper {
	private static final String dbName = "mainData.db";
	private SQLiteDatabase db;

	public DBMain(Context context) {
		super(context, dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sdb) {
		// 建表
		sdb.execSQL("create table tab_gps (state integer,latitude float,longitude float,high double,direct double,speed double,gpstime date);");
		sdb.execSQL("create table tab_sms (safeNum_1 varchar(20),safeNum_2 varchar(20));");
		sdb.execSQL("create table tab_mail (safeMail_1 varchar(50),safeMail_2 varchar(50));");
		sdb.execSQL("create table tab_ctrlnum (num varchar(20),pwd varchar(50));");
		sdb.execSQL("create table tab_pwd (login_pwd varchar(20));");
		sdb.execSQL("create table tab_sim_info (sim_serial_number varchar(20));");

		// 初始化tab_sms和tab_mail表
		String smsStrSql = String.format(
				"insert into tab_sms (safeNum_1,safeNum_2) values ('%s','%s')",
				"点此设置", "点此设置");
		sdb.execSQL(smsStrSql);
		String mailStrSql = String
				.format("insert into tab_mail (safeMail_1,safeMail_2) values ('%s','%s')",
						"点此设置", "点此设置");
		sdb.execSQL(mailStrSql);
		String pwdStrSql = "insert into tab_pwd (login_pwd) values ('aaaa')";
		sdb.execSQL(pwdStrSql);
		String simStrSql = "insert into tab_sim_info (sim_serial_number) values ('0')";
		sdb.execSQL(simStrSql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
		sdb.execSQL("drop table if exists tab_gps");
		sdb.execSQL("drop table if exists tab_sms");
		sdb.execSQL("drop table if exists tab_mail");
		onCreate(sdb);
	}

	public void closeDB() {
		if (db.isOpen()) {
			db.close();
		}
	}

	// set password
	public boolean setPassword(String pwd) {

		boolean result = false;
		try {
			db = this.getWritableDatabase();
			String strSql = String.format("update tab_pwd set login_pwd='%s'",
					pwd);
			db.execSQL(strSql);
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

	// 写入GPS数据
	public boolean addGpsData(GpsData mGpsData) {

		boolean result = true;
		try {
			db = this.getWritableDatabase();
			String StrSql = String
					.format("insert into tab_gps (state,latitude,longitude,high,direct,speed,gpstime) values (%d,%f,%f,%.1f,%.1f,%.1f,'%s')",
							mGpsData.state, mGpsData.latitude,
							mGpsData.longitude, mGpsData.high, mGpsData.direct,
							mGpsData.speed, mGpsData.gpsTime);
			db.execSQL(StrSql);
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

	// 写入sim相关数据
	public boolean updateSimInfo(String s) {
		boolean result = true;
		try {
			db = this.getWritableDatabase();
			String strSql;
			strSql = String.format(
					"update tab_sim_info set sim_serial_number='%s'", s);
			db.execSQL(strSql);
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// 写入SMS相关数据
	public boolean updateSmsData(SmsData mSmsData, int num) {
		boolean result = true;
		try {
			db = this.getWritableDatabase();
			String strSql;
			if (num == 1) {
				strSql = String.format("update tab_sms set safeNum_1='%s'",
						mSmsData.safeNum_1);
				db.execSQL(strSql);
			} else if (num == 2) {
				strSql = String.format("update tab_sms set safeNum_2='%s'",
						mSmsData.safeNum_2);
				db.execSQL(strSql);
			}
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

	// 写入email相关数据
	public boolean updateMailData(MailData mMailData, int num) {
		boolean result = true;
		try {
			db = this.getWritableDatabase();
			String strSql;
			if (num == 1) {
				strSql = String.format("update tab_mail set safeMail_1='%s'",
						mMailData.safeMail_1);
				db.execSQL(strSql);
			} else if (num == 2) {
				strSql = String.format("update tab_mail set safeMail_2='%s'",
						mMailData.safeMail_2);
				db.execSQL(strSql);
			}
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

	public boolean addCtrlNum(DBCtrlNum mCtrlNum) {
		boolean result = true;
		try {
			db = this.getWritableDatabase();
			String StrSql = String.format(
					"insert into tab_ctrlnum (num,pwd) values ('%s','%s')",
					mCtrlNum.num, mCtrlNum.pwd);
			db.execSQL(StrSql);
			closeDB();
			result = true;
		} catch (Exception e) {
			result = false;

		}
		return result;
	}

}
