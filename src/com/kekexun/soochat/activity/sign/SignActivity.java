package com.kekexun.soochat.activity.sign;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.activity.main.MainActivity;
import com.kekexun.soochat.business.sign.impl.SignBusiness;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public class SignActivity extends BaseActivity {
	
	private static final String tag = "LoginActivity";
	
	private ImageView ivHeader;
	private EditText etLoginName;
	private EditText etLoginPassword;
	
	private SignBusiness signBusiness;
	
	/**
	 * 更新登录用户头像事件类型
	 */
	private final static int SHOW_LOGIN_HEADER_IMAGE = 0;
	
	private final static int QUERY_LOGIN_HEADER_IMAGE_ERROR = 1;
	
	public final static int CONNECT_TO_IMSERVER_SUCCESS = 2;
	
	public final static int CONNECT_TO_IMSERVER_FAILURE = 3;

	/**
	 * 主线程消息处理器
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_LOGIN_HEADER_IMAGE: 
					Bitmap bitmap = (Bitmap) msg.obj;
					ivHeader.setImageBitmap(bitmap);
				break;
				case QUERY_LOGIN_HEADER_IMAGE_ERROR: 
					String content = (String) msg.obj;
					Toast.makeText(SignActivity.this, content, Toast.LENGTH_LONG).show();
				break;
				case CONNECT_TO_IMSERVER_SUCCESS: 
					// 保存登录信息
					signBusiness.saveLoginInfo(etLoginName.getText().toString().trim(), etLoginPassword.getText().toString().trim(), true);
					// 跳转到主页面
					Intent intent = new Intent(SignActivity.this, MainActivity.class);
					SignActivity.this.startActivity(intent);
					
					SignActivity.this.finish();
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sign);
		
		// 检查网络状态
		//checkNetState(LIFTCYCLE_CREATE);
		
		// Initialize SignBusiness
		signBusiness = new SignBusiness(this);
		
		// Initialize views
		initViews();
		
		//queryMyHeaderImage("a", "b");
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
		ivHeader = (ImageView) this.findViewById(R.id.iv_header);
		etLoginName = (EditText) this.findViewById(R.id.et_login_name);
		etLoginPassword = (EditText) this.findViewById(R.id.et_login_password);
	}
	
	/**
	 * 获取登录用户的头像
	 */
	private void queryMyHeaderImage(String loginName, String path) {
		final String tmp = "http://a.hiphotos.baidu.com/image/pic/item/aec379310a55b319c3dd7a9e45a98226cffc1730.jpg";
		new Thread() {
			
			@Override
			public void run() {
				try {
					URL url = new URL(tmp);
					HttpURLConnection httpUrlConnection =  (HttpURLConnection) url.openConnection();
					// 设置请求的方式
					httpUrlConnection.setRequestMethod("GET");
					// 设置链接超时时间
					httpUrlConnection.setConnectTimeout(5000);
					// 设置读取超时时间(读取图片一般时，网络掉线等异常)
					httpUrlConnection.setReadTimeout(10000);
					// 设置其他参数
					httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");

					// 获取响应吗
					int responseCode = httpUrlConnection.getResponseCode();
					switch(responseCode) {
						case 200:
							InputStream is = httpUrlConnection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							//ivHeader.setImageBitmap(bitmap);
							//告诉主线程一个消息，帮当前线程更新头像控件
							Message message = new Message();
							message.what = SHOW_LOGIN_HEADER_IMAGE;
							message.obj = bitmap;
							handler.sendMessage(message);
							
							is.close();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					Message message = new Message();
					message.what = QUERY_LOGIN_HEADER_IMAGE_ERROR;
					message.obj = "获取图片失败！详细原因：" + e.getMessage();
					handler.sendMessage(message);
					
					Log.e(tag, "@@@@@@ 获取登录头像出错！详细信息：" + e.getMessage());
				}	
			}
			
		}.start();
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
			signBusiness.doSignIn(loginName, loginPassword, handler);
		} catch (Exception e) {
			Log.e(tag, "登录发生异常！" + e.getMessage());
			Toast.makeText(this, "登录异常！原因：" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
