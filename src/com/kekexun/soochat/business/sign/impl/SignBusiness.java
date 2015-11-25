package com.kekexun.soochat.business.sign.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.activity.sign.SignActivity;
import com.kekexun.soochat.business.impl.BaseBusiness;
import com.kekexun.soochat.common.BusinessNameEnum;
import com.kekexun.soochat.common.K;
import com.kekexun.soochat.pojo.ChatItem;
import com.kekexun.soochat.smack.BaseIMServer;
import com.kekexun.soochat.smack.ConnectionListener;
import com.kekexun.soochat.smack.IIMServer;

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
	
	public SignBusiness() {
		
	}
	
	public SignBusiness(Context context) {
		this.context = context;
		this.sharedPreferences = context.getSharedPreferences("prefenrences", Context.MODE_PRIVATE);
	}
	
	/**
	 * ÅÐ¶ÏÊÇ·ñÒÑ¾­µÇÂ¼
	 * @param context
	 * @return
	 */
	public boolean isLogin() {
		return sharedPreferences.getBoolean(K.Login.IS_LOGIN, false);
	}
	
	/**
	 * Ö´ÐÐµÇÂ¼
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void doLogin(final String username, final String password) throws Exception {
		final IIMServer imServer = new BaseIMServer(sharedPreferences);
		
		Log.d(tag, "------ 0 SignBusiness.doLogin()");
		
		if (((BaseIMServer) imServer).getConn() != null) {
			Log.d(tag, "------ 0 SignBusiness.doLogin() conn is exists. conn=" + ((BaseIMServer) imServer).getConn());
			
			saveLoginInfo(username, password, true);
			
			Intent intent = new Intent(context, MainActivity.class);
			//intent.putExtra("chatItems", (ArrayList<ChatItem>) chatItems);
			
			context.startActivity(intent);
			
			((SignActivity) context).finish();
			
			return;
		}
		
		// µÇÂ¼
		imServer.connect(username, password, new ConnectionListener() {
			
			@Override
			public void onSuccess() {
				Log.d(tag, "------ 3 ConnectionListener.onSuccess()");
				// 
				saveLoginInfo(username, password, true);
				
				//List<ChatItem> chatItems = imServer.queryRoster();
				//Log.d(tag, "------ 3.1 ConnectionListener.onSuccess() chatItems=" + chatItems);
				
				Intent intent = new Intent(context, MainActivity.class);
				//intent.putExtra("chatItems", (ArrayList<ChatItem>) chatItems);
				
				context.startActivity(intent);
				
				((SignActivity) context).finish();
			}
			
			@Override
			public void onFailure(String errorMessage) {
				Log.d(tag, "------ 3 IMServer connect()@ConnectionListener@onFailure()");
				Toast.makeText(context, "µÇÂ¼Ê§°Ü¡£ÏêÏ¸Ô­Òò£º" + errorMessage, Toast.LENGTH_SHORT).show();
				Log.e(tag, "µÇÂ¼Ê§°Ü¡£ÏêÏ¸Ô­Òò£º" + errorMessage);
			}
			
		});
		
	}
	
	/**
	 * Í¨¹ý SharedPrefences ±£´æ
	 * @param jsonParam
	 * @param context
	 * @return
	 */
	public boolean saveLoginInfo(String loginName, String loginPassword, boolean isLogin) {
		// µÃµ½±à¼­Æ÷
		Editor editor = sharedPreferences.edit();
		editor.putString(K.Login.LOGIN_NAME, loginName);
		editor.putString(K.Login.LOGIN_PASSWORD, loginPassword);
		editor.putBoolean(K.Login.IS_LOGIN, isLogin);
		return editor.commit();
	}
	
}
