package com.kekexun.soochat.activity.boot;

import android.content.Intent;
import android.os.Bundle;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.common.K;

/**
 * 
 * @author Ke.Wang
 *
 */
public class BootActivity extends BaseActivity {
	
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ÅÐ¶ÏÊÇ·ñÒÑ¾­µÇÂ¼
		isLogin = sharedPreferences.getBoolean(K.Login.IS_LOGIN, false);
		
		Intent intent = new Intent();
		if (!isLogin) {
			intent.setClass(BootActivity.this, WelcomeActivity.class);
		} else {
			intent.setClass(BootActivity.this, MainActivity.class);
		}
		
		this.startActivity(intent);
		this.finish();
	}
	
}
