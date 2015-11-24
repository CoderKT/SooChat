package com.kekexun.soochat.activity.main.myself;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.kekexun.soochat.activity.R;

public class MyselfSettingActivity extends Activity {
	
	private EditText etNewName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main_myself_setting);
		
		initViews();		
	}
	
	private void initViews() {
		etNewName = (EditText) this.findViewById(R.id.etNewName);
	}
	
	public void setName(View view) {
		String newName = etNewName.getText().toString();
		if (TextUtils.isEmpty(newName)) {
			return;
		}
		
		Intent data = new Intent();
		data.putExtra("newName", newName);
		
		setResult(0, data);
		
		finish();
	}
	
}
