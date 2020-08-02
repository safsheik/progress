package com.progrexion.bcm.model.entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "WEBSERVICE_LOGS")
@Access(AccessType.FIELD)
@Data
public class WebServiceLog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logId;

	@Column(name = "UCID")
	private Long ucid;

	@Column(name = "URL")
	private String url;

	@Column(name = "HTTP_STATUS_CODE")
	private Long httpStatusCode;

	@Column(name = "HTTP_METHOD")
	private String httpMethod;

	@Column(name = "REQUEST_PAYLOAD")
	private String requestPayload;

	@Column(name = "RESPONSE_PAYLOAD")
	private String responsePayload;
	
	@Column(name = "BRAND")
	private String brand;
	
	@Column(name = "CREATED_DATE")
	protected ZonedDateTime createdDate;

	public WebServiceLog() {
		super();
		this.createdDate =ZonedDateTime.now(ZoneId.systemDefault());
	}
}
