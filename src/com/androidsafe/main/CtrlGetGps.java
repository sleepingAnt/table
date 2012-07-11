package com.androidsafe.main;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.androidsafe.db.DBMain;
import com.androidsafe.db.DBQuery;
import com.androidsafe.sms.SmsSender;

public class CtrlGetGps extends Activity {
	private Button btn_ctrl_get_gps_back;
	private Button btn_ctrl_get_gps_get;
	private String addrNum;
	private String pwd;
	private SmsSender sms = new SmsSender(CtrlGetGps.this);
	private SQLiteDatabase db;
	private DBMain mDbMain = new DBMain(this);
	private DBQuery mDbQuery = new DBQuery(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctrl_get_gps);

		btn_ctrl_get_gps_back = (Button) findViewById(R.id.btn_ctrl_get_gps_back);
		btn_ctrl_get_gps_get = (Button) findViewById(R.id.btn_ctrl_get_gps_get);
		db = mDbMain.getWritableDatabase();

		btn_ctrl_get_gps_back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
//				startActivity(new Intent(CtrlGetGps.this, CtrlPanel.class));
				if (db.isOpen()) {
					db.close();
				}
				CtrlGetGps.this.finish();
			}
		});

		btn_ctrl_get_gps_get.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				addrNum = mDbQuery.stringQuery("num", "tab_ctrlnum");
				pwd = mDbQuery.stringQuery("pwd", "tab_ctrlnum");
				
				sms.sentMsg(addrNum, "--androidsafe_reg;gps;"+pwd);
				if (db.isOpen()) {
					db.close();
				}
				Toast.makeText(getApplicationContext(), "·¢ËÍ³É¹¦!", 3000).show();
				CtrlGetGps.this.finish();
			}
		});
	}

}
