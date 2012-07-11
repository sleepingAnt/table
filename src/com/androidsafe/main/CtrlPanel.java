package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CtrlPanel extends Activity implements OnClickListener {
	Button btn_ctrl_panel_backup;
	Button btn_ctrl_panel_delete;
	Button btn_ctrl_panel_get_gps;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctrl_panel);

		btn_ctrl_panel_backup = (Button) findViewById(R.id.btn_ctrl_panel_backup);
		btn_ctrl_panel_delete = (Button) findViewById(R.id.btn_ctrl_panel_delete);
		btn_ctrl_panel_get_gps = (Button) findViewById(R.id.btn_ctrl_panel_get_gps);

		btn_ctrl_panel_backup.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(CtrlPanel.this, CtrlBackup.class));
				// CtrlPanel.this.finish();
			}
		});

		btn_ctrl_panel_delete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(CtrlPanel.this, CtrlDelete.class));
				// CtrlPanel.this.finish();
			}
		});

		btn_ctrl_panel_get_gps.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(CtrlPanel.this, CtrlGetGps.class));
				// CtrlPanel.this.finish();
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(CtrlPanel.this, AndroidSafeActivity.class));
			CtrlPanel.this.finish();
			return false;
		}
		return false;
	}

	public void onClick(View v) {

	}
}
