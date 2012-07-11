package com.androidsafe.sms;

import java.util.regex.Pattern;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.androidsafe.db.DBQuery;
import com.androidsafe.gps.GpsMain;

public class SmsChangedService extends Service {
	private DBQuery mDbQuery;
	private long dateFlag = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		SmsContent content = new SmsContent(new Handler());
		// 注册短信变化监听
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, content);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void getSmsInPhone() {
		final String SMS_URI_INBOX = "content://sms/inbox";
		// StringBuilder smsBuilder = new StringBuilder();

		try {
			Uri uri = Uri.parse(SMS_URI_INBOX);
			String[] projection = new String[] { "address", "body", "read",
					"date" };
			Cursor cur = getContentResolver().query(uri, projection, null,
					null, "date desc"); // 获取手机内部短信

			if (cur.moveToFirst()) {
				int index_Address = cur.getColumnIndex("address");
				int index_Body = cur.getColumnIndex("body");
				// int index_Read = cur.getColumnIndex("read");
				int index_Date = cur.getColumnIndex("date");

				String strAddress = cur.getString(index_Address);
				String strbody = cur.getString(index_Body);
				// int intRead = cur.getInt(index_Read);
				long longDate = cur.getLong(index_Date);

				boolean b = false;
				if (dateFlag == 0) {
					dateFlag = longDate;
					b = true;
				} else if (dateFlag != longDate) {
					dateFlag = longDate;
					b = true;
				}
				if (b) {
					if (strbody.matches("^--androidsafe_reg.*")) {
						Pattern pattern = Pattern.compile(";");
						String[] strs = pattern.split(strbody);
						String cmd = strs[1];
						String pwd = strs[2];
						Log.i("my", "command:" + cmd);
						// Log.i("my", "password:" + pwd);
						if (isPwdRight(pwd)) {
							Log.i("my", "isPwdRight");
							startService(new Intent(SmsChangedService.this,
									GpsMain.class));
							Intent intent2 = new Intent(SmsChangedService.this,
									MsgCtrl.class);
							intent2.putExtra("sms", cmd);
							intent2.putExtra("incomingAddr", strAddress);
							this.startService(intent2);
						}
					}
					b = false;

				}
				// String strRead = "";
				// if (intRead == 0) {
				// strRead = "未读";
				// } else if (intRead == 1) {
				// strRead = "已读";
				// } else {
				// strRead = "null";
				// }
				//
				// smsBuilder.append("[ ");
				// smsBuilder.append(strAddress + ", ");
				// smsBuilder.append(strbody + ", ");
				// smsBuilder.append(strRead);
				// smsBuilder.append(" ]\n\n");

				if (!cur.isClosed()) {
					cur.close();
					cur = null;
				}
			} else {
				// smsBuilder.append("no result!");
			} // end if

			// smsBuilder.append("getSmsInPhone has executed!");

		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}

		// Log.i("my", "message:" + smsBuilder.toString());
	}

	/**
	 * 该类兼听本地短信的变化。当用户收到或发出短信时都会触发onChange（boolean selfChange）方法
	 * 
	 * @author anTa
	 * 
	 */
	class SmsContent extends ContentObserver {
		public SmsContent(Handler handler) {
			super(handler);
		}

		/**
		 * @Description 当短信表发送改变时，调用该方法 需要两种权限 android.permission.READ_SMS读取短信
		 *              android.permission.WRITE_SMS写短信
		 */
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getSmsInPhone();
		}
	}

	/**
	 * 判断密码正误
	 * 
	 * @param pwd
	 * @return 正确返回true；错误返回false
	 */
	private boolean isPwdRight(String pwd) {
		mDbQuery = new DBQuery(SmsChangedService.this);
		if (pwd.equals(mDbQuery.stringQuery("login_pwd", "tab_pwd"))) {
			return true;
		}
		return false;
	}

}
