package com.vendingmachine;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vending-machine-conf")
public class VMConfiguration {

	/**
	 * maximun rack vending machine have
	 */
	@XmlElement(name = "max-rack-count")
	private Integer maxRackCount;

	/**
	 * maximum item kept in each rack
	 */
	@XmlElement(name = "max-items-per-rack")
	private Integer maxItemsOnRack;

	public Integer getMaxRackCount() {
		return maxRackCount;
	}

	public void setMaxRackCount(Integer maxRackCount) {
		this.maxRackCount = maxRackCount;
	}

	public Integer getMaxItemsOnRack() {
		return maxItemsOnRack;
	}

	public void setMaxItemsOnRack(Integer maxItemsOnRack) {
		this.maxItemsOnRack = maxItemsOnRack;
	}

}
