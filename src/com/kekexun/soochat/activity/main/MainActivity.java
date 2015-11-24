package com.kekexun.soochat.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.activity.main.addrbook.AddrBookFragment;
import com.kekexun.soochat.activity.main.chat.ChatFragment;
import com.kekexun.soochat.activity.main.discover.DiscoveryFragment;
import com.kekexun.soochat.activity.main.myself.MySelfFragment;
import com.kekexun.soochat.activity.main.myself.MyselfSettingActivity;
import com.kekexun.soochat.business.sign.impl.SignBusiness;
import com.kekexun.soochat.pojo.MyInfoItem;

/**
 * 
 * @author Ke.Wang
 *
 */
public class MainActivity extends FragmentActivity {
	
	private static final String tag = "MainActivity";
	
	private LayoutInflater layoutInflater;
	 
	private ViewPager vpMain;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	private Button btnChat;
	private Button btnAddrBook;
	private Button btnDiscovery;
	private Button btnMyself;
	
	private List<MyInfoItem> myInfos = new ArrayList<MyInfoItem>();
	
	private ChatFragment chatFragment;
	private AddrBookFragment addrBookFragment;
	private DiscoveryFragment discoveryFragment;
	private MySelfFragment myselfFragment;
	
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ��ʼ�� Views
		initViews();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * ѡ��˵��¼�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		switch(itemId) {
			case R.id.system_setting: 
				Toast.makeText(this, "��ѡ����ϵͳ���ã�", Toast.LENGTH_SHORT).show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʼ�� Views
	 */
	private void initViews() {
		vpMain = (ViewPager) this.findViewById(R.id.vpMain);
		
		btnChat = (Button) this.findViewById(R.id.btnChat);
		btnAddrBook = (Button) this.findViewById(R.id.btnAddrBook);
		btnDiscovery = (Button) this.findViewById(R.id.btnDiscovery);
		btnMyself = (Button) this.findViewById(R.id.btnMyself);
		
		chatFragment = new ChatFragment();
		addrBookFragment = new AddrBookFragment();
		discoveryFragment = new DiscoveryFragment();
		myselfFragment = new MySelfFragment();
		
		fragments.add(chatFragment);
		fragments.add(addrBookFragment);
		fragments.add(discoveryFragment);
		fragments.add(myselfFragment);
		
		fragmentManager = this.getSupportFragmentManager();
		MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragments);
		vpMain.setAdapter(myFragmentPagerAdapter);
		vpMain.setCurrentItem(0);
		vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int item) {
				switch(item) {
					case 0:
						btnChat.setTextColor(Color.BLUE);
					break;
					case 1:
						btnAddrBook.setTextColor(Color.BLUE);
					break;
					case 2:
						btnDiscovery.setTextColor(Color.RED);
					break;
					case 3:
						btnMyself.setTextColor(Color.RED);
					break;
				}
			}
			
		});
	}
	
	public void setPage(View v) {
		switch(v.getId()) {
			case R.id.btnChat: 
				vpMain.setCurrentItem(0);
			break;
			case R.id.btnAddrBook: 
				vpMain.setCurrentItem(1);
			break;
			case R.id.btnDiscovery: 
				vpMain.setCurrentItem(2);
			break;
			case R.id.btnMyself: 
				vpMain.setCurrentItem(3);
			break;
		}
	}
	
	/**
	 * 
	 * @param view
	 */
	public void openMyselfSettings(View view) {
		Intent intent = new Intent(this, MyselfSettingActivity.class);
		int requestCode = 0;
		this.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * �ڲ���
	 * List ������
	 *
	 */
	private class MyInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return myInfos != null ? myInfos.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return myInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyInfoItem myInfoItem = myInfos.get(position);
			
			View myinfoItemView = layoutInflater.inflate(R.layout.activity_main_myself_myinfo_item, null);

			ImageView ivIcon = (ImageView) myinfoItemView.findViewById(R.id.ivIcon);
			if (myInfoItem.getIcon() != null && myInfoItem.getIcon().length() > 0) {
				String fileName = MainActivity.this.getCacheDir() + "/" + myInfoItem.getIcon();
				Log.i(tag, "fileName=" + fileName);
				Bitmap bm = BitmapFactory.decodeFile(fileName); 
				ivIcon.setImageBitmap(bm);
			}
			
			TextView tvName = (TextView) myinfoItemView.findViewById(R.id.tvName);
			tvName.setText(myInfoItem.getName());
			
			return myinfoItemView;
		}
		
	}
	
	private List<MyInfoItem> qryMyInfo() {
		List<MyInfoItem> myInfos = new ArrayList<MyInfoItem>();
		
		MyInfoItem myInfoItem0 = new MyInfoItem("Ͷ��һ��", "myself_dept.png");		
		MyInfoItem myInfoItem1 = new MyInfoItem("Ͷ���鳤");
		MyInfoItem myInfoItem2 = new MyInfoItem("18891545830");
		MyInfoItem myInfoItem3 = new MyInfoItem("soosky@163.com");
		
		MyInfoItem myInfoItem4 = new MyInfoItem("��������");
		
		myInfos.add(myInfoItem0);
		myInfos.add(myInfoItem1);
		myInfos.add(myInfoItem2);
		myInfos.add(myInfoItem3);
		myInfos.add(myInfoItem4);
		
		return myInfos;
	}
	
	public void quit(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("��ʾ");
		builder.setMessage("ȷ��Ҫ�˳���");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SignBusiness loginBusiness = new SignBusiness(MainActivity.this);
				loginBusiness.saveLoginInfo("", "", false);
				MainActivity.this.finish();
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
			
		});
		
		builder.create().show();
	}

}