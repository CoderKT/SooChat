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
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
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
	 * 
	 */
	private SharedPreferences sharedPreferences;
	
	/**
	 * 是否连接到IMServer
	 */
	private boolean isConnected = false;

	/**
	 * Construct
	 * @param sharedPreferences
	 */
	private BaseIMServer() {
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
	public void connect(final String username, final String password, final ConnectionListener connListener) throws Exception {
		Log.d(tag, "------ 1 BaseIMServer.connect()");
		
		if (getConn() != null) {
			return;
		}
		
		new Thread() {

			@Override
			public void run() {
				Log.d(tag, "------ 2 BaseIMServer#new Thread().run() begin");
				
				// XMPP service (i.e., the XMPP domain)
				String serviceName = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "192.168.9.120");
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
					setConnected(true);
					conn.connect();
					conn.login();
					Log.d(tag, "------ 2.1 BaseIMServer#new Thread().run() conn=" + conn);
					connListener.onSuccess();
				} catch (Exception e) {
					setConnected(false);
					Log.e(tag, "@@@@@@ BaseIMServer#new Thread().run() " + e.getMessage());
					connListener.onFailure(e.getMessage());
				}
				
				Log.d(tag, "------ 2.2 BaseIMServer#new Thread().run() end");
			}
			
		}.start();
	}
	
	/**
	 * 获取花名册
	 * @param conn
	 * @return
	 */
	@Override
	public List<ChatItem> queryRoster() {
		Log.d(tag, "------ 4 BaseIMServer.queryMyRoster()");
		List<ChatItem> chatItems = new ArrayList<ChatItem>();
		if (getConn() == null || !getConn().isConnected()) { // TODO conn is null or not connected need to handle.
			Log.d(tag, "------ 4.1 BaseIMServer.queryRoster() conn=" + getConn());
			if (getConn() != null) {
				Log.d(tag, "------ 4.2 BaseIMServer.queryRoster() isConnected=" + getConn().isConnected());	
			}
			Log.d(tag, "------ 4.3 BaseIMServer.queryRoster() return");
			return chatItems;
		}
		Log.d(tag, "------ 4.4 BaseIMServer.queryRoster() conn=" + getConn() + " isConnected=" + getConn().isConnected());
		Roster roster = Roster.getInstanceFor(getConn());
		Log.d(tag, "------ 4.5 BaseIMServer.queryRoster() roster=" + roster);
		Collection<RosterEntry> entries = roster.getEntries();
		Log.d(tag, "------ 4.6 BaseIMServer.queryRoster() entries=" + entries);
		for (RosterEntry entry : entries) {
			String jid = entry.getUser();
			String name = entry.getName() != null ? entry.getName() : getJidPart(jid, "100");
			//ItemStatus itemStatus = entry.getStatus();
			//ItemType itemType = entry.getType();
			//List<RosterGroup> groups = entry.getGroups();
			
			ChatItem chatItem = new ChatItem("ID-" + name, "icon", name, "用户的 JID 是: " + jid);
			chatItems.add(chatItem);
		}
		Log.d(tag, "------ 4.7 BaseIMServer.queryRoster() chatItems=" + chatItems);
		return chatItems;
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
