package com.vendingmachine.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VMUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static <T> T convertJsonToObj(String jsonStr, Class<T> clazzName)
			throws JsonParseException, JsonMappingException, IOException {
		return getObjectMapper().readValue(jsonStr, clazzName);

	}

	public static boolean isNotBlank(String s) {
		return s != null && !s.trim().isEmpty();
	}
}
