package com.progrexion.bcm.models.v1;

import lombok.Data;

@Data
public class ProvisioningChangeDto {

	Long ucid;
	String brand;
	Long subscriptionId;
	Long externalId;
	String channel;
	Long newSubscriptionId;

}
