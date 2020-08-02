package com.progrexion.bcm.common.model.vendor;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VendorCreateUserRequestModel {

	private String type;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	private String email;
	
	private String password;
	@JsonProperty("resident_id")
	private String residentId;
}
