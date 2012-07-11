package com.androidsafe.sms;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/*
 * 用于发送SMS短信息
 */
public class SmsSender {
	private Context context;
	private SmsManager sm = SmsManager.getDefault();

	public SmsSender(Context context) {
		this.context = context;
	}

	// 发送消息
	public void sentMsg(String addr, String msg) {
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
				new Intent(), 0);

		if (msg.length() > 70) {
			// 使用短信管理器进行短信内容的分段，返回分成的段
			ArrayList<String> contents = sm.divideMessage(msg);
			sm.sendMultipartTextMessage(addr, null, contents, null, null);
			/*
			 * for (String msg0 : contents) { // 使用短信管理器发送短信内容 // 参数一为短信接收者 //
			 * 参数三为短信内容
			 * 
			 * sm.sendTextMessage(addr, null, msg0, sentIntent, null); }
			 */
			// 否则一次过发送
		} else {
			sm.sendTextMessage(addr, null, msg, sentIntent, null);
		}
		Log.v("my", "Sent Successfully!" + msg);
	}

}
