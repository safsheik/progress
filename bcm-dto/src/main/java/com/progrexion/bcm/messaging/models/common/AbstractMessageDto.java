package com.progrexion.bcm.messaging.models.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.progrexion.bcm.messaging.models.utils.ISO8601Deserializer;
import com.progrexion.bcm.messaging.models.utils.ISO8601Serializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractMessageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final double version = 1.0;

	protected Long ucid;
	protected String brand;

	@JsonDeserialize(using = ISO8601Deserializer.class)
	@JsonSerialize(using = ISO8601Serializer.class)
	protected Date timeStamp = new Date();

	public AbstractMessageDto() {
		super();
	}

	public AbstractMessageDto(Long ucid, String brand) {
		super();
		this.ucid = ucid;
		this.brand = brand;
	}

	public Long getUcid() {
		return this.ucid;
	}

	public void setUcid(Long ucid) {
		this.ucid = ucid;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "AbstractMessageDto [ucid=" + this.ucid + ", brand=" + this.brand + ", timeStamp=" + this.timeStamp + "]";
	}

}