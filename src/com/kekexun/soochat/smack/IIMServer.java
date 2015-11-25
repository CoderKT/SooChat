package com.kekexun.soochat.smack;

import java.util.List;

import com.kekexun.soochat.pojo.ChatItem;

public interface IIMServer {
	
	public String getJidPart(String jid, String type);

	public void connect(String username, String password, ConnectionListener connListener) throws Exception;
	
	public List<ChatItem> queryRoster();
	
}
