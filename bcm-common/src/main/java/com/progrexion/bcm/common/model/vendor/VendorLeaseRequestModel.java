package com.progrexion.bcm.common.model.vendor;

import java.time.LocalDate;

import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.Landlord;

import lombok.Data;

@Data

public class VendorLeaseRequestModel {

	private Address address;
	private float rent;
	private int dueDay;
	private Landlord landlordModel;
	private LocalDate startAt;

}
