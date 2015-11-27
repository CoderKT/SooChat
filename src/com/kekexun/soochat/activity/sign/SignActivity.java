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
	 * ���µ�¼�û�ͷ���¼�����
	 */
	private final static int SHOW_LOGIN_HEADER_IMAGE = 0;
	
	private final static int QUERY_LOGIN_HEADER_IMAGE_ERROR = 1;
	
	public final static int CONNECT_TO_IMSERVER_SUCCESS = 2;
	
	public final static int CONNECT_TO_IMSERVER_FAILURE = 3;

	/**
	 * ���߳���Ϣ������
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
					// �����¼��Ϣ
					signBusiness.saveLoginInfo(etLoginName.getText().toString().trim(), etLoginPassword.getText().toString().trim(), true);
					// ��ת����ҳ��
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
		
		// �������״̬
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
		
		// �������״̬
		//checkNetState(LIFTCYCLE_START);
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		ivHeader = (ImageView) this.findViewById(R.id.iv_header);
		etLoginName = (EditText) this.findViewById(R.id.et_login_name);
		etLoginPassword = (EditText) this.findViewById(R.id.et_login_password);
	}
	
	/**
	 * ��ȡ��¼�û���ͷ��
	 */
	private void queryMyHeaderImage(String loginName, String path) {
		final String tmp = "http://a.hiphotos.baidu.com/image/pic/item/aec379310a55b319c3dd7a9e45a98226cffc1730.jpg";
		new Thread() {
			
			@Override
			public void run() {
				try {
					URL url = new URL(tmp);
					HttpURLConnection httpUrlConnection =  (HttpURLConnection) url.openConnection();
					// ��������ķ�ʽ
					httpUrlConnection.setRequestMethod("GET");
					// �������ӳ�ʱʱ��
					httpUrlConnection.setConnectTimeout(5000);
					// ���ö�ȡ��ʱʱ��(��ȡͼƬһ��ʱ��������ߵ��쳣)
					httpUrlConnection.setReadTimeout(10000);
					// ������������
					httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");

					// ��ȡ��Ӧ��
					int responseCode = httpUrlConnection.getResponseCode();
					switch(responseCode) {
						case 200:
							InputStream is = httpUrlConnection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							//ivHeader.setImageBitmap(bitmap);
							//�������߳�һ����Ϣ���ﵱǰ�̸߳���ͷ��ؼ�
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
					message.obj = "��ȡͼƬʧ�ܣ���ϸԭ��" + e.getMessage();
					handler.sendMessage(message);
					
					Log.e(tag, "@@@@@@ ��ȡ��¼ͷ�������ϸ��Ϣ��" + e.getMessage());
				}	
			}
			
		}.start();
	}
	
	/**
	 * ��¼
	 * @param view
	 */
	public void signInAction(View view) {
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
			signBusiness.doSignIn(loginName, loginPassword, handler);
		} catch (Exception e) {
			Log.e(tag, "��¼�����쳣��" + e.getMessage());
			Toast.makeText(this, "��¼�쳣��ԭ��" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
