package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("rental_address")
	private	AddressDto address;
	private float rent;	
	private int dueDay;
	@JsonProperty("landlord")
	private	LandlordDto landlord;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("start_at")
	private LocalDate startAt;
	
	
	
	
}
