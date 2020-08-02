package com.progrexion.bcm.common.webclients.errorhandlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WebClientErrorHandler {


	public ExchangeFilterFunction customerMasterWebClientErrorHandlingFilter() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			if (clientResponse.statusCode() != null && clientResponse.statusCode().is4xxClientError()) {
				return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
					return Mono.error(new BCMWebClientResponseException(errorBody, clientResponse.statusCode(),
							BCMModuleExceptionEnum.CUSTOMER_MASTER_CLIENT_ERROR));
				});
			} else if (clientResponse.statusCode() != null && (clientResponse.statusCode().is5xxServerError())) {
				return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
					return Mono.error(new BCMWebClientResponseException(errorBody, clientResponse.statusCode(),
							BCMModuleExceptionEnum.CUSTOMER_MASTER_SERVER_ERROR));
				});
			} else {
				return Mono.just(clientResponse);
			}
		});
	}

	

	public ExchangeFilterFunction rentTrackWebClientErrorHandlingFilter() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			if (clientResponse.statusCode() != null && clientResponse.statusCode().is4xxClientError()) {
				if(clientResponse.statusCode().value() == 409) {
					log.error("rentTrackWebClientErrorHandlingFilter:409");
					return Mono.error(new BCMWebClientResponseException("A resouce already exists.", clientResponse.statusCode(),
							BCMModuleExceptionEnum.RENTTRACK_CLIENT_ERROR_CONFLICT));
				} else {
					log.error("rentTrackWebClientErrorHandlingFilter:4xx");
					return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
						return Mono.error(new BCMWebClientResponseException(errorBody, clientResponse.statusCode(),
								BCMModuleExceptionEnum.RENTTRACK_CLIENT_ERROR));
					});
				}			
			} else if (clientResponse.statusCode() != null && (clientResponse.statusCode().is5xxServerError())) {
				log.error("rentTrackWebClientErrorHandlingFilter:5xx");
				return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
					return Mono.error(new BCMWebClientResponseException(errorBody, clientResponse.statusCode(),
							BCMModuleExceptionEnum.RENTTRACK_SERVER_ERROR));
				});
			} else {
				log.error("rentTrackWebClientErrorHandlingFilter:Default");
				return Mono.just(clientResponse);
			}
		});
	}
}
