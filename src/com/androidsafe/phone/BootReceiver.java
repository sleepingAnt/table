package com.androidsafe.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.androidsafe.db.DBMain;
import com.androidsafe.db.DBQuery;
import com.androidsafe.sms.SmsChangedService;
import com.androidsafe.sms.SmsSender;

public class BootReceiver extends BroadcastReceiver {
	private static final String DEFAULT_VALUE = "点此设置";
	private PhoneNumber mPhoneNumber;
	private String simSerialNum;
	private DBMain mDbMain;
	private DBQuery mDbQuery;
	private SmsSender mSmsSender;

	@Override
	public void onReceive(Context context, Intent intent) {

		// Log.v("MyBrocast.onReceive", "testtttttttttttt");
		// if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
		// Intent service=new Intent(context, SmsRecevier.class);
		// context.startService(service);
		// }

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// Intent bootActivityIntent = new Intent(context,
			// AndroidSafeActivity.class);
			// bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(bootActivityIntent);

			context.startService(new Intent(context, SmsChangedService.class));

			mPhoneNumber = new PhoneNumber(context);
			mDbMain = new DBMain(context);
			mDbQuery = new DBQuery(context);
			mSmsSender = new SmsSender(context);
			String safeNum_1 = mDbQuery.stringQuery("safeNum_1", "tab_sms");
			String safeNum_2 = mDbQuery.stringQuery("safeNum_2", "tab_sms");

			simSerialNum = mPhoneNumber.getMySimSerialNumber();
			if (simSerialNum.equals("0")) {
				mDbMain.updateSimInfo(simSerialNum);
			} else if (!simSerialNum.equals(mDbQuery.stringQuery(
					"sim_serial_number", "tab_sim_info"))) {

				if (isAddressAviable(safeNum_1)) {
					Log.i("my", "Boot send_1");
					try {
						Thread.sleep(1000 * 60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mSmsSender.sentMsg(safeNum_1, "此号码正在使用你的手机！");
					Toast.makeText(context, "自动发送成功！", 5000).show();
					// context.startService(new Intent(context, GpsMain.class));
					// try {
					// Thread.sleep(10 * 1000);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }
					// Intent intent2 = new Intent(context, MsgCtrl.class);
					// intent2.putExtra("sms", "--gps");
					// intent2.putExtra("incomingAddr", safeNum_1);
					// context.startService(intent2);
				}

				if (isAddressAviable(safeNum_2)) {
					Log.i("my", "Boot send_2");
					mSmsSender.sentMsg(safeNum_2, "此号码正在使用你的手机！");
					Toast.makeText(context, "自动发送成功！", 5000).show();
				}

			}
		}
	}

	private boolean isAddressAviable(String s1) {
		boolean flag = false;
		if (!s1.equals(DEFAULT_VALUE)) {
			flag = true;
		}
		return flag;
	}
}