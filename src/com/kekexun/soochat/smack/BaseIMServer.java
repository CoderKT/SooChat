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
public class BaseIMServer implements IIMServer {
	
	private static final String tag = "BaseIMServer";
	
	private static AbstractXMPPConnection conn;
	private SharedPreferences sharedPreferences;
	
	public BaseIMServer(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
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


	@Override
	public void connect(final String username, final String password, final ConnectionListener connListener) throws Exception {
		Log.d(tag, "------ 1 BaseIMServer.connect()");
		
		if (getConn() != null) {
			return;
		}
		
		new Thread() {

			@Override
			public void run() {
				Log.d(tag, "------ 2 BaseIMServer@new Thread().run() begin");
				// XMPP service (i.e., the XMPP domain)
				String serviceName = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "192.168.9.107");
				// 资源
				String resource = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "SooChat");
				int port = sharedPreferences.getInt(K.PreferenceKey.KEY_XMPP_SERVER_PORT, 5222);
				
				XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
				configBuilder.setUsernameAndPassword(username, password)
							 .setServiceName(serviceName)
							 .setHost(serviceName)
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
				
				conn = new XMPPTCPConnection(configBuilder.build());
				
				try {
					conn.connect();
					conn.login();
					Log.d(tag, "------ 2.1 BaseIMServer@new Thread().run() conn=" + conn);
					connListener.onSuccess();
				} catch (Exception e) {
					Log.e(tag, "@@@@@@" + e.getMessage());
					connListener.onFailure("@@@@@@" + e.getMessage());
				}
				
				Log.d(tag, "------ 2.2 BaseIMServer new Thread().run() end");
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
		Log.d(tag, "------ 4 BaseIMServer queryMyRoster()");
		List<ChatItem> chatItems = new ArrayList<ChatItem>();
		if (getConn() == null) {
			Log.d(tag, "------ 4.1 BaseIMServer conn is " + getConn());
			return chatItems;
		}
		Log.d(tag, "------ 4.2 BaseIMServer queryRoster() conn=" + getConn() + " isConnected=" + getConn().isConnected());
		Roster roster = Roster.getInstanceFor(getConn());
		Log.d(tag, "------ 4.3 BaseIMServer queryRoster() roster=" + roster);
		Collection<RosterEntry> entries = roster.getEntries();
		Log.d(tag, "------ 4.4 BaseIMServer queryRoster() entries=" + entries);
		for (RosterEntry entry : entries) {
			String jid = entry.getUser();
			String name = entry.getName() != null ? entry.getName() : getJidPart(jid, "100");
			//ItemStatus itemStatus = entry.getStatus();
			//ItemType itemType = entry.getType();
			//List<RosterGroup> groups = entry.getGroups();
			
			ChatItem chatItem = new ChatItem("ID-" + name, "icon", name, "用户的 JID 是: " + jid);
			chatItems.add(chatItem);
		}
		Log.d(tag, "------ 4.5 BaseIMServer queryRoster() chatItems=" + chatItems);
		return chatItems;
	}
	
	/**
	 * 获取链接
	 * @return
	 */
	public synchronized static XMPPConnection getConn() {
		return conn;
	}
	
}
