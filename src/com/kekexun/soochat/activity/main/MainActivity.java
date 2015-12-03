package com.kekexun.soochat.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kekexun.soochat.activity.main.myself.MyselfFragment;
import com.kekexun.soochat.activity.main.myself.MyselfSettingActivity;
import com.kekexun.soochat.activity.sign.SignActivity;
import com.kekexun.soochat.business.sign.impl.SignBusiness;
import com.kekexun.soochat.pojo.ChatItem;
import com.kekexun.soochat.pojo.MyInfoItem;
import com.kekexun.soochat.smack.BaseIMServer;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
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
	private MyselfFragment myselfFragment;
	
	private FragmentManager fragmentManager;
	
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedPreferences = getSharedPreferences("prefenrences", Context.MODE_PRIVATE);
		
		// 初始化 Views
		try {
			initViews();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "出错了，详细原因：" + e.getMessage());
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 选择菜单事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		switch(itemId) {
			case R.id.system_setting: 
				Toast.makeText(this, "你选择了系统设置！", Toast.LENGTH_SHORT).show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化 Views
	 */
	private void initViews() throws Exception {
		vpMain = (ViewPager) this.findViewById(R.id.vpMain);
		
		btnChat = (Button) this.findViewById(R.id.btnChat);
		btnAddrBook = (Button) this.findViewById(R.id.btnAddrBook);
		btnDiscovery = (Button) this.findViewById(R.id.btnDiscovery);
		btnMyself = (Button) this.findViewById(R.id.btnMyself);
		
		// Initialize chat panel
		chatFragment = initChatPanel();
		// Initialize addrBook panel
		addrBookFragment = initAddrBookPanel();
		// Initialize discovery panel
		discoveryFragment = initDiscoveryPanel();
		// Initialize myself panel
		myselfFragment = initMyselfPanel();
		
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
				btnChat.setTextColor(Color.parseColor("#CCCCCC"));
				btnAddrBook.setTextColor(Color.parseColor("#CCCCCC"));
				btnDiscovery.setTextColor(Color.parseColor("#CCCCCC"));
				btnMyself.setTextColor(Color.parseColor("#CCCCCC"));
				
				switch(item) {
					case 0:
						btnChat.setTextColor(Color.BLUE);
					break;
					case 1:
						btnAddrBook.setTextColor(Color.BLUE);
					break;
					case 2:
						btnDiscovery.setTextColor(Color.BLUE);
					break;
					case 3:
						btnMyself.setTextColor(Color.BLUE);
					break;
				}
			}
			
		});
	}
	
	/**
	 * Initialize chat panel
	 */
	private ChatFragment initChatPanel() throws Exception {
		BaseIMServer imServer = BaseIMServer.getInstance();
		imServer.setSharedPreferences(sharedPreferences);
		List<ChatItem> chatItems = imServer.queryRoster();
		if (chatItems == null) { // TODO
			Intent intent = new Intent(this, SignActivity.class);
			startActivity(intent);
			this.finish();
		}
		chatFragment = new ChatFragment(this, chatItems);
		return chatFragment;
	}
	
	/**
	 * Initialize address book panel
	 */
	private AddrBookFragment initAddrBookPanel() {
		addrBookFragment = new AddrBookFragment();
		return addrBookFragment;
	}
	
	/**
	 * Initialize discovery panel
	 * @return
	 */
	private DiscoveryFragment initDiscoveryPanel() {
		discoveryFragment = new DiscoveryFragment();
		return discoveryFragment;
	}
	
	/**
	 * Initialize myself panel
	 * @return
	 */
	private MyselfFragment initMyselfPanel() {
		myselfFragment = new MyselfFragment();
		return myselfFragment;
	}
	
	/**
	 * 设置选择的 tab 页
	 * @param v
	 */
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
	 * 内部类
	 * List 适配器
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
		
		MyInfoItem myInfoItem0 = new MyInfoItem("投诉一部", "myself_dept.png");		
		MyInfoItem myInfoItem1 = new MyInfoItem("投诉组长");
		MyInfoItem myInfoItem2 = new MyInfoItem("18891545830");
		MyInfoItem myInfoItem3 = new MyInfoItem("soosky@163.com");
		
		MyInfoItem myInfoItem4 = new MyInfoItem("在线升级");
		
		myInfos.add(myInfoItem0);
		myInfos.add(myInfoItem1);
		myInfos.add(myInfoItem2);
		myInfos.add(myInfoItem3);
		myInfos.add(myInfoItem4);
		
		return myInfos;
	}
	
	/**
	 * Quit
	 * @param view
	 */
	public void quit(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("提示");
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SignBusiness loginBusiness = new SignBusiness(MainActivity.this);
				loginBusiness.saveLoginInfo("", "", false);
				MainActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
			
		});
		
		builder.create().show();
	}

}
