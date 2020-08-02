package com.progrexion.bcm.services.validator;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;

@Component
public class LeaseValidator extends BCMValidator {

	public void validateLeaseRequest(LeaseRequestModel leaseRequestModel) {

		if (null == leaseRequestModel.getAddress()) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ADDRESS_INVALID);
		}
		if (StringUtils.isBlank(leaseRequestModel.getAddress().getAddress1())) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ADDRESS_INVALID);
		}

		if (StringUtils.isBlank(leaseRequestModel.getAddress().getState())) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ADDRESS_STATE_INVALID);
		}

		if (StringUtils.isBlank(leaseRequestModel.getAddress().getCity())) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ADDRESS_CITY_INVALID);
		}

		if (StringUtils.isBlank(leaseRequestModel.getAddress().getZip())) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ADDRESS_ZIP_INVALID);
		}
		if (leaseRequestModel.getDueDay() < 1 || leaseRequestModel.getDueDay() > 31) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_DUE_DAY_INVALID);
		}
		if (leaseRequestModel.getRent() <= 0) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_RENT_INVALID);
		}
	}

}