package com.androidsafe.phone;

import android.content.Context;
import android.net.ConnectivityManager;

/*
 * @Description: 判断设备网络是否可用
 * 
 */
public class NetworkState {

	public Context context;
	private ConnectivityManager cwjManager;

	public NetworkState(Context context) {
		this.context = context;

	}

	public boolean isAvaiable() {
		cwjManager = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean flag = false;
		if (cwjManager.getActiveNetworkInfo() != null) {
			flag = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

}
