package com.vendingmachine.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.vendingmachine.Currency;
import com.vendingmachine.Item;
import com.vendingmachine.Rack;
import com.vendingmachine.VMConfiguration;
import com.vendingmachine.VendingMachine;
import com.vendingmachine.storagereader.StorageReader;
import com.vendingmachine.util.VMUtil;

@Path("supplier")
public class VendingMachineSupplierIntfImpl implements VendingMachineSupplierIntf {

	@Override
	@GET
	@Path("/vending-machine")
	public String getVendingMachineInfo() throws Exception {
		StorageReader reader = new StorageReader();
		return reader.readVendingMachineContent();
	}

	@Override
	@GET
	@Path("/vending-machine/rack/{rack-id}")
	public String getRackInfo(@PathParam(value = "rack-id ") Integer rackId) throws Exception {
		VendingMachine vm = new StorageReader().readVendingMachineContentAsObj();
		if (rackId > vm.getRacks().size()) {
			return "Invalid rack Id";
		}
		return vm.getRacks().get(rackId).toString();
	}

	@Override
	@POST
	@Path("/vending-machine/rack/{rack-id}/reset")
	public String resetRack(@PathParam(value = "rack-id ") Integer rackId) throws Exception {
		VendingMachine vm = new StorageReader().readVendingMachineContentAsObj();
		if (rackId > vm.getRacks().size()) {
			return "Invalid rack Id";
		}
		Rack rack = vm.getRacks().get(rackId);
		rack.resetRack();
		new StorageReader().writeVendingMachine(vm);
		return "Success";
	}

	@Override
	@POST
	@Path("/vending-machine/rack/{rack-id}/product")
	public String addProductToRack(@PathParam(value = "rack-id") Integer rackId, String productDetails)
			throws Exception {

		if(rackId == null) {
			return "failed";
		}
		Item product = null;
		try {
			product = VMUtil.convertJsonToObj(productDetails, Item.class);
		} catch (JsonParseException e) {
			return "Invalid Input";
		}
		StorageReader reader = new StorageReader();
		VendingMachine vm = reader.readVendingMachineContentAsObj();
		if(vm.getRacks() == null) {
			return "failed to add product";
		}
		
		if (rackId > vm.getRacks().size()) {
			return "Invalid rack Id";
		}
		Rack rack = vm.getRacks().get(rackId-1);
		if (product.getQuantity() > 0) {
			VMConfiguration vmConfig = reader.readVMConfigurationAsObj();
			if (product.getQuantity() > vmConfig.getMaxItemsOnRack()) {
				return "Exceeds Max Quantity size";
			}
			rack.getItem().setQuantity(product.getQuantity());
		}
		if (VMUtil.isNotBlank(product.getName())) {
			rack.getItem().setName(product.getName());
		}
		if (product.getPrice() > 0) {
			if (product.getPrice() % 5 != 0) {
				return "InValid Price. Please provide price in multiples of 5";
			}
			rack.getItem().setPrice(product.getPrice());
		}
		reader.writeVendingMachine(vm);
		return "Success";
	}

	@Override
	@POST
	@Path("/vending-machine/cash")
	public String addCashToVM(String cashDetails) throws Exception {
		Currency currency = null;
		try {
			currency = VMUtil.convertJsonToObj(cashDetails, Currency.class);
		} catch (JsonParseException e) {
			return "Invalid Input";
		}
		if (currency.getCount() < 0 || (currency.getValue() <= 0 && currency.getValue() != 5
				&& currency.getValue() != 10 && currency.getValue() != 20)) {
			return "Invalid currency value.Please add 5 or 10 or 20 rupee currency";
		}
		StorageReader reader = new StorageReader();
		VendingMachine vm = reader.readVendingMachineContentAsObj();
		List<Currency> currencies = vm.getCurrencies();
		for (Currency iter : currencies) {
			if (iter.getValue() == currency.getValue()) {
				iter.setCount(currency.getCount());
				break;
			}
		}
		reader.writeVendingMachine(vm);
		return "Success";

	}

	@Override
	@GET
	@Path("/vending-machine/cash")
	public String getVMCashInfo() throws Exception {
		StorageReader reader = new StorageReader();
		VendingMachine vm =  reader.readVendingMachineContentAsObj();
		return VMUtil.getObjectMapper().writeValueAsString(vm.getCurrencies());
	}
}
