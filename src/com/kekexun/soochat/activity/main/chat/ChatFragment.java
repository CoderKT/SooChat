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

public class ChatFragment extends BaseFragment {
	
	private View vPanel;
	private ListView lvChatList;
	
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
		
		List<ChatItem> items = new ArrayList<ChatItem>();
		for (int i = 0; i < 5; i++) {
			ChatItem item = new ChatItem(i, "icon_" + i, "title_" + i, "ÕâÊÇÄÚÈÝÃèÊö_" + i);
			items.add(item);
		}
		
		ChatAdapter chatAdapter = new ChatAdapter(items, layoutInflater);
		lvChatList.setAdapter(chatAdapter);
	}

}
