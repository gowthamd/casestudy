package com.vendingmachine;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An object to hold information about vending machine
 * 
 * 
 * 
 */
public class VendingMachine {
	
	@JsonProperty(value ="racks")
	private List<Rack> racks;
	
	@JsonProperty(value ="currencies")
	private List<Currency> currencies;

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}

	public List<Rack> getRacks() {
		return racks;
	}

	public void setRacks(List<Rack> racks) {
		this.racks = racks;
	}	
	
	public VendingMachine() {
		
	}

}
