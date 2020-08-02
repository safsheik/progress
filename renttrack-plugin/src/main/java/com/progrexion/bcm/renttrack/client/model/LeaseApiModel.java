package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data

@JsonInclude(Include.NON_NULL)
public class LeaseApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("address")
	private AddressApiModel address;

	@JsonProperty("landlord")
	private LandlordApiModel landlord;

	@JsonProperty("rent")
	private float rent;

	@JsonProperty("due_day")
	private int dueDay;

	@JsonProperty("start_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt;
	
	
}