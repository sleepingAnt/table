package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidsafe.gps.GpsMain;

public class SetPanel extends Activity implements OnClickListener {
	private Button btn_set_panel_gps;
	private Button btn_set_panel_sms;
	private Button btn_set_panel_mail;
	private Button btn_set_panel_rest_pwd;
	private Button btn_set_panel_settings;
	private Button btn_set_panel_about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_panel);
		btn_set_panel_gps = (Button) findViewById(R.id.btn_set_panel_gps);
		btn_set_panel_sms = (Button) findViewById(R.id.btn_set_panel_sms);
		btn_set_panel_mail = (Button) findViewById(R.id.btn_set_panel_mail);
		btn_set_panel_rest_pwd = (Button) findViewById(R.id.btn_set_panel_rest_pwd);
		btn_set_panel_settings = (Button) findViewById(R.id.btn_set_panel_settings);
		btn_set_panel_about = (Button) findViewById(R.id.btn_set_panel_about);

		btn_set_panel_gps.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(SetPanel.this, SetGps.class));
				// startService(new Intent(SetPanel.this, GpsMain.class));
				SetPanel.this.finish();
			}
		});

		btn_set_panel_sms.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(SetPanel.this, SetSms.class));
				SetPanel.this.finish();
			}
		});

		btn_set_panel_mail.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(SetPanel.this, SetMail.class));
				SetPanel.this.finish();
			}
		});

		btn_set_panel_rest_pwd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(SetPanel.this, SetNewPwd.class));
			}
		});

		btn_set_panel_settings.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				stopService(new Intent(SetPanel.this, GpsMain.class));

			}
		});

		btn_set_panel_about.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(SetPanel.this, About.class));
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(SetPanel.this, AndroidSafeActivity.class));
			SetPanel.this.finish();
			return false;
		}
		return false;
	}

	public void onClick(View v) {
	}

}
