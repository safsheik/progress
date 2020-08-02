package com.progrexion.bcm.messagingconfig.enums;

import java.lang.reflect.Type;

public enum MessageConfigEnum {
	/**
	 * PBS ProvisioningChangeTopic
	 */
	PBS_PROVISIONING_CHANGE("pbs", "provisioningchange", null),
	BCM_JOBS_PROCESSING_ORDER("bcm", "jobprocessingorder", null),
	
	/**
	 * BCM BuildCredit.Public Queue in AWS - disabled for now.
	 */
	//BCM_BUILDCREDIT("bcm", "buildcredit", null),
	
	
	;

	private String module;
	private String destination;

	/**
	 *  TargetType is the Type of Object needs to be converted
	 *  (Message -> targetType) 
	 */
	private Type targetType;
	
	MessageConfigEnum(String module, String destination, Type targetType) {
		this.module = module;
		this.destination = destination;
		this.targetType = targetType;
	}
	
	public String getKey() {
		return module + "_" + destination;
	}
	
	public Type getTargetType() {
		return this.targetType;
	}	

}

