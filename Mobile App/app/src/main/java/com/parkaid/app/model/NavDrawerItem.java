package com.parkaid.app.model;

public class NavDrawerItem {
	
	private String title;	
	public NavDrawerItem(){}

	public NavDrawerItem(String title){
		this.title = title;
	}
	
	public NavDrawerItem(String title, boolean isCounterVisible, String count){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
}
