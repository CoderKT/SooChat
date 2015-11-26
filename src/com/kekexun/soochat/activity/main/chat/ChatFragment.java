package com.kekexun.soochat.activity.main.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kekexun.soochat.activity.BaseFragment;
import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.adapter.main.chat.ChatAdapter;
import com.kekexun.soochat.pojo.ChatItem;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public class ChatFragment extends BaseFragment {
	
	private final static String tag = "ChatFragment";
	
	private View vPanel;
	private ListView lvChatList;
	
	private Context context;
	private List<ChatItem> chatItems = new ArrayList<ChatItem>();
	
	public ChatFragment() {
	}
	
	public ChatFragment(Context context, List<ChatItem> chatItems) {
		this.context = context;
		this.chatItems = chatItems;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		vPanel = inflater.inflate(R.layout.activity_main_chat, container, false);
		
		//
		initViews(inflater);
		
		return vPanel;
	}
	
	/**
	 * Initialization components
	 */
	private void initViews(LayoutInflater layoutInflater) {
		lvChatList = (ListView) vPanel.findViewById(R.id.lvChatList);
		ChatAdapter chatAdapter = new ChatAdapter(chatItems, layoutInflater);
		lvChatList.setAdapter(chatAdapter);
		lvChatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(tag, "parent=" + parent + " view=" + view + " position=" + position + " id=" + id);
				
				ChatItem chatItem = (ChatItem) parent.getItemAtPosition(position);
				
				Intent intent = new Intent(context, ChatWinActivity.class);
				intent.putExtra("userTitle", chatItem.getTitle());
				startActivity(intent);
			}
			
		});
	}

}
