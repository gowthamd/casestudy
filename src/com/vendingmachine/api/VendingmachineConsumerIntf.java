package com.vendingmachine.api;

public interface VendingmachineConsumerIntf {

	public String getVendingMachineInfo() throws Exception;

	public String getRackInfo(Integer rackId) throws Exception;

	public String addPaymnet(Integer value) throws Exception;

	public String addProduct(Integer rackId, Integer quantity) throws Exception;
	
	public String cancelLastTransaction() throws Exception;

	public String checkout() throws Exception;

}
