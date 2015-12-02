package com.kekexun.soochat.smack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jivesoftware.smack.packet.IQ;

public class Time extends IQ {
	
	private Date utc;
    private TimeZone timeZone;
    private String display;
    
    protected Time(String childElementName) {
		super(childElementName);
	}
    
    public void setUtc(String utcString) {
        try {
          utc = SimpleDateFormat.getInstance().parse(utcString);
        } catch (Exception e) {
        }
     }

     public void setTimeZone(String zone) {
        timeZone = TimeZone.getTimeZone(zone);
     }

     public void setDisplay(String timeDisplay) {
        display = timeDisplay;
     }

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
		return null;
	}

}
