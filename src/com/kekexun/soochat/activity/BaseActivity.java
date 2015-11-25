package com.kekexun.soochat.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public class BaseActivity extends Activity {

	protected static final int LIFTCYCLE_CREATE = 0;
	protected static final int LIFTCYCLE_START = 1;

	// ����
	protected static final int WINDOW_VERTICAL = 1;
	// ����
	protected static final int WINDOW_HORIZONTAL = 0;
	
	/**
	 * �������״̬
	 */
	protected boolean getNetState(int liftcycle) {
		ConnectivityManager conectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conectivityManager.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * ����������
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected int horizontalOrVertical() {
		int width = getWindowManager().getDefaultDisplay().getWidth(); //TODO ��������
		int height = getWindowManager().getDefaultDisplay().getHeight();
		
		if (width < height) {
			return WINDOW_VERTICAL; // ����
		} else {
			return WINDOW_HORIZONTAL; // ����
		}
	}
	
}
