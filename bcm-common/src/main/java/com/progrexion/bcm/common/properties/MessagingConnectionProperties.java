package com.progrexion.bcm.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagingConnectionProperties {
	private String brokerurl;
	private String username;
	private String password;
}
