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
import com.androidsafe.db.MailData;

public class SetMail extends Activity {
	private Button btn_set_mail_ok;
	private TextView tv_set_mail_1;
	private TextView tv_set_mail_2;
	private DBMain mailDB = new DBMain(this);
	private DBQuery mDbQuery = new DBQuery(this);
	MailData mMailData = new MailData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_mail);
		btn_set_mail_ok = (Button) findViewById(R.id.btn_set_mail_ok);
		tv_set_mail_1 = (TextView) findViewById(R.id.tv_set_mail_1);
		tv_set_mail_2 = (TextView) findViewById(R.id.tv_set_mail_2);
		tv_set_mail_1.setText(mDbQuery.stringQuery("safeMail_1", "tab_mail"));
		tv_set_mail_2.setText(mDbQuery.stringQuery("safeMail_2", "tab_mail"));

		mailDB.getWritableDatabase();
		btn_set_mail_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				startActivity(new Intent(SetMail.this, SetPanel.class));
				SetMail.this.finish();
			}

		});

		tv_set_mail_1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(SetMail.this);
				final View DialogView = factory.inflate(
						R.layout.set_mail_dialog, null);
				AlertDialog dialog = new AlertDialog.Builder(SetMail.this)
						.setTitle("请输入邮箱")
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
										} else if (!et
												.getText()
												.toString()
												.matches(
														"[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+")) {
											Toast.makeText(
													getApplicationContext(),
													"请输入正确的邮箱地址！",
													Toast.LENGTH_SHORT).show();
											et.setText("");

										} else {
											setTextView(tv_set_mail_1, et);
											mMailData.safeMail_1 = et.getText()
													.toString();
											mailDB.getWritableDatabase();
											if (mailDB.updateMailData(
													mMailData, 1)) {
												Toast.makeText(
														getApplicationContext(),
														"保存mail数据成功！:",
														Toast.LENGTH_SHORT)
														.show();
												mailDB.closeDB();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"保存mail数据失败！:",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									}
								}).setNegativeButton("取消", null).create();
				dialog.show();
			}
		});

		tv_set_mail_2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(SetMail.this);
				final View DialogView = factory.inflate(
						R.layout.set_mail_dialog, null);
				AlertDialog dialog = new AlertDialog.Builder(SetMail.this)
						.setTitle("请输入邮箱")
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
											setTextView(tv_set_mail_2, et);
											mMailData.safeMail_2 = et.getText()
													.toString();
											mailDB.getWritableDatabase();
											if (mailDB.updateMailData(
													mMailData, 2)) {
												Toast.makeText(
														getApplicationContext(),
														"保存mail数据成功！:",
														Toast.LENGTH_SHORT)
														.show();
												mailDB.closeDB();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"保存mail数据失败！:",
														Toast.LENGTH_SHORT)
														.show();
											}
										}

									}
								}).setNegativeButton("取消", null).create();
				dialog.show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(SetMail.this, SetPanel.class));
			SetMail.this.finish();
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
