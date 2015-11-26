package com.kekexun.soochat.business.sign.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.activity.sign.SignActivity;
import com.kekexun.soochat.business.impl.BaseBusiness;
import com.kekexun.soochat.common.BusinessNameEnum;
import com.kekexun.soochat.common.K;
import com.kekexun.soochat.smack.BaseIMServer;
import com.kekexun.soochat.smack.ConnectionListener;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 */
public class SignBusiness extends BaseBusiness {
	
	private static final String tag = "LoginBusiness";
	
	protected Context context;
	protected BusinessNameEnum businessUrl;
	protected SharedPreferences sharedPreferences;
	
	private BaseIMServer imServer;
	
	/**
	 * Constructor
	 */
	public SignBusiness() {
		
	}
	
	/**
	 * Constructor
	 * @param context
	 */
	public SignBusiness(Context context) {
		this.context = context;
		this.sharedPreferences = context.getSharedPreferences("prefenrences", Context.MODE_PRIVATE);
		this.imServer = BaseIMServer.getInstance();
		imServer.setSharedPreferences(sharedPreferences);
	}
	
	/**
	 * �ж��Ƿ��Ѿ���¼
	 * @param context
	 * @return
	 */
	public boolean isLogin() {
		return sharedPreferences.getBoolean(K.Login.IS_LOGIN, false);
	}
	
	/**
	 * ִ�е�¼
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void doSignIn(final String username, final String password) throws Exception {
		Log.d(tag, "------ 0 SignBusiness.doSignIn()");
		
		if (imServer.getConn() == null || !imServer.getConn().isConnected()) {
			Log.d(tag, "------ 0.0 SignBusiness.doSignIn() conn is null or not connected!");
			Log.d(tag, "------ 0.1 SignBusiness.doSignIn() conn = " + imServer.getConn());
			if (imServer.getConn() != null) {
				Log.d(tag, "------ 0.2 SignBusiness.doSignIn() isConnected = " + imServer.getConn());
			} else {
				Log.d(tag, "------ 0.2 SignBusiness.doSignIn() isConnected = null" );
			}
			
			// ��¼
			imServer.connect(username, password, new ConnectionListener() {
				
				@Override
				public void onSuccess() {
					Log.d(tag, "------ 3 ConnectionListener.onSuccess(). ���� IMServer �ɹ���");
				}
				
				@Override
				public void onFailure(String errorMessage) {
					Log.e(tag, "@@@@@@ 3 ConnectionListener.onFailure(). ����IMServer ʧ�ܣ���ϸԭ��" + errorMessage);
				}
				
			});
		}
		
		while (!imServer.isConnected()) {
			Log.d(tag, "------ 0.3 Waiting for the sign thread to connect IM Server.");
		}
		
		Log.d(tag, "------ 0.4 SignBusiness.doSignIn() conn = " + imServer.getConn()); 
		Log.d(tag, "------ 0.5 SignBusiness.doSignIn() isConnected = " + imServer.getConn().isConnected());
		
		// �����¼��Ϣ
		saveLoginInfo(username, password, true);
		// ��ת����ҳ��
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		
		((SignActivity) context).finish();
	}
	
	/**
	 * ͨ�� SharedPrefences ����
	 * @param jsonParam
	 * @param context
	 * @return
	 */
	public boolean saveLoginInfo(String loginName, String loginPassword, boolean isLogin) {
		// �õ��༭��
		Editor editor = sharedPreferences.edit();
		editor.putString(K.Login.LOGIN_NAME, loginName);
		editor.putString(K.Login.LOGIN_PASSWORD, loginPassword);
		editor.putBoolean(K.Login.IS_LOGIN, isLogin);
		return editor.commit();
	}
	
}
