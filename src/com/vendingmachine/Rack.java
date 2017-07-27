package com.vendingmachine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rack {

	@JsonProperty(value ="id")
	private Integer rackNumber;

	@JsonProperty(value ="item")
	private Item item;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getRackNumber() {
		return rackNumber;
	}

	public void setRackNumber(Integer rackNumber) {
		this.rackNumber = rackNumber;
	}

	public Rack(Integer rackNumber) {
		this.setRackNumber(rackNumber);
		this.item = new Item();
		resetRack();
	}
	
	public Rack() {
		
	}

	public void resetRack() {
		this.item.setName("");
		this.item.setPrice(0);
		this.item.setQuantity(0);
	}

}
