package com.kekexun.soochat.business.sign.impl;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

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
import com.kekexun.soochat.smack.IMServer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * @author Ke.Wang
 *
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
	 * �ж��Ƿ��Ѿ���¼
	 * @param context
	 * @return
	 */
	public boolean isLogin() {
		return sharedPreferences.getBoolean(K.Login.IS_LOGIN, false);
	}
	
	public void loginOpenfire(final String username, final String password) {
		new Thread() {

			@Override
			public void run() {
				IMServer imServer = new IMServer();
				try {
					imServer.login(username, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("aaaa");
			}
			
		}.start();
	}
	
	/**
	 * ִ�е�¼
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void doLogin(final String username, final String password) throws Exception {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String hostAddress = sharedPreferences.getString(K.PreferenceKey.KEY_HOST_URL, "http://192.168.9.106:8080/KfAppService"); // TODO
		String url = hostAddress + "/" + BusinessNameEnum.LOGIN_BUSINESS.getName();
		
		asyncHttpClient.post(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int responseCode, Header[] headers, byte[] responseContent) {
				String content = "";
				try {
					content = new String(responseContent, "utf-8");
				} catch (UnsupportedEncodingException e) {
					Log.e(tag, "��������ת�����ַ�������ԭ��" + e.getMessage());
				}
				
				Log.d(tag, "��¼�ɹ���content=" + content);
				
				saveLoginInfo(username, password, true);
				
				Intent intent = new Intent();
				intent.setClass(context, MainActivity.class);
				context.startActivity(intent);
				
				((SignActivity) context).finish();
			}
			
			@Override
			public void onFailure(int responseCode, Header[] headers, byte[] responseContent, Throwable t) {
				Toast.makeText(context, "��¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				
				String content = "";
				try {
					content = new String(responseContent, "utf-8");
				} catch (Exception e) {
					content += " ��¼ʧ�ܣ�������Ϣ����ת���쳣����ϸԭ��: " + e.getMessage();
					Log.e(tag, content);
				}
				
				Log.d(tag, "��¼ʧ�ܡ�content=" + content);
			}
		});
		
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
