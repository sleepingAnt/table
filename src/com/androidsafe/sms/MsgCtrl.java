package com.androidsafe.sms;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.androidsafe.backup.CallLogDB;
import com.androidsafe.backup.ContactsBackupDB;
import com.androidsafe.backup.ContactsBackupHelper;
import com.androidsafe.db.DBQuery;
import com.androidsafe.mail.MailSender;
import com.androidsafe.phone.NetworkState;

public class MsgCtrl extends Service {
	private static final String DEFAULT_VALUE = "点此设置";
	private String incomingMsg;// 收到的信息内容
	private String incomingAddr;// 收到的信息的地址
	private String mailContent;// 要发送的邮件的内容
	private String msgContent;// 要发送的短信的内容
	private String sendMsgNull;// 获取位置信息失败时要发送的内容
	private String safeMailAddress1;
	private String safeMailAddress2;
	private String contactsInfo;
	private String logInfo;
	private SmsSender sms;
	private DBQuery mDbQuery = new DBQuery(this);
	private MailSender mMailSender = new MailSender();
	private NetworkState mNetworkState = new NetworkState(this);
	private CallLogDB mCallLogDB = new CallLogDB(this);
	private ContactsBackupHelper mContactsBackupHelper = new ContactsBackupHelper(
			this);
	private ContactsBackupDB mContactsBackupDB = new ContactsBackupDB(this);
	private SQLiteDatabase db;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("my", "onCreate MsgCtrl.class Successfully!");

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		incomingMsg = intent.getStringExtra("sms");
		incomingAddr = intent.getStringExtra("incomingAddr");

		Log.v("my", "Message from" + incomingAddr);
		Log.v("my", "Message text :" + incomingMsg);

