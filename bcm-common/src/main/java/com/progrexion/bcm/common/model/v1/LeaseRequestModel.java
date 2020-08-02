package com.progrexion.bcm.common.model.v1;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LeaseRequestModel {
	

	private	Address address;
	private float rent;
	private int dueDay;
	private	Landlord landlord;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt;
	

	

}
