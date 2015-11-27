package com.kekexun.soochat.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import android.content.SharedPreferences;
import android.util.Log;

import com.kekexun.soochat.common.K;
import com.kekexun.soochat.pojo.ChatItem;

/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public class BaseIMServer implements IMServer {
	
	private static final String tag = "BaseIMServer";
	
	/**
	 * 唯一实例
	 */
	private static BaseIMServer instance;
	
	/**
	 * 链接
	 */
	private AbstractXMPPConnection conn;
	
	/**
	 * 花名册
	 */
	private Roster roster;
	
	/**
	 * 会话管理
	 */
	private ChatManager chatManager;
	
	/**
	 * 
	 */
	private SharedPreferences sharedPreferences;
	
	/**
	 * 是否连接到IMServer
	 */
	private boolean isConnected = false;
	
	/**
	 * 花名册列表
	 */
	private List<ChatItem> rosterList = new ArrayList<ChatItem>();

	/**
	 * Construct
	 * @param sharedPreferences
	 */
	private BaseIMServer() {
	}
	
	/**
	 * 花名册监听对象
	 */
	private RosterListener rosterListener = new RosterListener() {
		
		@Override
		public void presenceChanged(Presence presence) {
			Log.d(tag, "------ BaseIMServer.rosterListener.presenceChanged()");
			Type type = presence.getType();
		}
		
		@Override
		public void entriesUpdated(Collection<String> addresses) {
			Log.d(tag, "------ BaseIMServer.rosterListener.entriesUpdated()");
		}
		
		@Override
		public void entriesDeleted(Collection<String> addresses) {
			Log.d(tag, "------ BaseIMServer.rosterListener.entriesDeleted()");
		}
		
		@Override
		public void entriesAdded(Collection<String> addresses) {
			Log.d(tag, "------ BaseIMServer.rosterListener.entriesAdded()");
		}
		
	};
	
	/**
	 * 会话监听器
	 */
	private ChatManagerListener chatManagerListener = new ChatManagerListener() {

		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			if (!createdLocally) {
				//TODO chat.addMessageListener(new MyNewMessageListener());
			}

		}
		
	};
	
	/**
	 * 
	 */
	private void initRoster() {
		if (getConn() == null || !getConn().isConnected()) {
			Log.d(tag, "------ BaseIMServer.queryRoster() 连接异常：conn=" + getConn());
			return;
		}
		Log.d(tag, "------ BaseIMServer.initRoster() 准备初始化花名册：roster=" + roster);
		Collection<RosterEntry> entries = roster.getEntries();
		Log.d(tag, "------ BaseIMServer.initRoster() 准备初始化花名册：entries=" + entries);
		for (RosterEntry entry : entries) {
			String jid = entry.getUser();
			String name = entry.getName() != null ? entry.getName() : getJidPart(jid, "100");
			//ItemStatus itemStatus = entry.getStatus();
			//ItemType itemType = entry.getType();
			//List<RosterGroup> groups = entry.getGroups();
			
			ChatItem chatItem = new ChatItem("ID-" + name, "icon", name, "用户的 JID 是: " + jid);
			rosterList.add(chatItem);
		}
		Log.d(tag, "------ BaseIMServer.initRoster() 完成花名册初始化：rosterList=" + rosterList);
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static BaseIMServer getInstance() {
		if (instance == null) {
			Log.d(tag, "------ 新创建 BaseIMServer 实例");
			instance = new BaseIMServer();
			return instance;
		}
		Log.d(tag, "------ 返回已经存在的 BaseIMServer 实例");
		return instance;
	}
	
	/**
	 * 获取 JID 指定的部分 
	 */
	@Override
	public String getJidPart(String jid, String type) {
		if (type == "100") {
			return jid.substring(0, jid.indexOf("@"));
		} else if (type == "010") {
			return jid.substring(jid.indexOf("@"), jid.lastIndexOf("\\/"));
		} else if (type == "001") {
			return jid.substring(jid.lastIndexOf("\\/"));
		} else if (type == "110") {
			return jid.substring(jid.lastIndexOf("\\/"));
		}
		
		return null;
	}

	/**
	 * 连接的 IMServer 服务器
	 */
	@Override
	public boolean connect(String username, String password) throws Exception {
		if (getConn() != null) {
			Log.d(tag, "------ BaseIMServer.connect() conn=null, return false");
			return false;
		}
		
		// XMPP service (i.e., the XMPP domain)
		String serviceName = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "192.168.9.105");
		// 资源
		String resource = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "SooChat");
		// 端口
		int port = sharedPreferences.getInt(K.PreferenceKey.KEY_XMPP_SERVER_PORT, 5222);
		
		XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
		configBuilder.setUsernameAndPassword(username, password)
					 .setServiceName(serviceName)
					 .setPort(port)
					 .setResource(resource)
					 .setSecurityMode(SecurityMode.disabled);
		
		SASLAuthentication.registerSASLMechanism(new SASLMechanism() {
			
			@Override
			protected SASLMechanism newInstance() {
				return this;
			}
			
			@Override
			public int getPriority() {
				return 0;
			}
			
			@Override
			public String getName() {
				return null;
			}
			
			@Override
			protected byte[] getAuthenticationText() throws SmackException {
				return null;
			}
			
			@Override
			public void checkIfSuccessfulOrThrow() throws SmackException {
				
			}
			
			@Override
			protected void authenticateInternal(CallbackHandler cbh) throws SmackException {
				
			}
		});
		
		try {
			conn = new XMPPTCPConnection(configBuilder.build());
			// 连接
			conn.connect();
			
			// TODO 发送获取花名册节
			
			// 添加花名册监听器
			roster = Roster.getInstanceFor(getConn());
			roster.addRosterListener(rosterListener);
			
			// 添加消息监听器
			chatManager = ChatManager.getInstanceFor(getConn());
			chatManager.addChatListener(chatManagerListener);
			
			// 登录
			conn.login();
			
			if (conn != null && conn.isConnected()) { // TODO 异常情况未考虑完整
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "@@@@@@ 登录到 IMServer 出错，详细原因：" + e.getMessage());
			return false;
		}
		
		return false;
	}
	
	/**
	 * 获取花名册
	 * @param conn
	 * @return
	 */
	@Override
	public List<ChatItem> queryRoster() {
		return rosterList;
	}
	
	/**
	 * 设置 SharedPreferences
	 * @param sharedPreferences
	 */
	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}
	
	/**
	 * 获取链接
	 * @return
	 */
	public XMPPConnection getConn() {
		return conn;
	}
	
	public synchronized boolean isConnected() {
		return isConnected;
	}

	public synchronized void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
}
