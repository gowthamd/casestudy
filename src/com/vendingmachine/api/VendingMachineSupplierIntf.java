package com.vendingmachine.api;

public interface VendingMachineSupplierIntf {

	public String getVendingMachineInfo() throws Exception;

	public String getRackInfo(Integer rackId) throws Exception;

	public String resetRack(Integer rackId) throws Exception;

	public String addProductToRack(Integer rackId, String itemDetail) throws Exception;
	
	public String addCashToVM(String cashDetails) throws Exception;
	
	public String getVMCashInfo() throws Exception;

}
