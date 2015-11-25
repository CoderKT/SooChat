package com.kekexun.soochat.activity.main.chat;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	
	private View vPanel;
	private ListView lvChatList;
	private List<ChatItem> chatItems = new ArrayList<ChatItem>();
	
	public ChatFragment() {
	}
	
	public ChatFragment(List<ChatItem> chatItems) {
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
		
	}

}
