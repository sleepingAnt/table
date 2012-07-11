package com.androidsafe.gps;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.androidsafe.db.DBMain;
import com.androidsafe.db.GpsData;

public class GpsMain extends Service {
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private LocationManager lm;
	private Location loc;
	private Criteria ct;
	private String provider;
	private DBMain gpsDB;
	private GpsData mGpsData;

	private final GpsStatus.Listener gpsListener = new GpsStatus.Listener() {

		public void onGpsStatusChanged(int event) {
			initLocation();
		}
	};

	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location arg0) {
			// λ���б仯�������ݿ�д��λ������
			if (isBetterLocation(arg0, loc)) {
				saveLastPosition(arg0);

			}
		}

		public void onProviderDisabled(String arg0) {
			Log.i("my", "onProviderDisabled");
			// initLocation();
		}

		public void onProviderEnabled(String arg0) {
			Log.i("my", "onProviderEnabled");
			// initLocation();
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			Log.i("my", "onStatusChanged");
			// initLocation();
		}

	};

	private void initLocation() {
		ct = new Criteria();
		lm.addGpsStatusListener(gpsListener);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// ��ȡ����GPS��λ��
			ct.setAccuracy(Criteria.ACCURACY_FINE);// �߾���
			ct.setAltitudeRequired(true);// ��ʾ����
			ct.setBearingRequired(true);// ��ʾ����
			ct.setSpeedRequired(true);// ��ʾ�ٶ�
			ct.setCostAllowed(false);// �������л���
			ct.setPowerRequirement(Criteria.POWER_LOW);// �͹���
			provider = lm.getBestProvider(ct, true);
			loc = lm.getLastKnownLocation(provider);
			// λ�ñ仯����
			Log.i("my", "gps_location ");
			lm.requestLocationUpdates(provider, 0, 10, locationListener);
		} else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.i("my", "net_location");
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20,
					locationListener);

		} else {

			mGpsData.state = -1;// δ������λ����
			gpsDB.addGpsData(mGpsData);
		}

	}

	public void saveLastPosition(Location location) {

		if (location != null) {

			gpsDB.getWritableDatabase();
			mGpsData.state = 1;
			// λ����Ϣ��Чʱ��д�����ݿ�
			if ((int) location.getLatitude() != 0) {
				mGpsData.latitude = (float) (location.getLatitude());// -
																		// 0.002813

				Log.i("my", "lat1:" + location.getLatitude());
				Log.i("my", "lat2:" + mGpsData.latitude);
				Log.i("my", "lat3:" + (float) location.getLatitude());

				mGpsData.longitude = (float) (location.getLongitude());// +0.002354
				mGpsData.high = (float) location.getAltitude();
				mGpsData.direct = location.getBearing();
				mGpsData.speed = location.getSpeed();
				Date d = new Date();
				d.setTime(location.getTime());
				mGpsData.gpsTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", d)
						.toString();
				d = null;

				// ��GPS����д��DB
				if (!gpsDB.addGpsData(mGpsData)) {
//					Toast.makeText(getApplicationContext(), "����GPS����ʧ��:",Toast.LENGTH_SHORT).show();
				} else {
					Log.i("my", "save data successfuly!");
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		Log.i("my", "stop service!");
		Toast.makeText(getApplicationContext(), "�رշ���ɹ���", 2000).show();
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		gpsDB = new DBMain(this);
		mGpsData = new GpsData();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		initLocation();
		Log.i("my", "service onCreateed!");

//		Toast.makeText(getApplicationContext(), "��������ɹ���", 2000).show();
		// super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}