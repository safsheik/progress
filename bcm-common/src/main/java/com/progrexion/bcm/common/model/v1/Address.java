package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class Address {

	private String address1;
	private String address2;
	private String city;
	private String zip;
	private String state;
	private String country ;
	
	public Address(String address1, String address2, String city, String zip, String state, String country) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.country = country;
	}

	public Address() {
		super();
	}

}
