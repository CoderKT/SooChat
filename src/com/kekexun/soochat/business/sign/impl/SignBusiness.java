package com.kekexun.soochat.business.sign.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kekexun.soochat.activity.sign.SignActivity;
import com.kekexun.soochat.business.impl.BaseBusiness;
import com.kekexun.soochat.common.BusinessNameEnum;
import com.kekexun.soochat.common.K;
import com.kekexun.soochat.smack.BaseIMServer;

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
	 * ִ�е�¼
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void doSignIn(final String username, final String password, final Handler handler) throws Exception {
		Log.d(tag, "------ 0 SignBusiness.doSignIn()");
		
		// �����߳̽��������¼
		new Thread() {

			@Override
			public void run() {
				// ��¼
				try {
					boolean isConnected = imServer.connect(username, password);
					Message msg = new Message();
					if (isConnected) {
						msg.what = SignActivity.CONNECT_TO_IMSERVER_SUCCESS;
					} else {
						msg.what = SignActivity.CONNECT_TO_IMSERVER_FAILURE;
						msg.obj = "��¼ IMServer ʧ�ܣ�";
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(tag, "@@@@@@ SignBusiness.doSignIn#new-Thread().run(), ��¼IMServer������ϸԭ��" + e.getMessage());
					Message msg = new Message();
					msg.what = SignActivity.CONNECT_TO_IMSERVER_FAILURE;
					msg.obj = "��¼ IMServer ʧ�ܣ���ϸԭ��" + e.getMessage();
					handler.sendMessage(msg);
				}
			}			
			
		}.start();
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
	}}
