package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidsafe.db.DBMain;
import com.androidsafe.gps.GpsMain;
import com.androidsafe.phone.PhoneNumber;
import com.androidsafe.sms.SmsChangedService;

public class AndroidSafeActivity extends Activity {
	/** Called when the activity is first created. */
	private Button bt_ctrl;
	private Button bt_login;
	private PhoneNumber mPhoneNumber = new PhoneNumber(this);
	private DBMain mDbMain = new DBMain(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		bt_ctrl = (Button) findViewById(R.id.bt_ctrl);
		bt_login = (Button) findViewById(R.id.bt_login);
		startService(new Intent(AndroidSafeActivity.this, SmsChangedService.class));
		startService(new Intent(AndroidSafeActivity.this, GpsMain.class));
		mPhoneNumber.getPhoneNumber();
		mPhoneNumber.getMygetDeviceId();
		mPhoneNumber.getMySimSerialNumber();
		mPhoneNumber.getMySubscriberId();
		mDbMain.updateSimInfo(mPhoneNumber.getMySimSerialNumber());

		bt_ctrl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AndroidSafeActivity.this,
						CtrlLogin.class));
				AndroidSafeActivity.this.finish();
			}
		});

		bt_login.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				SharedPreferences sharedata = getSharedPreferences(
						"isFirstUse", 0);
				String data = sharedata.getString("time", "0");
				Log.i("my", "data=" + data);
				if (data.equals("0")) {
					SharedPreferences.Editor userData = getSharedPreferences(
							"isFirstUse", 0).edit();
					userData.putString("time", "0");
					userData.commit();
					startActivity(new Intent(AndroidSafeActivity.this,
							SetNewPwd.class));
				} else {
					startActivity(new Intent(AndroidSafeActivity.this,
							Login.class));
					AndroidSafeActivity.this.finish();
				}
			}
		});

		View bt_login = findViewById(R.id.bt_login);// 找到你要设透明背景的layout 的id
		bt_login.getBackground().setAlpha(160);// 0~255透明度值
		View bt_ctrl = findViewById(R.id.bt_ctrl);// 找到你要设透明背景的layout 的id
		bt_ctrl.getBackground().setAlpha(160);// 0~255透明度值
	}
}