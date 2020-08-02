package com.progrexion.bcm.common.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "activemq")
public class MessagingProperties {
	private Long jmsRecoveryInterval;
	private Map<String, MessagingModuleProperties> modules;
}
