package com.kekexun.soochat.smack;

import java.util.Collection;

import javax.security.auth.callback.CallbackHandler;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class IMServer extends SASLMechanism {

	public void login(String username, String password) throws Exception {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
				.builder()
				.setUsernameAndPassword(username, password)
				.setServiceName("192.168.9.108")
				.setHost("192.168.9.108")
				.setPort(5222)
				.setResource("Android")
				.setSecurityMode(SecurityMode.disabled)
				.build();

		SASLAuthentication.registerSASLMechanism(this);

		AbstractXMPPConnection conn = new XMPPTCPConnection(config);
		conn.connect();

		conn.login();
		
		// Create a new presence. Pass in false to indicate we're unavailable._
		Presence presence = new Presence(Presence.Type.available);
		//presence.setStatus("Gone fishing");
		// Send the packet (assume we have an XMPPConnection instance called "con").
		conn.sendStanza(presence);
		
		Roster roster = Roster.getInstanceFor(conn);
		Collection<RosterEntry> entries = roster.getEntries();
		for (RosterEntry entry: entries) {
			System.out.println("------ entry: " + entry);
		}
	}

	@Override
	protected void authenticateInternal(CallbackHandler arg0) throws SmackException {
		
	}

	@Override
	public void checkIfSuccessfulOrThrow() throws SmackException {
		
	}

	@Override
	protected byte[] getAuthenticationText() throws SmackException {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	protected SASLMechanism newInstance() {
		return this;
	}
	
}
