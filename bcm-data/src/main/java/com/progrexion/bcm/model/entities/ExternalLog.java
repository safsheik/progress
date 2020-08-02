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
@Data
@Entity
@Table(name = "EXTERNAL_LOGS")
@Access(AccessType.FIELD)
public class ExternalLog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXTERNAL_LOG_ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long externalLogId;

	@Column(name = "CUSTOMER_DATA_ID")
	private Long customerId;
	
	@Column(name = "UCID")
	private Long ucid;
	
	@Column(name = "BRAND")
	private String brand;
	
	@Column(name = "VENDOR_ID")
	private Long vendorId;

	@Column(name = "URL")
	private String url;

	@Column(name = "VENDOR_REQUEST_PAYLOAD")
	private String vendorRequestPayload;

	@Column(name = "VENDOR_RESPONSE_PAYLOAD")
	private String vendorResponsePayload;
	
	@Column(name = "HTTP_METHOD")
	private String httpMethod;

	@Column(name = "CREATED_DATE")
	protected ZonedDateTime createdDate;

	public ExternalLog() {
		super();
		this.createdDate =ZonedDateTime.now(ZoneId.systemDefault());
	}

}
