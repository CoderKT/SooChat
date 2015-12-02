package com.kekexun.soochat.smack;

import org.jivesoftware.smack.packet.IQ;

public class CustomIQ extends IQ {

	/**
	 * 
	 * @param childElementName
	 * @param childElementNamespace
	 */
	public CustomIQ(String childElementName, String childElementNamespace) {
		super(childElementName, childElementNamespace);
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
		 xml.append(">"); //TODO why
		return xml;
	}
	
}
