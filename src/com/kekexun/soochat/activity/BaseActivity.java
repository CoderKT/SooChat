package com.kekexun.soochat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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
	
	// ����ѡ��
	protected SharedPreferences sharedPreferences;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sharedPreferences = getSharedPreferences("prefenrences", Context.MODE_PRIVATE);
	}

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
