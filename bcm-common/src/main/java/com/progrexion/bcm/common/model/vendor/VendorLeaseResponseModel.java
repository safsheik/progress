package com.progrexion.bcm.common.model.vendor;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorLeaseResponseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long leaseId;
	
	@JsonProperty("url")
	private String LeaseUrl;
	
	@JsonProperty("address")
	private	LeaseAddress address;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("rent")
	private float rent;
	
	@JsonProperty("start_at")
	private String startAt;
	
	@JsonProperty("finished_at")
	private String finishedAt;
	
	@JsonProperty("due_day")
	private String dueDay;
	
	@JsonProperty("back_report_start_at")
	private String backReportStartAt;
	
	@JsonProperty("landlord")
	private	LeaseLandlord landlord;
	
}
