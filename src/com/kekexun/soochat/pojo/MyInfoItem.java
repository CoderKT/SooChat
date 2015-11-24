package com.kekexun.soochat.pojo;

public class MyInfoItem {

	private String name;
	private String icon;
	
	public MyInfoItem(String name) {
		this.name = name;
	}
	
	public MyInfoItem(String name, String icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
