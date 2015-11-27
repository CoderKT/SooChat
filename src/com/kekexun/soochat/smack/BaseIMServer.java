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
	 * Ψһʵ��
	 */
	private static BaseIMServer instance;
	
	/**
	 * ����
	 */
	private AbstractXMPPConnection conn;
	
	/**
	 * ������
	 */
	private Roster roster;
	
	/**
	 * �Ự����
	 */
	private ChatManager chatManager;
	
	/**
	 * 
	 */
	private SharedPreferences sharedPreferences;
	
	/**
	 * �Ƿ����ӵ�IMServer
	 */
	private boolean isConnected = false;
	
	/**
	 * �������б�
	 */
	private List<ChatItem> rosterList = new ArrayList<ChatItem>();

	/**
	 * Construct
	 * @param sharedPreferences
	 */
	private BaseIMServer() {
	}
	
	/**
	 * �������������
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
	 * �Ự������
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
			Log.d(tag, "------ BaseIMServer.queryRoster() �����쳣��conn=" + getConn());
			return;
		}
		Log.d(tag, "------ BaseIMServer.initRoster() ׼����ʼ�������᣺roster=" + roster);
		Collection<RosterEntry> entries = roster.getEntries();
		Log.d(tag, "------ BaseIMServer.initRoster() ׼����ʼ�������᣺entries=" + entries);
		for (RosterEntry entry : entries) {
			String jid = entry.getUser();
			String name = entry.getName() != null ? entry.getName() : getJidPart(jid, "100");
			//ItemStatus itemStatus = entry.getStatus();
			//ItemType itemType = entry.getType();
			//List<RosterGroup> groups = entry.getGroups();
			
			ChatItem chatItem = new ChatItem("ID-" + name, "icon", name, "�û��� JID ��: " + jid);
			rosterList.add(chatItem);
		}
		Log.d(tag, "------ BaseIMServer.initRoster() ��ɻ������ʼ����rosterList=" + rosterList);
	}
	
	/**
	 * ��ȡʵ��
	 * @return
	 */
	public static BaseIMServer getInstance() {
		if (instance == null) {
			Log.d(tag, "------ �´��� BaseIMServer ʵ��");
			instance = new BaseIMServer();
			return instance;
		}
		Log.d(tag, "------ �����Ѿ����ڵ� BaseIMServer ʵ��");
		return instance;
	}
	
	/**
	 * ��ȡ JID ָ���Ĳ��� 
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
	 * ���ӵ� IMServer ������
	 */
	@Override
	public boolean connect(String username, String password) throws Exception {
		if (getConn() != null) {
			Log.d(tag, "------ BaseIMServer.connect() conn=null, return false");
			return false;
		}
		
		// XMPP service (i.e., the XMPP domain)
		String serviceName = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "192.168.9.105");
		// ��Դ
		String resource = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "SooChat");
		// �˿�
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
			// ����
			conn.connect();
			
			// TODO ���ͻ�ȡ�������
			
			// ��ӻ����������
			roster = Roster.getInstanceFor(getConn());
			roster.addRosterListener(rosterListener);
			
			// �����Ϣ������
			chatManager = ChatManager.getInstanceFor(getConn());
			chatManager.addChatListener(chatManagerListener);
			
			// ��¼
			conn.login();
			
			if (conn != null && conn.isConnected()) { // TODO �쳣���δ��������
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "@@@@@@ ��¼�� IMServer ������ϸԭ��" + e.getMessage());
			return false;
		}
		
		return false;
	}
	
	/**
	 * ��ȡ������
	 * @param conn
	 * @return
	 */
	@Override
	public List<ChatItem> queryRoster() {
		return rosterList;
	}
	
	/**
	 * ���� SharedPreferences
	 * @param sharedPreferences
	 */
	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}
	
	/**
	 * ��ȡ����
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