		analysisMsg(incomingMsg);
		super.onStart(intent, startId);
	}

	/**
	 * 判断通过短信息发来的指令并作相应的处理
	 * 
	 * @param s
	 */
	private void analysisMsg(String s) {
		boolean flags_filter = false;
		if (s.equals("gps")) {
			setGpsStrings();
			sms = new SmsSender(this);
			try {
				Log.v("my", "start sleep 5s ....");
				Thread.sleep(5000);
				Log.v("my", "sleep over, start sending ....");
				if (mDbQuery.intQuery("latitude", "tab_gps") == 0) {
					sms.sentMsg(incomingAddr, sendMsgNull);
					mMailSender.sendGpsMail(sendMsgNull, "526417159@qq.com");
				} else {

					sms.sentMsg(incomingAddr, msgContent);// send message
					Log.v("my", "start sleep 20s ....");
					Thread.sleep(10 * 1000);
					if (mNetworkState.isAvaiable()) {
						if (isAddressAviable(safeMailAddress1)) {
							Log.i("my", "start to send mail");
							mMailSender.sendGpsMail(mailContent,
									safeMailAddress1);// sendMail
						}
						if (isAddressAviable(safeMailAddress2)) {
							Log.i("my", "start to send mail");
							mMailSender.sendGpsMail(mailContent,
									safeMailAddress2);// sendMail
						}
					}
					MsgCtrl.this.onDestroy();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.onDestroy();
		}

		if (s.equals("backup")) {
			mContactsBackupHelper.writePeople(MsgCtrl.this);
			mContactsBackupHelper.writeCallLog(MsgCtrl.this);
			this.onDestroy();
		}

		if (s.equals("delete")) {
			Log.i("my", "recived msg： " + s);
			// mContactsBackupHelper.clearSMS();
			mContactsBackupHelper.clearContacts();
			this.onDestroy();
		}

		if (flags_filter) {

		}
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// 当读取联系人数据完成后执行下面操作，发送邮件
			if (msg.what == 1) {
				// sms = new SmsSender(MsgCtrl.this);
				setContactStrings();
				if (mNetworkState.isAvaiable()) {
					if (isAddressAviable(safeMailAddress1)) {
						Log.i("my", "start to send contacts mail");
						// sms.sentMsg(incomingAddr, "备份成功！");// send message
						mMailSender.sendGpsMail(contactsInfo, safeMailAddress1);// sendMail
					}
					if (isAddressAviable(safeMailAddress2)) {
						Log.i("my", "start to send contacts mail");
						mMailSender.sendGpsMail(contactsInfo, safeMailAddress2);// sendMail
					}
				}

				Log.i("my", "isDone");
			}
			// 当读取通话记录数据完成后执行下面操作，发送邮件
			if (msg.what == 2) {
				// sms = new SmsSender(MsgCtrl.this);
				setCallLogStrings();
				if (mNetworkState.isAvaiable()) {
					if (isAddressAviable(safeMailAddress1)) {
						Log.i("my", "start to send contacts mail");
						// sms.sentMsg(incomingAddr, "备份成功！");// send message
						mMailSender.sendGpsMail(logInfo, safeMailAddress1);// sendMail
					}
					if (isAddressAviable(safeMailAddress2)) {
						Log.i("my", "start to send contacts mail");
						mMailSender.sendGpsMail(logInfo, safeMailAddress2);// sendMail
					}
				}

				Log.i("my", "isDone");
			}
		}

	};

	/**
	 * 设置将要发送的GPS信息
	 */
	private void setGpsStrings() {
		msgContent = "最后获取到的位置信息如下：\n"
				+ String.format("纬度:%f",
						mDbQuery.floatQuery("latitude", "tab_gps"))
				+ "\n"
				+ String.format("经度:%f",
						mDbQuery.floatQuery("longitude", "tab_gps"))
				+ "\n"
				+ String.format("海拔:%f", mDbQuery.floatQuery("high", "tab_gps"))
				+ "\n"
				+ String.format("方向:%f",
						mDbQuery.floatQuery("direct", "tab_gps"))
				+ "\n"
				+ String.format("时间:%s",
						mDbQuery.stringQuery("gpstime", "tab_gps")) + "\n"
				+ "\n以上信息仅供参考，请谨慎使用！（感谢使用蚂蚁防盗系统）";
		sendMsgNull = "抱歉，示获取到位置信息！";

		String _lat = (float) (mDbQuery.floatQuery("latitude", "tab_gps") - 0.002813)
				+ "";
		String _lon = (float) (mDbQuery.floatQuery("longitude", "tab_gps") + 0.002354)
				+ "";

		mailContent = "最后获取到的位置信息如下：\n"
				+ String.format("纬度:%f",
						mDbQuery.floatQuery("latitude", "tab_gps"))
				+ "\n"
				+ String.format("经度:%f",
						mDbQuery.floatQuery("longitude", "tab_gps"))
				+ "\n"
				+ String.format("海拔:%f", mDbQuery.floatQuery("high", "tab_gps"))
				+ "\n"
				+ String.format("方向:%f",
						mDbQuery.floatQuery("direct", "tab_gps"))
				+ "\n"
				+ String.format("时间:%s",
						mDbQuery.stringQuery("gpstime", "tab_gps")) + "\n"
				+ "在地图上查看：" + "http://ditu.google.cn/maps?hl=zh_cn&mrt=loc&q="
				+ _lat + "," + _lon + "\n以上信息仅供参考，请谨慎使用！（感谢使用蚂蚁防盗系统）";
		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");
	}

	/**
	 * 设置将要发送的联系人信息
	 */
	private void setContactStrings() {
		db = mContactsBackupDB.getWritableDatabase();
		String sql = "select name,phone_num from tab_contacts";
		Cursor cursor = db.rawQuery(sql, null);
		String name = "0";
		String num = "1";
		contactsInfo = "联系人：\n姓名：号码\n";

		// 遍历数据库
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int nameColumn_1 = cursor.getColumnIndex("name"); // 获取列号
				int nameColumn_2 = cursor.getColumnIndex("phone_num"); // 获取列号
				name = cursor.getString(nameColumn_1);
				num = cursor.getString(nameColumn_2);
				contactsInfo += name + ":" + num + "\n";
				cursor.moveToNext();
			}
			contactsInfo += "\n\n(谢谢使用【蚂蚁防盗】！)";
		}

		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");

		if (db.isOpen()) {
			db.close();
		}

	}

	/**
	 * 设置将要发送的通话记录信息
	 */
	private void setCallLogStrings() {
		db = mCallLogDB.getWritableDatabase();
		String sql = "select name,phone_num,time,duration from tab_calllog";
		Cursor cursor = db.rawQuery(sql, null);
		String name = "0";
		String num = "1";
		long duration;
		String time = "";
		logInfo = "通话记录：\n姓名：号码--时间， --时长\n ";

		// 遍历数据库
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int nameColumn = cursor.getColumnIndex("name"); // 获取列号
				int numColumn = cursor.getColumnIndex("phone_num"); // 获取列号
				int timeColumn = cursor.getColumnIndex("time"); // 获取列号
				int durationColumn = cursor.getColumnIndex("duration"); // 获取列号
				name = cursor.getString(nameColumn);
				num = cursor.getString(numColumn);
				duration = cursor.getLong(durationColumn);
				time = cursor.getString(timeColumn);
				logInfo += name + ":" + num + "--" + time + " --" + duration
						+ "\n";
				cursor.moveToNext();
			}
			logInfo += "\n\n(谢谢使用【蚂蚁防盗】！)";
		}

		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");

		if (db.isOpen()) {
			db.close();
		}

	}

	/**
	 * 检测用户是否设置了安全邮箱
	 * 
	 * @param s
	 * @return
	 */
	private boolean isAddressAviable(String s) {
		boolean flag = false;
		if (!s.equals(DEFAULT_VALUE)) {
			flag = true;
		}
		return flag;
	}

}
