package com.progrexion.bcm.models.v1;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseResponseDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	@JsonProperty("rental_address")
	private	AddressDto address; 
	private String status;
	private float rent;
	@JsonProperty("start_at")
	private String startAt;
	@JsonProperty("finished_at")
	private String finishedAt;
	@JsonProperty("due_day")
	private String dueDay;
	private	LandlordDto landlord;
	@JsonProperty("back_report_start_at")
	private String backReportStartAt;
	

}
