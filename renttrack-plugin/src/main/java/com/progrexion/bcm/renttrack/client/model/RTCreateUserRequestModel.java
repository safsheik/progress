package com.progrexion.bcm.renttrack.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RTCreateUserRequestModel {

	private String type;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	private String email;
	
	private String password;
	
	@JsonProperty("resident_id")
	private String residentId;
	
	@JsonProperty("holding_id")
	private String holdingId;
}
