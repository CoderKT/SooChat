package com.kekexun.soochat.smack;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CustomIQProvider extends IQProvider<CustomIQ> {
	
	private CustomIQ myIQ;

	public CustomIQProvider(CustomIQ myIQ) {
		this.myIQ = myIQ;
	}
	
	@Override
	public CustomIQ parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
		return myIQ;
	}

}
