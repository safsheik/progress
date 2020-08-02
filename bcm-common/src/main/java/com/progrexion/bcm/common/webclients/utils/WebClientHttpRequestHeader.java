package com.progrexion.bcm.common.webclients.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.utils.CommonUtils;

@Component
public class WebClientHttpRequestHeader {

	@Autowired
	private CustomerMasterConfigProperties property;


	public HttpHeaders configureRentTrackHeaders(HttpHeaders httpHeaders,String token) {
		httpHeaders.remove(HttpHeaders.CONTENT_TYPE);
		httpHeaders.remove(HttpHeaders.ACCEPT);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		httpHeaders.add(HttpHeaders.AUTHORIZATION, CommonUtils.setToken(token));
		return httpHeaders;
	}
	
	/**
	 * This method will set the Customer Master Headers
	 * @param httpHeaders
	 * @return
	 */
	public HttpHeaders configureCustomerMasterHeaders(HttpHeaders httpHeaders) {

		httpHeaders.remove(HttpHeaders.CONTENT_TYPE);
		httpHeaders.remove(HttpHeaders.ACCEPT);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add(property.getHeaderNameEndUser(),property.getHeaderValueEndUser());
		httpHeaders.add(property.getHeaderNameEndUserType(),property.getHeaderValueEndUserType());

		String userName = property.getApiUser();
		String password = property.getPassword();

		httpHeaders.add(HttpHeaders.AUTHORIZATION, CommonUtils.getBasicAuthCred(userName, password));
		return httpHeaders;
	}

}
