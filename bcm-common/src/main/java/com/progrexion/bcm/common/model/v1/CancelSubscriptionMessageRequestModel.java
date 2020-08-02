package com.progrexion.bcm.common.model.v1;

import java.util.List;

import lombok.Data;

@Data
public class CancelSubscriptionMessageRequestModel {
	private String brand;
	private Long ucid;
	private List<Long> mergedUcids;
}
