package com.vendingmachine.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.vendingmachine.Currency;
import com.vendingmachine.Item;
import com.vendingmachine.Rack;
import com.vendingmachine.VendingMachine;
import com.vendingmachine.storagereader.StorageReader;
import com.vendingmachine.util.VMUtil;

@Path("consumer")
public class VendingmachineConsumerIntfImpl implements VendingmachineConsumerIntf {

	@Override
	@GET
	@Path("vending-machine")
	public String getVendingMachineInfo() throws Exception {
		StorageReader reader = new StorageReader();
		return reader.readVendingMachineContent();
	}

	@Override
	@GET
	@Path("/vending-machine/rack/{rack-id}")
	public String getRackInfo(@PathParam(value = "rack-id") Integer rackId) throws Exception {
		VendingMachine vm = new StorageReader().readVendingMachineContentAsObj();
		if (rackId > vm.getRacks().size()) {
			return "Invalid rack Id";
		}
		return VMUtil.getObjectMapper().writeValueAsString(vm.getRacks().get(rackId - 1));
	}

	@Override
	@POST
	@Path("/vending-machine/order/payment")
	public String addPaymnet(@QueryParam(value = "value") Integer value) throws Exception {
		StorageReader reader = new StorageReader();
		VendingMachine vm = reader.readTransactionInProgressStateAsObj();
		List<Currency> currencies = vm.getCurrencies();
		for (Currency iter : currencies) {
			if (iter.getValue() == value) {
				iter.setCount(iter.getCount() + 1);
				break;
			}
		}
		reader.writeTransactionInProgressState(vm);
		return "Success";
	}

	@Override
	@POST
	@Path("/vending-machine/order/product")
	public String addProduct(@QueryParam(value = "rack-Id") Integer rackId,
			@QueryParam(value = "quantity") Integer quantity) throws Exception {
		StorageReader reader = new StorageReader();
		VendingMachine vm = reader.readTransactionInProgressStateAsObj();
		List<Rack> racks = vm.getRacks();
		if (racks == null) {
			racks = new ArrayList<>();
			vm.setRacks(racks);
		}
		boolean isFound = false;
		for (Rack iter : racks) {
			if (iter.getRackNumber() == rackId) {
				iter.getItem().setQuantity(quantity);
				isFound = true;
			}
		}
		Integer quantityReq = 0;
		if (!isFound) {
			Rack rack = new Rack();
			rack.setRackNumber(rackId);
			Item item = new Item();
			item.setQuantity(quantity);
			quantityReq = item.getQuantity();
			rack.setItem(item);
			racks.add(rack);
		}
		Item itemInVM = reader.readVendingMachineContentAsObj().getRacks().get(rackId - 1).getItem();

		if (quantityReq > itemInVM.getQuantity()) {
			return "Failed request is more than the available quantity in Vending machine";
		}
		reader.writeTransactionInProgressState(vm);
		return "Success";
	}

	@Override
	@POST
	@Path("/vending-machine/order/cancel")
	public String cancelLastTransaction() throws Exception {
		StorageReader reader = new StorageReader();
		VendingMachine vm = reader.readTransactionInProgressStateAsObj();
		List<Currency> currencies = vm.getCurrencies();
		Integer returnValue = 0;
		for (Currency iter : currencies) {
			returnValue += iter.getValue() * iter.getCount();
		}
		reader.initialzeTransactionInProgressState();
		return "Please collect your cash " + returnValue;
	}

	@Override
	@POST
	@Path("/vending-machine/order/checkout")
	public String checkout() throws Exception {
		StorageReader reader = new StorageReader();
		VendingMachine transactionInProgress = reader.readTransactionInProgressStateAsObj();
		Integer totalAmountAdded = 0, totalAmountFromQuantitySelected = 0;
		for (Currency iter : transactionInProgress.getCurrencies()) {
			totalAmountAdded += iter.getValue() * iter.getCount();
		}
		VendingMachine vm = reader.readVendingMachineContentAsObj();
		for (Rack iter : transactionInProgress.getRacks()) {
			Integer rackId = iter.getRackNumber();
			Item itemInVM = vm.getRacks().get(rackId - 1).getItem();
			totalAmountFromQuantitySelected += itemInVM.getPrice() * iter.getItem().getQuantity();
		}
		if (totalAmountAdded < totalAmountFromQuantitySelected) {
			return "Failed : insufficient money";
		}
		Integer amountToReturn = totalAmountAdded - totalAmountFromQuantitySelected;

		// checkout

		for (Rack iter : transactionInProgress.getRacks()) {
			Integer rackId = iter.getRackNumber();
			Item itemInVM = vm.getRacks().get(rackId - 1).getItem();
			itemInVM.setQuantity(itemInVM.getQuantity() - iter.getItem().getQuantity());
		}

		for (Currency iter : transactionInProgress.getCurrencies()) {
			for (Currency iter1 : vm.getCurrencies()) {
				if (iter1.getValue() == iter.getValue()) {
					iter1.setCount(iter1.getCount() + iter.getCount());
				}
			}
		}

		// calculate balance and update
		for (Currency iter1 : vm.getCurrencies()) {
			if (amountToReturn / iter1.getValue() > 0) {
				iter1.setCount(iter1.getCount() - amountToReturn / iter1.getValue());
			}
			amountToReturn = amountToReturn / iter1.getValue();
		}
		if (amountToReturn != 0) {
			return "Failed:Vending machince doesn't have sufficient amount to return balance amount";
		}
		reader.writeVendingMachine(vm);
		reader.initialzeTransactionInProgressState();
		return "success : please collect your balnace :" + (totalAmountAdded - totalAmountFromQuantitySelected);
	}

}
