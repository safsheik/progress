package com.progrexion.bcm.services.validator;

import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;

@Component
public class TransactionValidator extends BCMValidator {

	public void validateTransactionFinder(TransactionRequestModel transactionRequestModel) {

		if (transactionRequestModel.getDueDay() < 1 || transactionRequestModel.getDueDay() > 31) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_DUE_DAY);
		}
		
		if (transactionRequestModel.getRentAmount() < 1 ) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_AMOUNT_RENT);
		}

	}

	public void validateLeaseTransactionFinder(VendorLeaseResponseModel vendorLeaseResponseModel) {

		if (vendorLeaseResponseModel == null) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_LEASE);
		}

	}

	public void validatePaymentAccount(VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel) {

		if (vendorPaymentAccountResponseModel == null) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_PAYMENT_ACC);
		}

	}

	
	public void validateLeaseResponse(LeaseResponseModel leaseResponseModel) {

		if (leaseResponseModel == null) {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRANSACTION_FINDER_LEASE);
		}

	}
}
