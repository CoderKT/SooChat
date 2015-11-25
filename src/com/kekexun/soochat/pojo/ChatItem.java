package com.kekexun.soochat.pojo;

import java.io.Serializable;

public class ChatItem extends BasePojo implements Serializable {

	private static final long serialVersionUID = -441994848867283491L;
	
	private String id;
	private String icon;
	private String title;
	private String desc;
	private String time;
	
	public ChatItem() {
	}
	
	public ChatItem(String id, String icon, String title) {
		this.id = id;
		this.icon = icon;
		this.title = title;
	}
	
	public ChatItem(String id, String icon, String title, String desc) {
		this.id = id;
		this.icon = icon;
		this.title = title;
		this.desc = desc;
	}
	
	public ChatItem(String id, String icon, String title, String desc, String time) {
		this.id = id;
		this.icon = icon;
		this.title = title;
		this.desc = desc;
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
