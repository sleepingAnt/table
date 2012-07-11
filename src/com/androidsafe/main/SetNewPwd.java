package com.androidsafe.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidsafe.db.DBMain;

public class SetNewPwd extends Activity {
	private DBMain mDbMain = new DBMain(this);
	private EditText et_new_pwd_1;
	private EditText et_new_pwd_2;
	private Button bt_new_pwd_ok;
	private Button bt_new_pwd_reset;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_pwd);
		et_new_pwd_1 = (EditText) findViewById(R.id.et_new_pwd_1);
		et_new_pwd_2 = (EditText) findViewById(R.id.et_new_pwd_2);
		bt_new_pwd_ok = (Button) findViewById(R.id.bt_new_pwd_ok);
		bt_new_pwd_reset = (Button) findViewById(R.id.bt_new_pwd_reset);

		bt_new_pwd_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDbMain.getWritableDatabase();
				// 判断两次输入是否相同
				if (et_new_pwd_1.getText().toString().length() < 6) {
					pwd = "1";
				} else if (et_new_pwd_1.getText().toString()
						.equals(et_new_pwd_2.getText().toString())) {
					pwd = et_new_pwd_1.getText().toString();
				} else {
					pwd = "0";
				}
				if (pwd.equals("1")) {
					Toast.makeText(getApplicationContext(), "密码至少为6位数！", 3000)
							.show();
					et_new_pwd_1.setText("");
					et_new_pwd_2.setText("");
					et_new_pwd_1.setFocusable(true);
					et_new_pwd_1.setFocusableInTouchMode(true);
					et_new_pwd_1.requestFocus();
				} else if (pwd.equals("0")) {
					Toast.makeText(getApplicationContext(), "两次输入不一致！", 3000)
							.show();
					et_new_pwd_1.setText("");
					et_new_pwd_2.setText("");
					et_new_pwd_1.setFocusable(true);
					et_new_pwd_1.setFocusableInTouchMode(true);
					et_new_pwd_1.requestFocus();
				} else {

					SharedPreferences.Editor userData = getSharedPreferences(
							"isFirstUse", 0).edit();
					userData.putString("time", "1");
					userData.commit();
					mDbMain.setPassword(pwd);
					Toast.makeText(getApplicationContext(), "设置成功，请记住新密码！！",
							3000).show();
					SetNewPwd.this.finish();
				}
			}
		});

		bt_new_pwd_reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				et_new_pwd_1.setText("");
				et_new_pwd_2.setText("");
			}
		});

	}
}
