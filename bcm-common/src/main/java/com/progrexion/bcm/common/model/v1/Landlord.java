package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class Landlord {

	private String Type;
	private String name;
	private String phone;
	private String email;
	public Landlord(String type, String name, String phone, String email) {
		super();
		Type = type;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
	
	public Landlord() {
		super();
	}
}
