package com.androidsafe.phone;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneNumber {
	private Context context;

	public PhoneNumber(Context context) {
		this.context = context;
	}

	public String getPhoneNumber() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getLine1Number();
		Log.i("my","getPhoneNumber: "+phoneId);
		return phoneId;
	}

	public String getMySimSerialNumber() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getSimSerialNumber();
		Log.i("my","getMySimSerialNumber: "+phoneId);
		return phoneId;
	}

	public String getMySubscriberId() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getSubscriberId();
		Log.i("my","getMySubscriberId: "+phoneId);
		return phoneId;

	}
	
	public String getMygetDeviceId() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getDeviceId();
		Log.i("my","getMygetDeviceId: "+phoneId);
		return phoneId;

	}
}
