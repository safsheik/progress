package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
@Data
@JsonInclude(Include.NON_NULL)
public class LandlordApiModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Type")
	private String type;
	@JsonProperty("name")
	private String name;
	@JsonProperty("phone")
	private String phone;
	@JsonProperty("email")
	private String email;
	
}
