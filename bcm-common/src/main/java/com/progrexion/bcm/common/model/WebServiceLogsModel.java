package com.progrexion.bcm.common.model;

import lombok.Data;

@Data
public class WebServiceLogsModel {

	private Long ucid;
	private String url;
	private String requestBody;
	private String responseBody;
	private Integer httStatusCode;
	private String httpMethod;
	private String brand;

}
