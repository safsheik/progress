package com.progrexion.bcm.messaging.models.provisioningchange.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Actions that can be requested in a ProvisioningChangeMessage.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ProvisioningAction {
	ADD, REMOVE, UPDATE
}
