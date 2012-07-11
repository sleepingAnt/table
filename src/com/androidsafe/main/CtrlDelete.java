package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.androidsafe.db.DBMain;
import com.androidsafe.db.DBQuery;
import com.androidsafe.sms.SmsSender;

public class CtrlDelete extends Activity {
	Button btn_ctrl_delete_back;
	Button btn_ctrl_delete_delete;
	private String addrNum;
	private String pwd;
	private SmsSender sms = new SmsSender(this);
	private SQLiteDatabase db;
	private DBMain mDbMain = new DBMain(this);
	private DBQuery mDbQuery = new DBQuery(this);

	// private ContactsBackupHelper mContactsBackupHelper = new
	// ContactsBackupHelper(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctrl_delete);

		btn_ctrl_delete_back = (Button) findViewById(R.id.btn_ctrl_delete_back);
		btn_ctrl_delete_delete = (Button) findViewById(R.id.btn_ctrl_delete_delete);
		db = mDbMain.getWritableDatabase();
		btn_ctrl_delete_back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// startActivity(new Intent(CtrlDelete.this, CtrlPanel.class));
				CtrlDelete.this.finish();
			}
		});

		btn_ctrl_delete_delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// mContactsBackupHelper.writePeople();
				// mContactsBackupHelper.deleteAll();
				addrNum = mDbQuery.stringQuery("num", "tab_ctrlnum");
				pwd = mDbQuery.stringQuery("pwd", "tab_ctrlnum");
				// sms.sentMsg(addrNum, "delete");
				sms.sentMsg(addrNum, "--androidsafe_reg;delete;" + pwd);
				if (db.isOpen()) {
					db.close();
				}
				Toast.makeText(getApplicationContext(), "·¢ËÍ³É¹¦!", 3000).show();
				startActivity(new Intent(CtrlDelete.this, CtrlPanel.class));
				CtrlDelete.this.finish();
			}
		});

	}

}
