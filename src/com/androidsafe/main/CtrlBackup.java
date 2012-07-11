package com.androidsafe.main;

import com.androidsafe.db.DBQuery;
import com.androidsafe.sms.SmsSender;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CtrlBackup extends Activity {
	Button btn_ctrl_backup_back;
	Button btn_ctrl_backup_backup;
	private String addrNum;
	private String pwd;
	private SmsSender sms = new SmsSender(CtrlBackup.this);
	private DBQuery mDbQuery = new DBQuery(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctrl_backup);

		btn_ctrl_backup_back = (Button) findViewById(R.id.btn_ctrl_backup_back);
		btn_ctrl_backup_backup = (Button) findViewById(R.id.btn_ctrl_backup_backup);
		btn_ctrl_backup_back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
//				startActivity(new Intent(CtrlBackup.this, CtrlPanel.class));
				CtrlBackup.this.finish();
			}
		});

		btn_ctrl_backup_backup.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				addrNum = mDbQuery.stringQuery("num", "tab_ctrlnum");
				pwd = mDbQuery.stringQuery("pwd", "tab_ctrlnum");
				sms.sentMsg(addrNum, "--androidsafe_reg;backup;" + pwd);
				Toast.makeText(getApplicationContext(), "·¢ËÍ³É¹¦!", 3000).show();
				CtrlBackup.this.finish();
			}
		});

	}
//
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			startActivity(new Intent(CtrlBackup.this, SetPanel.class));
//			CtrlBackup.this.finish();
//			return false;
//		}
//		return false;
//	}

}
