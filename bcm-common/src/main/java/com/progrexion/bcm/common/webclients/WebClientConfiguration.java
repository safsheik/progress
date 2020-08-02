package com.progrexion.bcm.common.webclients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.common.webclients.utils.WebClientHttpRequestHeader;

@Configuration
public class WebClientConfiguration {
	
	@Bean("customerMasterWebClient")
	public WebClient customerMasterWebClient(WebClientErrorHandler webClientErrorHandler,
			WebClientHttpRequestHeader webClientHttpRequestHeader, CustomerMasterConfigProperties property) {

		return WebClient.builder()
				.filter(webClientErrorHandler.customerMasterWebClientErrorHandlingFilter())
				.baseUrl(property.getHost())
				.defaultHeaders((headers) -> webClientHttpRequestHeader.configureCustomerMasterHeaders(headers))
				.build();
	}

	

	@Bean("rentTrackWebClient")
	public WebClient rentTrackWebClient() {
		return  WebClient.create();
	}

}
