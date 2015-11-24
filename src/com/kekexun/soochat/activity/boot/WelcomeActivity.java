package com.kekexun.soochat.activity.boot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.activity.sign.SignActivity;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_welcome);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this, SignActivity.class);
				
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}
			
		}, 1000);
	}
	
}
