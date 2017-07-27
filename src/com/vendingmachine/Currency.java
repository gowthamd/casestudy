package com.vendingmachine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {
	@JsonProperty(value ="count")
	private Integer count;
	@JsonProperty(value ="value")
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Currency(Integer value, Integer count) {
		this.value = value;
		this.count = count;
	}
	
	public Currency() {
		
	}
}
