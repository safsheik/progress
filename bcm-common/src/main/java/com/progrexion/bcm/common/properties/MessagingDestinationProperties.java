package com.progrexion.bcm.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagingDestinationProperties {
	private String destinationName;
	private Boolean isQueue;
	private Boolean isTopic;
	private String 	clientId;
	private Boolean retryRequired;
	private Integer retryAttemptCount;
	private Integer initialRedeliveryDelay;
	private Integer redeliveryDelay;
	private Integer maximumRedeliveries;
	private Boolean exponentialBackoffRequired;
	private Double backoffMultiplier;
	
	public Boolean isQueue() {
		return isQueue;
	}
	
	public Boolean isTopic() {
		return isTopic;
	}
	
	public Boolean isRetryRequired() {
		return retryRequired;
	}
	
	public Boolean isExponentialBackoffRequired() {
		return exponentialBackoffRequired;
	}
}
