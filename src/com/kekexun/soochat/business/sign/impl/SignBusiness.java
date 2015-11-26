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
	 * 判断是否已经登录
	 * @param context
	 * @return
	 */
	public boolean isLogin() {
		return sharedPreferences.getBoolean(K.Login.IS_LOGIN, false);
	}
	
	/**
	 * 执行登录
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
			
			// 登录
			imServer.connect(username, password, new ConnectionListener() {
				
				@Override
				public void onSuccess() {
					Log.d(tag, "------ 3 ConnectionListener.onSuccess(). 链接 IMServer 成功！");
				}
				
				@Override
				public void onFailure(String errorMessage) {
					Log.e(tag, "@@@@@@ 3 ConnectionListener.onFailure(). 连接IMServer 失败！详细原因：" + errorMessage);
				}
				
			});
		}
		
		while (!imServer.isConnected()) {
			Log.d(tag, "------ 0.3 Waiting for the sign thread to connect IM Server.");
		}
		
		Log.d(tag, "------ 0.4 SignBusiness.doSignIn() conn = " + imServer.getConn()); 
		Log.d(tag, "------ 0.5 SignBusiness.doSignIn() isConnected = " + imServer.getConn().isConnected());
		
		// 报错登录信息
		saveLoginInfo(username, password, true);
		// 跳转到主页面
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		
		((SignActivity) context).finish();
	}
	
	/**
	 * 通过 SharedPrefences 保存
	 * @param jsonParam
	 * @param context
	 * @return
	 */
	public boolean saveLoginInfo(String loginName, String loginPassword, boolean isLogin) {
		// 得到编辑器
		Editor editor = sharedPreferences.edit();
		editor.putString(K.Login.LOGIN_NAME, loginName);
		editor.putString(K.Login.LOGIN_PASSWORD, loginPassword);
		editor.putBoolean(K.Login.IS_LOGIN, isLogin);
		return editor.commit();
	}
	
}
