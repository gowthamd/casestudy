package com.vendingmachine.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/places")
public class API {
	@GET
	@Path("test")
	public String test() {
		return "tested";
	}

	/*@GET
	@Path("autocomplete")
	public JsonObject getAutoCompletePlaces(@QueryParam(value = "place") String placeStr) {
		return TPUtil.getAutoCompletePlaces(placeStr);
	}*/
}