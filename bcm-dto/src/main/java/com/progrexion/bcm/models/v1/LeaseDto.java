package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("rental_address")
	private AddressDto address;
	private float rent;
	private String startAt;
	private String dueDay;

}
