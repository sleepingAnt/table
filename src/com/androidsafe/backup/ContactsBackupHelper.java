package com.androidsafe.backup;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.androidsafe.sms.MsgCtrl;

/**
 * This class is used to backup Contacts or sms and delete Contacts or sms.
 * 
 * @author anTa
 * 
 */
public class ContactsBackupHelper {
	private Context context;

	public ContactsBackupHelper(Context context) {
		this.context = context;
	}

	/**
	 * Add all of the Contacts to SQLiteDB.
	 * 
	 * @param activity
	 *            The activity with a Handler
	 */
	public void writePeople(final MsgCtrl activity) {
		new Thread(new Runnable() {

			public void run() {
				Message msg;
				ContactsBackupDB mContactsBackupDB = new ContactsBackupDB(
						context);
				SQLiteDatabase db = mContactsBackupDB.getWritableDatabase();
				mContactsBackupDB.onUpgrade(db, 1, 1);
				ContentResolver cr = context.getContentResolver();
				Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
						null, null, null, null);
				while (cursor.moveToNext()) {
					// 取得联系人名字
					int nameFieldColumnIndex = cursor
							.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String name = cursor.getString(nameFieldColumnIndex);
					// 取得联系人ID
					String contactId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));
					Cursor phone = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);

					// 取得电话号码(可能存在多个号码)
					while (phone.moveToNext()) {
						String strPhoneNumber = phone.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						mContactsBackupDB.addPeople(name, strPhoneNumber);
					}
					phone.close();
				}
				cursor.close();
				msg = new Message();
				msg.what = 1;
				activity.mHandler.sendMessage(msg);
			}
		}).start();

	}

	public void writeCallLog(final MsgCtrl activity) {
		Message msg;
		CallLogDB mCallLogDB = new CallLogDB(context);
		SQLiteDatabase db = mCallLogDB.getWritableDatabase();
		mCallLogDB.onUpgrade(db, 1, 1);
		String name = "";
		String phone_num = "";
		int duration;
		Date date;
		String time = "";
		ContentResolver cr = context.getContentResolver();
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			phone_num = cursor.getString(0);
			name = cursor.getString(1);
			duration = cursor.getInt(4);
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = new Date(Long.parseLong(cursor.getString(3)));
			time = sfd.format(date);
			mCallLogDB.addCallLog(name, phone_num, time, duration);
		}
		msg = new Message();
		msg.what = 2;
		activity.mHandler.sendMessage(msg);

	}

	/**
	 * Delete all Contacts
	 */
	public void clearContacts() {
		context.getContentResolver().delete(
				Uri.parse(ContactsContract.RawContacts.CONTENT_URI.toString()
						+ "?" + ContactsContract.CALLER_IS_SYNCADAPTER
						+ " = true"),
				ContactsContract.RawContacts._ID + " > 0", null);
	}

	/**
	 * Delete all SMS one by one
	 */
	public void clearSMS() {
		try {
			ContentResolver CR = context.getContentResolver();
			// Query SMS
			Uri uriSms = Uri.parse("content://sms/inbox");
			Cursor c = CR.query(uriSms, new String[] { "_id", "thread_id" },
					null, null, null);
			if (null != c && c.moveToFirst()) {
				do {
					// Delete SMS
					long threadId = c.getLong(1);
					CR.delete(Uri.parse("content://sms/conversations/"
							+ threadId), null, null);
					Log.d("deleteSMS", "threadId:: " + threadId);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			Log.d("deleteSMS", "Exception:: " + e);
		}
	}

}
