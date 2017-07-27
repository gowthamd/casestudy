package com.vendingmachine.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendingmachine.Currency;
import com.vendingmachine.Rack;
import com.vendingmachine.VMConfiguration;
import com.vendingmachine.VendingMachine;
import com.vendingmachine.storagereader.StorageReader;
import com.vendingmachine.util.VMUtil;

@Path("vending-machine")
public class VendingMachineIntfImpl implements VendingMachineIntf {

	@GET
	@Path("/config")
	public String getVMConfigDetails() throws Exception {
		StorageReader reader = new StorageReader();
		return reader.readVMConfiguration();
	}

	@POST
	@Path("/config")
	public String setVMConfigDetails(String vmConfig) throws Exception {
		VMConfiguration config = null;
		try {
			config = VMUtil.convertJsonToObj(vmConfig, VMConfiguration.class);
		} catch (JsonParseException e) {
			return " Invalid Input";
		}
		StorageReader reader = new StorageReader();
		reader.writeVMConfiguration(config);
		initialzeVendingMachine(reader, config);
		reader.initialzeTransactionInProgressState();
		return "Success";
	}

	private void initialzeVendingMachine(StorageReader reader, VMConfiguration config) throws Exception {
		VendingMachine vm = new VendingMachine();
		List<Rack> racks = new ArrayList<Rack>();
		vm.setRacks(racks);
		for (int i = 1; i <= config.getMaxRackCount(); i++) {
			Rack rack = new Rack(i);
			racks.add(rack);
		}
		Currency currency1 = new Currency(5,0);
		Currency currency2 = new Currency(10,0);
		Currency currency3 = new Currency(20,0);
		List<Currency> currencies = new ArrayList<Currency>();
		currencies.add(currency1);
		currencies.add(currency2);
		currencies.add(currency3);
		vm.setCurrencies(currencies);
		reader.writeVendingMachine(vm);
	}
	
	

	@POST
	@Path("/reset")
	public String resetVMConfigDetails() throws Exception {
		StorageReader reader = new StorageReader();
		reader.resetVMConfiguration();
		return "Success";
	}

}
