package com.kekexun.soochat.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.packet.Registration;
import org.jivesoftware.smackx.xroster.provider.RosterExchangeProvider;

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
			System.out.println("------" + type);
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
	 * 注册
	 * @param account
	 * @param password
	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
	 */
	public String register(String account, String password) {// TODO 
		// 设置属性
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("name", "soosky");
		attributes.put("first", "wang");
		attributes.put("last", "ke");
		attributes.put("email", "soosky@163.com");
		attributes.put("city", "xi'an");
		attributes.put("state", "0"); // the user's state
		attributes.put("zip", "71000");
		attributes.put("phone", "15091545831");
		attributes.put("url", "www.kekexun.com");
		attributes.put("misc", ""); // other miscellaneous information to associate with the account.
		attributes.put("text", "wk"); // textual information to associate with the account.
		//attributes.put("remove", ""); //empty flag to remove account.
		
	    Registration reg = new Registration(attributes);  
	    reg.setType(IQ.Type.set); 
	    reg.setTo("192.168.9.111");
	    
	    /*PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));  
	    PacketCollector collector = getConn().createPacketCollector(filter);  
	    getConn().sendStanza(reg);
	    
	    IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());  
	    // Stop queuing results  
	    collector.cancel();// 停止请求results（是否成功的结果）  
	    if (result == null) {  
	        Log.e("RegistActivity", "No response from server.");  
	        return "0";  
	    } else if (result.getType() == IQ.Type.result) {  
	        return "1";  
	    } else { // if (result.getType() == IQ.Type.ERROR)  
	        if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {  
	            Log.e("RegistActivity", "IQ.Type.ERROR: " + result.getError().toString());  
	            return "2";  
	        } else {  
	            Log.e("RegistActivity", "IQ.Type.ERROR: " + result.getError().toString());  
	            return "3";  
	        }  
	    }*/
	    return "0";
	}
	
	private boolean openConnection() throws Exception {
		if (null == conn || !conn.isAuthenticated()) {  
			//XMPPConnection.DEBUG_ENABLED = true;// 开启DEBUG模式  
			// 配置连接  
			/*ConnectionConfiguration config = new ConnectionConfiguration(SERVER_HOST, SERVER_PORT, SERVER_NAME);  
			config.setReconnectionAllowed(true);  
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);  
			config.setSendPresence(true); // 状态设为离线，目的为了取离线消息  
			config.setSASLAuthenticationEnabled(false); // 是否启用安全验证  
			config.setTruststorePath("/system/etc/security/cacerts.bks");  
			config.setTruststorePassword("changeit");  
			config.setTruststoreType("bks");  
			connection = new XMPPConnection(config);  
			connection.connect();// 连接到服务器  
			// 配置各种Provider，如果不配置，则会无法解析数据  
			configureConnection(ProviderManager.getInstance());  */
			return true;  
		}  

        return false;  
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
		String serviceName = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "192.168.43.228");
		// 资源
		String resource = sharedPreferences.getString(K.PreferenceKey.KEY_XMPP_RESOURCE, "SooChat");
		// 端口
		int port = sharedPreferences.getInt(K.PreferenceKey.KEY_XMPP_SERVER_PORT, 5222);
		
		XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
		configBuilder.setDebuggerEnabled(true)
					 .setUsernameAndPassword(username, password)
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
			/*CustomIQ myIQ = new CustomIQ("query", "jabber:iq:roster");
			myIQ.setType(IQ.Type.get); //or use instead SET
			String jid = "user2";
			myIQ.setTo(jid);

			CustomIQProvider provider = new CustomIQProvider(myIQ); //This will create a customIQ again. Possib
			ProviderManager.addIQProvider("query", "myxmlns", provider);
			conn.sendStanza(myIQ);
			
			ProviderManager.addExtensionProvider("presence", "jabber:iq:roster", new RosterExchangeProvider());*/
			
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
	 * 更改用户状态 
	 */  
	public void setPresence(int code) throws Exception {
		Presence presence;  
    	switch (code) {
        case 0:  
            presence = new Presence(Presence.Type.available);  
            getConn().sendStanza(presence);  
            Log.v("state", "设置在线");  
            break;  
        case 1:  
            presence = new Presence(Presence.Type.available);  
            presence.setMode(Presence.Mode.chat);  
            getConn().sendStanza(presence);  
            Log.v("state", "设置Q我吧");  
            System.out.println(presence.toXML());  
            break;  
        case 2:  
            presence = new Presence(Presence.Type.available);  
            presence.setMode(Presence.Mode.dnd);  
            getConn().sendStanza(presence);  
            Log.v("state", "设置忙碌");  
            System.out.println(presence.toXML());  
            break;  
        case 3:  
            presence = new Presence(Presence.Type.available);  
            presence.setMode(Presence.Mode.away);  
            getConn().sendStanza(presence);  
            Log.v("state", "设置离开");  
            System.out.println(presence.toXML());  
            break;  
        case 4:  
            Collection<RosterEntry> entries = roster.getEntries();  
            for (RosterEntry entry : entries) {  
                presence = new Presence(Presence.Type.unavailable);  
                //presence.setStanzaID(Packet.ID_NOT_AVAILABLE);  
                presence.setFrom(getConn().getUser());  
                presence.setTo(entry.getUser());  
                getConn().sendStanza(presence);  
                System.out.println(presence.toXML());  
            }  
            // 向同一用户的其他客户端发送隐身状态  
            presence = new Presence(Presence.Type.unavailable);  
            //presence.setStanzaID(Packet.ID_NOT_AVAILABLE);  
            presence.setFrom(getConn().getUser());  
            //presence.setTo(StringUtils.parseBareAddress(getConn().getUser()));  
            getConn().sendStanza(presence);  
            Log.v("state", "设置隐身");  
            break;  
        case 5:  
            presence = new Presence(Presence.Type.unavailable);  
            getConn().sendStanza(presence);  
            Log.v("state", "设置离线");  
            break;
        default:  
        	break;
        }  
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
	
}
