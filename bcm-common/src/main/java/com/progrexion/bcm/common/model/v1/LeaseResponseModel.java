package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class LeaseResponseModel {
	
	private Long leaseId;
	private String LeaseUrl;
	private	Address address;
	private String status;
	private String rent;
	private String startAt;
	private String finishedAt;
	private String dueDay;
	private String backReportStartAt;
	private	Landlord landlordModel;
	private Boolean updateStatus;
	

}
