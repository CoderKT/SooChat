package com.kekexun.soochat.smack;

import java.util.List;

import com.kekexun.soochat.pojo.ChatItem;

public interface IMServer {
	
	public String getJidPart(String jid, String type);

	public boolean connect(String username, String password) throws Exception;
	
	public List<ChatItem> queryRoster() throws Exception;
	
}
