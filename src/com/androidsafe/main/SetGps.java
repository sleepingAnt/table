package com.androidsafe.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.androidsafe.db.DBQuery;

public class SetGps extends Activity implements OnClickListener {
	Button btn_manual;
	private TextView tv_Latitude;
	private TextView tv_Longitude;
	private TextView tv_High;
	private TextView tv_Direction;
	private TextView tv_Speed;
	private TextView tv_GpsTime;
	private DBQuery mDbQuery = new DBQuery(this);

	// private SQLiteDatabase db;
	// private DBMain mDbGps = new DBMain(this);;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.set_gps);
		tv_Latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_Longitude = (TextView) findViewById(R.id.tv_longitude);
		tv_High = (TextView) findViewById(R.id.tv_high);
		tv_Direction = (TextView) findViewById(R.id.tv_direction);
		tv_Speed = (TextView) findViewById(R.id.tv_speed);
		tv_GpsTime = (TextView) findViewById(R.id.tv_gpstime);
		btn_manual = (Button) findViewById(R.id.btn_manual);
		setViews();

		btn_manual.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String _lat = (float) (mDbQuery.floatQuery("latitude",
						"tab_gps") - 0.002813) + "";
				String _lon = (float) (mDbQuery.floatQuery("longitude",
						"tab_gps") + 0.002354) + "";
				String s = "http://ditu.google.cn/maps?hl=zh_cn&mrt=loc&q=" + _lat
						+ "," + _lon;
				Uri uri = Uri.parse(s);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
	}

	private void setViews() {
		tv_Latitude.setText(String.format("纬度:%f",
				mDbQuery.floatQuery("latitude", "tab_gps")));
		tv_Longitude.setText(String.format("经度:%f",
				mDbQuery.floatQuery("longitude", "tab_gps")));
		tv_High.setText(String.format("海拔:%f",
				mDbQuery.floatQuery("high", "tab_gps")));
		tv_Direction.setText(String.format("方向:%f",
				mDbQuery.floatQuery("direct", "tab_gps")));
		tv_Speed.setText(String.format("速度:%f",
				mDbQuery.floatQuery("speed", "tab_gps")));
		tv_GpsTime.setText(String.format("时间:%s",
				mDbQuery.stringQuery("gpstime", "tab_gps")));

		if (mDbQuery.intQuery("state", "tab_gps") == -1) {
			btn_manual.setText("请开启定位功能");
			btn_manual.setEnabled(false);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(SetGps.this, SetPanel.class));
			SetGps.this.finish();
			return false;
		}
		return false;
	}

	public void onClick(View v) {
	}

}
