package com.androidsafe.sms;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/*
 * ���ڷ���SMS����Ϣ
 */
public class SmsSender {
	private Context context;
	private SmsManager sm = SmsManager.getDefault();

	public SmsSender(Context context) {
		this.context = context;
	}

	// ������Ϣ
	public void sentMsg(String addr, String msg) {
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
				new Intent(), 0);

		if (msg.length() > 70) {
			// ʹ�ö��Ź��������ж������ݵķֶΣ����طֳɵĶ�
			ArrayList<String> contents = sm.divideMessage(msg);
			sm.sendMultipartTextMessage(addr, null, contents, null, null);
			/*
			 * for (String msg0 : contents) { // ʹ�ö��Ź��������Ͷ������� // ����һΪ���Ž����� //
			 * ������Ϊ��������
			 * 
			 * sm.sendTextMessage(addr, null, msg0, sentIntent, null); }
			 */
			// ����һ�ι�����
		} else {
			sm.sendTextMessage(addr, null, msg, sentIntent, null);
		}
		Log.v("my", "Sent Successfully!" + msg);
	}

}
