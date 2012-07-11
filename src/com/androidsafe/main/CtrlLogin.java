package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidsafe.db.DBCtrlNum;
import com.androidsafe.db.DBMain;

public class CtrlLogin extends Activity {
	private Button btn_ctrl_panel_login_back;
	private Button btn_ctrl_panel_login_login;
	private EditText et_ctrl_panel_login_num;
	private EditText et_ctrl_panel_login_pwd;
	private DBMain mDbMain = new DBMain(CtrlLogin.this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctrl_panel_login);

		btn_ctrl_panel_login_back = (Button) findViewById(R.id.btn_ctrl_panel_login_back);
		btn_ctrl_panel_login_login = (Button) findViewById(R.id.btn_ctrl_panel_login_login);
		et_ctrl_panel_login_num = (EditText) findViewById(R.id.et_ctrl_panel_login_num);
		et_ctrl_panel_login_pwd = (EditText) findViewById(R.id.et_ctrl_panel_login_pwd);

		btn_ctrl_panel_login_back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(CtrlLogin.this,
						AndroidSafeActivity.class));
				CtrlLogin.this.finish();
			}
		});

		btn_ctrl_panel_login_login.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DBCtrlNum mDBCtrlNum = new DBCtrlNum();
				mDBCtrlNum.num = et_ctrl_panel_login_num.getText().toString();
				mDBCtrlNum.pwd = et_ctrl_panel_login_pwd.getText().toString();
				if (mDBCtrlNum.num.equals("") || mDBCtrlNum.pwd.equals("")) {
					Toast.makeText(getApplicationContext(), "²»ÄÜÎª¿Õ£¡", 2000)
							.show();
				} else {
					mDbMain.getWritableDatabase();
					mDbMain.addCtrlNum(mDBCtrlNum);
					startActivity(new Intent(CtrlLogin.this, CtrlPanel.class));
					CtrlLogin.this.finish();
				}
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(CtrlLogin.this, AndroidSafeActivity.class));
			CtrlLogin.this.finish();
			return false;
		}
		return false;
	}
}
