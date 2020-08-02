package com.progrexion.bcm.common.model.vendor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VendorUpdateUserRequestModel {

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("middle_name")
	private String middleName;

	private String email;

	private String phone;

	@JsonProperty("date_of_birth")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;

	private String ssn;

	@JsonProperty("email_notification")
	private Boolean emailNotification;

	@JsonProperty("offer_notification")
	private Boolean offerNotification;

	private String culture;
}
