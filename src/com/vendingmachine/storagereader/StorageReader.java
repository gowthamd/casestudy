package com.vendingmachine.storagereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendingmachine.Currency;
import com.vendingmachine.Rack;
import com.vendingmachine.VMConfiguration;
import com.vendingmachine.VendingMachine;

public class StorageReader {

	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	public void initialzeTransactionInProgressState() throws Exception {
		VendingMachine vm = new VendingMachine();
		List<Rack> racks = new ArrayList<Rack>();
		vm.setRacks(racks);
		Currency currency1 = new Currency(5, 0);
		Currency currency2 = new Currency(10, 0);
		Currency currency3 = new Currency(20, 0);
		List<Currency> currencies = new ArrayList<Currency>();
		currencies.add(currency1);
		currencies.add(currency2);
		currencies.add(currency3);
		vm.setCurrencies(currencies);
		writeTransactionInProgressState(vm);
	}

	public void writeTransactionInProgressState(VendingMachine config) throws Exception {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(StorageConstants.TRANS_INPROGRESS_FILE_NAME);
			writer.write("");
			String jsonStr = objectMapper.writeValueAsString(config);
			writer.write(jsonStr);
		} finally {
			writer.close();
		}
	}

	public VendingMachine readTransactionInProgressStateAsObj() throws Exception {
		return objectMapper.readValue(new File(StorageConstants.TRANS_INPROGRESS_FILE_NAME), VendingMachine.class);
	}

	private String readFile(String filename) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} finally {
			reader.close();
		}
		return stringBuilder.toString();
	}

	public String readVMConfiguration() throws Exception {
		return readFile(StorageConstants.VM_CONFIG__FILE_NAME);
	}

	public VMConfiguration readVMConfigurationAsObj() throws Exception {
		return objectMapper.readValue(new File(StorageConstants.VM_CONFIG__FILE_NAME), VMConfiguration.class);
	}

	public void resetVMConfiguration() throws Exception {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(StorageConstants.VM_CONFIG__FILE_NAME);
			writer.write("");
			writer = new PrintWriter(StorageConstants.ITEMS_FILE_NAME);
			writer.write("");
			initialzeTransactionInProgressState();
		} finally {
			writer.close();
		}
	}

	public void writeVMConfiguration(VMConfiguration config) throws Exception {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(StorageConstants.VM_CONFIG__FILE_NAME);
			writer.write("");
			String jsonStr = objectMapper.writeValueAsString(config);
			writer.write(jsonStr);
		} finally {
			writer.close();
		}
	}

	public void writeVendingMachine(VendingMachine vm) throws Exception {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(StorageConstants.ITEMS_FILE_NAME);
			writer.write("");
			String jsonStr = objectMapper.writeValueAsString(vm);
			writer.write(jsonStr);
		} finally {
			writer.close();
		}
	}

	public String readVendingMachineContent() throws Exception {
		return readFile(StorageConstants.ITEMS_FILE_NAME);
	}

	public VendingMachine readVendingMachineContentAsObj() throws Exception {
		return objectMapper.readValue(new File(StorageConstants.ITEMS_FILE_NAME), VendingMachine.class);
	}

	public List<Currency> readVendingMachineCashContentAsObj() throws Exception {
		VendingMachine vm = readVendingMachineContentAsObj();
		return vm.getCurrencies();
	}
}
