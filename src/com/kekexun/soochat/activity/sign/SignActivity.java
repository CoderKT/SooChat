package com.kekexun.soochat.activity.sign;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.business.sign.impl.SignBusiness;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public class SignActivity extends BaseActivity {
	
	private static final String tag = "LoginActivity";
	
	private EditText etLoginName;
	private EditText etLoginPassword;
	private SignBusiness signBusiness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sign);
		
		// 检查网络状态
		//checkNetState(LIFTCYCLE_CREATE);
		
		signBusiness = new SignBusiness(this);
		
		initViews();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// 检查网络状态
		//checkNetState(LIFTCYCLE_START);
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		etLoginName = (EditText) this.findViewById(R.id.et_login_name);
		etLoginPassword = (EditText) this.findViewById(R.id.et_login_password);
	}
	
	/**
	 * 登录
	 * @param view
	 */
	public void signInAction(View view) {
		String loginName = etLoginName.getText().toString().trim();
		if (TextUtils.isEmpty(loginName)) {
			Toast.makeText(this, "登录账号不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String loginPassword = etLoginPassword.getText().toString().trim();
		if (TextUtils.isEmpty(loginPassword)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			signBusiness.doSignIn(loginName, loginPassword);
		} catch (Exception e) {
			Log.e(tag, "登录发生异常！" + e.getMessage());
			Toast.makeText(this, "登录异常！原因：" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
