package com.androidsafe.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsafe.db.DBMain;
import com.androidsafe.db.DBQuery;
import com.androidsafe.db.SmsData;

public class SetSms extends Activity {
	private Button btn_set_sms_ok;
	private TextView tv_set_sms_safe_num1;
	private TextView tv_set_sms_safe_num2;
	private DBMain smsDB = new DBMain(this);
	private DBQuery mDbQuery = new DBQuery(this);
	private SmsData mSmsData = new SmsData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_sms);

		btn_set_sms_ok = (Button) findViewById(R.id.btn_set_sms_ok);
		btn_set_sms_ok = (Button) findViewById(R.id.btn_set_sms_ok);
		tv_set_sms_safe_num1 = (TextView) findViewById(R.id.tv_set_sms_safe_num1);
		tv_set_sms_safe_num2 = (TextView) findViewById(R.id.tv_set_sms_safe_num2);
		tv_set_sms_safe_num1.setText(mDbQuery.stringQuery("safeNum_1",
				"tab_sms"));
		tv_set_sms_safe_num2.setText(mDbQuery.stringQuery("safeNum_2",
				"tab_sms"));

		tv_set_sms_safe_num1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(SetSms.this);
				final View DialogView = factory.inflate(
						R.layout.set_sms_dialog, null);
				AlertDialog dialog = new AlertDialog.Builder(SetSms.this)
						.setTitle("请输入电话号码")
						.setView(DialogView)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										EditText et = (EditText) DialogView
												.findViewById(R.id.et_set_num);
										if (et.getText().toString() == "") {
											Toast.makeText(
													getApplicationContext(),
													"不能为空！", Toast.LENGTH_SHORT)
													.show();
										} else {
											setTextView(tv_set_sms_safe_num1,
													et);
											mSmsData.safeNum_1 = et.getText()
													.toString();
											smsDB.getWritableDatabase();
											if (smsDB
													.updateSmsData(mSmsData, 1)) {
												Toast.makeText(
														getApplicationContext(),
														"保存SMS数据成功！:",
														Toast.LENGTH_SHORT)
														.show();
												smsDB.closeDB();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"保存SMS数据失败！:",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									}
								}).setNegativeButton("取消", null).create();
				dialog.show();
			}
		});

		tv_set_sms_safe_num2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(SetSms.this);
				final View DialogView = factory.inflate(
						R.layout.set_sms_dialog, null);
				AlertDialog dialog = new AlertDialog.Builder(SetSms.this)
						.setTitle("请输入电话号码")
						.setView(DialogView)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										EditText et = (EditText) DialogView
												.findViewById(R.id.et_set_num);
										if (et.getText().toString() == "") {
											Toast.makeText(
													getApplicationContext(),
													"不能为空！", Toast.LENGTH_SHORT)
													.show();
										} else {
											setTextView(tv_set_sms_safe_num2,
													et);
											mSmsData.safeNum_2 = et.getText()
													.toString();
											smsDB.getWritableDatabase();
											if (smsDB
													.updateSmsData(mSmsData, 2)) {
												Toast.makeText(
														getApplicationContext(),
														"保存SMS数据成功！:",
														Toast.LENGTH_SHORT)
														.show();
												smsDB.closeDB();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"保存SMS数据失败！:",
														Toast.LENGTH_SHORT)
														.show();
											}
										}

									}
								}).setNegativeButton("取消", null).create();
				dialog.show();
			}
		});

		btn_set_sms_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SetSms.this, SetPanel.class));
				SetSms.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(SetSms.this, SetPanel.class));
			SetSms.this.finish();
			return false;
		}
		return false;
	}

	public void onClick(View v) {
	}

	private void setTextView(TextView tv1, EditText tv2) {
		tv1.setText(tv2.getText().toString());
	}

}
