package com.kekexun.soochat.activity.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.activity.main.MainActivity;

public class ChatWinActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main_chat_chatwin);
		
		initViews();
	}
	
	/**
	 * Initialize
	 */
	private void initViews() {
		TextView tvUserTitle = (TextView) findViewById(R.id.tvUserTitle);
		tvUserTitle.setText(getIntent().getStringExtra("userTitle"));
	}
	
	/**
	 * ∑µªÿ
	 * @param view
	 */
	public void goBack(View view) {
		Intent intent = new Intent(this, MainActivity.class); //TOTO∑µªÿ“Ï≥£
		this.startActivity(intent);
		this.finish();
	}
	
}
