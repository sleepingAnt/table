package com.androidsafe.main;

import com.androidsafe.db.DBQuery;
import com.androidsafe.gps.GpsMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
	private Button bt_login_ok;
	private DBQuery mDbQuery = new DBQuery(this);
	private EditText et_login_pwd;
	private Button bt_login_reset;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_login);
		bt_login_ok = (Button) findViewById(R.id.bt_login_ok);
		bt_login_reset = (Button) findViewById(R.id.bt_login_reset);
		et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);

		bt_login_ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if (et_login_pwd.getText().toString()
						.equals(mDbQuery.stringQuery("login_pwd", "tab_pwd"))) {
					startActivity(new Intent(Login.this, SetPanel.class));
					startService(new Intent(Login.this, GpsMain.class));

					Login.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "√‹¬Î¥ÌŒÛ£°", 2000)
							.show();
					et_login_pwd.setText("");
				}

			}
		});

		bt_login_reset.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				et_login_pwd.setText("");
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(Login.this, AndroidSafeActivity.class));
			Login.this.finish();
			return false;
		}
		return false;
	}

	public void onClick(View v) {

	}
}
