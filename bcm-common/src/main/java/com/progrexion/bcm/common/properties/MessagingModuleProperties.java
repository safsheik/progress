package com.progrexion.bcm.common.properties;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagingModuleProperties {
	private MessagingConnectionProperties connection;
	private Map<String, MessagingDestinationProperties> destinations;
}
