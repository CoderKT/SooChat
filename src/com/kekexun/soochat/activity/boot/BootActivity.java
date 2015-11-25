package com.kekexun.soochat.activity.boot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.business.sign.impl.SignBusiness;

/**
 * 
 * @author Ke.Wang
 *
 */
public class BootActivity extends Activity {
	
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SignBusiness signBusiness = new SignBusiness(BootActivity.this);
		
		// ÅÐ¶ÏÊÇ·ñÒÑ¾­µÇÂ¼
		isLogin = signBusiness.isLogin();
		
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
