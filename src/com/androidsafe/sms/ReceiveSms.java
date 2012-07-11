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
 * ������Ҫ�����յ���ÿһ�����š����������"--androidsafe_reg"��ͷ������ֹ�㲥���Զ������ݽ��з�����
 * 
 * @author anTa
 * 
 */
public class ReceiveSms extends BroadcastReceiver {
	private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";
	private String ctrlMsg;
	private String cmd; // ������Ҫִ�е�����
	private String pwd; // �����а�����������Ϣ
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
					// �յ��Ķ��ŵ�����
					ctrlMsg = currMsg.getDisplayMessageBody();
					// ���ź���
					incomingAddr = currMsg.getDisplayOriginatingAddress();
					// ���������������"--androidsafe_reg"��ͷ������ֹ�㲥���Զ������ݽ��з���
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
	 * �ж���������
	 * 
	 * @param pwd
	 * @return ��ȷ����true�����󷵻�false
	 */
	private boolean isPwdRight(String pwd) {
		if (pwd.equals(mDbQuery.stringQuery("login_pwd", "tab_pwd"))) {
			return true;
		}
		return false;
	}

}
