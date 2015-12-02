package com.kekexun.soochat.smack;

import org.jivesoftware.smack.provider.IntrospectionProvider.IQIntrospectionProvider;

public class TimeProvider extends IQIntrospectionProvider<Time> {

	protected TimeProvider(Class<Time> elementClass) {
		super(elementClass);
	}

}
