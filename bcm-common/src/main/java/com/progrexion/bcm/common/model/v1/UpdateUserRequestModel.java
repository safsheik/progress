package com.progrexion.bcm.common.model.v1;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateUserRequestModel {

	
	private String firstName;
	private String lastName;
	private String middleName;
	private String email;
	private String phone;
	//@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	private String ssn;
	private Boolean emailNotification;
	private Boolean offerNotification;
	private String culture;


}
