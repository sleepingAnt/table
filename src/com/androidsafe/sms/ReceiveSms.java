package com.androidsafe.sms;

import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.androidsafe.db.DBQuery;
import com.androidsafe.gps.GpsMain;
import com.androidsafe.phone.SmsRecevier;

/**
 * 此类主要拦截收到的每一条短信。如果短信以"--androidsafe_reg"开头，就中止广播，对短信内容进行分析。
 * 
 * @author anTa
 * 
 */
public class ReceiveSms extends BroadcastReceiver {
	private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";
	private String ctrlMsg;
	private String cmd; // 短信想要执行的命令
	private String pwd; // 短信中包含的密码信息
	private String incomingAddr;
	private DBQuery mDbQuery;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.v("MyBrocast.onReceive", "testtttttttttttt");
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                Intent service=new Intent(context, SmsRecevier.class);
                context.startService(service);
        }
		
		mDbQuery = new DBQuery(context);
		if (intent.getAction().equals(strACT)) {
			// StringBuilder sb = new StringBuilder();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] msg = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage currMsg : msg) {
					/*
					 * sb.append("From:");
					 * sb.append(currMsg.getDisplayOriginatingAddress());
					 * sb.append("\nMessage:");
					 * sb.append(currMsg.getDisplayMessageBody());
					 */
					// 收到的短信的内容
					ctrlMsg = currMsg.getDisplayMessageBody();
					// 来信号码
					incomingAddr = currMsg.getDisplayOriginatingAddress();
					// 如果短信内容是以"--androidsafe_reg"开头，就中止广播，对短信内容进行分析
					if (ctrlMsg.matches("^--androidsafe_reg.*")) {
						this.abortBroadcast();
						Pattern pattern = Pattern.compile(";");
						String[] strs = pattern.split(ctrlMsg);
						cmd = strs[1];
						pwd = strs[2];
						if (isPwdRight(pwd)) {
							context.startService(new Intent(context,
									GpsMain.class));
							Intent intent2 = new Intent(context, MsgCtrl.class);
							intent2.putExtra("sms", cmd);
							intent2.putExtra("incomingAddr", incomingAddr);
							context.startService(intent2);
							Log.v("my",
									"sms_number.equals("
											+ currMsg
													.getDisplayOriginatingAddress()
											+ ")");
						}
					}

				}

			}
		}

	}

	/**
	 * 判断密码正误
	 * 
	 * @param pwd
	 * @return 正确返回true；错误返回false
	 */
	private boolean isPwdRight(String pwd) {
		if (pwd.equals(mDbQuery.stringQuery("login_pwd", "tab_pwd"))) {
			return true;
		}
		return false;
	}

}
