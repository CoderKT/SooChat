package com.kekexun.soochat.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.business.sign.impl.SignBusiness;

public class SignActivity extends BaseActivity {
	
	private static final String tag = "LoginActivity";
	
	private EditText etLoginName;
	private EditText etLoginPassword;
	private SignBusiness loginBusiness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sign);
		
		// �������״̬
		//checkNetState(LIFTCYCLE_CREATE);
		
		loginBusiness = new SignBusiness(this);
		
		initViews();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// �������״̬
		//checkNetState(LIFTCYCLE_START);
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		etLoginName = (EditText) this.findViewById(R.id.et_login_name);
		etLoginPassword = (EditText) this.findViewById(R.id.et_login_password);
	}
	
	/**
	 * ��¼
	 * @param view
	 */
	public void loginAction(View view) {
		String loginName = etLoginName.getText().toString().trim();
		if (TextUtils.isEmpty(loginName)) {
			Toast.makeText(this, "��¼�˺Ų���Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String loginPassword = etLoginPassword.getText().toString().trim();
		if (TextUtils.isEmpty(loginPassword)) {
			Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			loginBusiness.loginOpenfire(loginName, loginPassword);
			//loginBusiness.doLogin(loginName, loginPassword);
			
			//TOD ��������Ϊģ���¼ begin
			loginBusiness.saveLoginInfo(loginName, loginPassword, true);
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
			//TOD ��������Ϊģ���¼ end
		} catch (Exception e) {
			Log.e(tag, "��¼�����쳣��" + e.getMessage());
			Toast.makeText(this, "��¼�쳣��ԭ��", Toast.LENGTH_SHORT).show();
		}
	}

}
