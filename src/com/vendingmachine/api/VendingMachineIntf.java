package com.vendingmachine.api;

public interface VendingMachineIntf {

	public String getVMConfigDetails() throws Exception;

	public String setVMConfigDetails(String vmConfig) throws Exception;

	public String resetVMConfigDetails() throws Exception;

}
