package com.vendingmachine;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An object to hold information about an particular item
 * 
 *
 */

public class Item {

	@JsonProperty(value ="name")
	private String name;
	@JsonProperty(value ="quantity")
	private Integer quantity;
	@JsonProperty(value ="price")
	private Integer price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public Item(){
		
	}

}
