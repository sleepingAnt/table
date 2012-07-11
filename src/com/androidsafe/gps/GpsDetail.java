package com.androidsafe.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsDetail {

	Context context;
	LocationManager locationManager;
	LocationListener llistener;
	String provider;
	public Location mLocation;

	public GpsDetail(Context context) {
		this.context = context;
	}

	public void getGpsAetail() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context
				.getSystemService(serviceName);
		locationManager.setTestProviderEnabled("gps", true);
		provider = locationManager.getBestProvider(criteria, true);
		Log.d("provider", provider);

		llistener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.i("onLocationChanged", "come in");
				if (location != null) {
					Log.w("Location",
							"Current altitude = " + location.getAltitude());
					Log.w("Location",
							"Current latitude = " + location.getLatitude());
					mLocation = location;
				}
				locationManager.removeUpdates(this);
				locationManager.setTestProviderEnabled(provider, false);
			}

			public void onProviderDisabled(String provider) {
				Log.i("my", "onProviderDisabled");

			}

			public void onProviderEnabled(String provider) {
				Log.i("my", "onProviderEnabled");
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.i("my", "onStatusChanged");

			}

		};
		locationManager.requestLocationUpdates(provider, 1000, (float) 1000.0,
				llistener);
	}
}
//
// protected void onDestroy() {
// locationManager.removeUpdates(llistener);
// locationManager.setTestProviderEnabled(provider, false);
// super.onDestroy();
// }

