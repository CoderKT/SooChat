package com.kekexun.soochat.adapter.main.chat;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kekexun.soochat.activity.R;
import com.kekexun.soochat.pojo.ChatItem;

public class ChatAdapter extends BaseAdapter {
	
	private List<ChatItem> chatItems = new ArrayList<ChatItem>();
	private LayoutInflater layoutInflater;
	

	public ChatAdapter(List<ChatItem> chatItems, LayoutInflater layoutInflater) {
		this.chatItems = chatItems;
		this.layoutInflater = layoutInflater;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return chatItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatItem chatItem = chatItems.get(position);
		
		View itemView = layoutInflater.inflate(R.layout.activity_main_chat_item, null);

		//ImageView ivIcon = (ImageView) itemView.findViewById(R.id.ivChatItemIcon);
		TextView tvTitle = (TextView) itemView.findViewById(R.id.tvChatItemTitle);
		TextView tvDesc = (TextView) itemView.findViewById(R.id.tvChatItemDesc);
		
		tvTitle.setText(chatItem.getTitle());
		tvDesc.setText(chatItem.getDesc());
		
		return itemView;
	}

	@Override
	public int getCount() {
		return chatItems.size();
	}

}
