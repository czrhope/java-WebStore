package com.czr.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	
	private Map<String,CartItem> map = new HashMap<String,CartItem>();
	private double sumMoney = 0;
	public Map<String,CartItem> getMap() {
		return map;
	}
	public void setMap(Map<String,CartItem> map) {
		this.map = map;
	}
	public double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	
}
