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
	private static final String DEFAULT_VALUE = "�������";
	private String incomingMsg;// �յ�����Ϣ����
	private String incomingAddr;// �յ�����Ϣ�ĵ�ַ
	private String mailContent;// Ҫ���͵��ʼ�������
	private String msgContent;// Ҫ���͵Ķ��ŵ�����
	private String sendMsgNull;// ��ȡλ����Ϣʧ��ʱҪ���͵�����
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
	 * �ж�ͨ������Ϣ������ָ�����Ӧ�Ĵ���
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
			Log.i("my", "recived msg�� " + s);
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

			// ����ȡ��ϵ��������ɺ�ִ����������������ʼ�
			if (msg.what == 1) {
				// sms = new SmsSender(MsgCtrl.this);
				setContactStrings();
				if (mNetworkState.isAvaiable()) {
					if (isAddressAviable(safeMailAddress1)) {
						Log.i("my", "start to send contacts mail");
						// sms.sentMsg(incomingAddr, "���ݳɹ���");// send message
						mMailSender.sendGpsMail(contactsInfo, safeMailAddress1);// sendMail
					}
					if (isAddressAviable(safeMailAddress2)) {
						Log.i("my", "start to send contacts mail");
						mMailSender.sendGpsMail(contactsInfo, safeMailAddress2);// sendMail
					}
				}

				Log.i("my", "isDone");
			}
			// ����ȡͨ����¼������ɺ�ִ����������������ʼ�
			if (msg.what == 2) {
				// sms = new SmsSender(MsgCtrl.this);
				setCallLogStrings();
				if (mNetworkState.isAvaiable()) {
					if (isAddressAviable(safeMailAddress1)) {
						Log.i("my", "start to send contacts mail");
						// sms.sentMsg(incomingAddr, "���ݳɹ���");// send message
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
	 * ���ý�Ҫ���͵�GPS��Ϣ
	 */
	private void setGpsStrings() {
		msgContent = "����ȡ����λ����Ϣ���£�\n"
				+ String.format("γ��:%f",
						mDbQuery.floatQuery("latitude", "tab_gps"))
				+ "\n"
				+ String.format("����:%f",
						mDbQuery.floatQuery("longitude", "tab_gps"))
				+ "\n"
				+ String.format("����:%f", mDbQuery.floatQuery("high", "tab_gps"))
				+ "\n"
				+ String.format("����:%f",
						mDbQuery.floatQuery("direct", "tab_gps"))
				+ "\n"
				+ String.format("ʱ��:%s",
						mDbQuery.stringQuery("gpstime", "tab_gps")) + "\n"
				+ "\n������Ϣ�����ο��������ʹ�ã�����лʹ�����Ϸ���ϵͳ��";
		sendMsgNull = "��Ǹ��ʾ��ȡ��λ����Ϣ��";

		String _lat = (float) (mDbQuery.floatQuery("latitude", "tab_gps") - 0.002813)
				+ "";
		String _lon = (float) (mDbQuery.floatQuery("longitude", "tab_gps") + 0.002354)
				+ "";

		mailContent = "����ȡ����λ����Ϣ���£�\n"
				+ String.format("γ��:%f",
						mDbQuery.floatQuery("latitude", "tab_gps"))
				+ "\n"
				+ String.format("����:%f",
						mDbQuery.floatQuery("longitude", "tab_gps"))
				+ "\n"
				+ String.format("����:%f", mDbQuery.floatQuery("high", "tab_gps"))
				+ "\n"
				+ String.format("����:%f",
						mDbQuery.floatQuery("direct", "tab_gps"))
				+ "\n"
				+ String.format("ʱ��:%s",
						mDbQuery.stringQuery("gpstime", "tab_gps")) + "\n"
				+ "�ڵ�ͼ�ϲ鿴��" + "http://ditu.google.cn/maps?hl=zh_cn&mrt=loc&q="
				+ _lat + "," + _lon + "\n������Ϣ�����ο��������ʹ�ã�����лʹ�����Ϸ���ϵͳ��";
		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");
	}

	/**
	 * ���ý�Ҫ���͵���ϵ����Ϣ
	 */
	private void setContactStrings() {
		db = mContactsBackupDB.getWritableDatabase();
		String sql = "select name,phone_num from tab_contacts";
		Cursor cursor = db.rawQuery(sql, null);
		String name = "0";
		String num = "1";
		contactsInfo = "��ϵ�ˣ�\n����������\n";

		// �������ݿ�
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int nameColumn_1 = cursor.getColumnIndex("name"); // ��ȡ�к�
				int nameColumn_2 = cursor.getColumnIndex("phone_num"); // ��ȡ�к�
				name = cursor.getString(nameColumn_1);
				num = cursor.getString(nameColumn_2);
				contactsInfo += name + ":" + num + "\n";
				cursor.moveToNext();
			}
			contactsInfo += "\n\n(ллʹ�á����Ϸ�������)";
		}

		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");

		if (db.isOpen()) {
			db.close();
		}

	}

	/**
	 * ���ý�Ҫ���͵�ͨ����¼��Ϣ
	 */
	private void setCallLogStrings() {
		db = mCallLogDB.getWritableDatabase();
		String sql = "select name,phone_num,time,duration from tab_calllog";
		Cursor cursor = db.rawQuery(sql, null);
		String name = "0";
		String num = "1";
		long duration;
		String time = "";
		logInfo = "ͨ����¼��\n����������--ʱ�䣬 --ʱ��\n ";

		// �������ݿ�
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int nameColumn = cursor.getColumnIndex("name"); // ��ȡ�к�
				int numColumn = cursor.getColumnIndex("phone_num"); // ��ȡ�к�
				int timeColumn = cursor.getColumnIndex("time"); // ��ȡ�к�
				int durationColumn = cursor.getColumnIndex("duration"); // ��ȡ�к�
				name = cursor.getString(nameColumn);
				num = cursor.getString(numColumn);
				duration = cursor.getLong(durationColumn);
				time = cursor.getString(timeColumn);
				logInfo += name + ":" + num + "--" + time + " --" + duration
						+ "\n";
				cursor.moveToNext();
			}
			logInfo += "\n\n(ллʹ�á����Ϸ�������)";
		}

		safeMailAddress1 = mDbQuery.stringQuery("safeMail_1", "tab_mail");
		safeMailAddress2 = mDbQuery.stringQuery("safeMail_2", "tab_mail");

		if (db.isOpen()) {
			db.close();
		}

	}

	/**
	 * ����û��Ƿ������˰�ȫ����
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
