package com.progrexion.bcm.common.model.v1;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;

@Data
public class CreateUserRequestModel {

	
	private String type;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	private String residentId;
	private	LeaseRequestModel leaseData;
	
			
}
