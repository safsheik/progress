package com.progrexion.bcm.services.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.model.entities.BCMCustomer;


@Component
public class PlaidValidator extends BCMValidator {

	@Autowired
	RentTrackConfigProperties property;

	public void validatePaymentAccountRequest(PaymentAccountRequestModel paymentAccountRequest) {

		if (!StringUtils.startsWith(paymentAccountRequest.getPublicToken(), property.getPlaidPublicTokenPrefix())) {
			throw new BCMModuleException(BCMModuleExceptionEnum.PLAID_PUBLIC_TOKEN_INVALID);
		}

	}

	public void validateLeaseTransactionFinder(LeaseResponseModel leaseResponseModel) {

		if (null == leaseResponseModel) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_LEASE);
		}

	}
	
	public void validateTransactionFinderId(Long transactionId) {

		if (null == transactionId) {
			throw new BCMModuleException(BCMModuleExceptionEnum.MATCH_TRX_FINDER_NO_RECORDS);
		}

	}
	public void validatePaymentAccountId(BCMCustomer customer) {

		if (null == customer.getPaymentAccountId()) {
			throw new BCMModuleException(BCMModuleExceptionEnum.PAYMENT_ACCOUNT_NOT_FOUND);
		}

	}
}
