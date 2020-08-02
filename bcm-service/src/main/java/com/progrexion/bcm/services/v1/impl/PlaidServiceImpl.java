package com.progrexion.bcm.services.v1.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountResponseModel;
import com.progrexion.bcm.common.model.v1.PlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.PlaidRequestBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.v1.PlaidService;
import com.progrexion.bcm.services.v1.TransactionService;
import com.progrexion.bcm.services.validator.PlaidValidator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PlaidServiceImpl extends BaseBCMService  implements PlaidService {

	@Autowired
	private PlaidValidator validator;
	@Autowired
	private PlaidRequestBuilder plaidRequestBuilder;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private CustomerService customerService;


	@Override
	public PaymentAccountResponseModel createPlaidPaymentAccount(Long ucid, String brand,
			PaymentAccountRequestModel paymentAccountRequest) {
		log.info("PlaidServiceImpl: createPlaidPaymentAccount Start ucid: ", ucid);
		TransactionResponseModel transactionCreateResponse = null;
		PaymentAccountResponseModel paymentAccountResponse = null;
		TransactionRequestModel transactionRequestModel = new TransactionRequestModel();
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid, brand);
		
		PaymentAccountResponseModel paymentAccountResponseModel = null;
		validator.validateCustomer(bcmCustomer);
		validator.validatePaymentAccountRequest(paymentAccountRequest);
		
		VendorPaymentAccountResponseModel[] vendorPlaidPayAccResponse = processPlaidPaymentAccountRequest(bcmCustomer,
				paymentAccountRequest);
	
		paymentAccountResponseModel = new ModelMapper().map(vendorPlaidPayAccResponse,
				PaymentAccountResponseModel[].class)[0];

		if (null != vendorPlaidPayAccResponse) {
			bcmCustomer.setPaymentAccountId(paymentAccountResponseModel.getId());
			customerService.updateBcmCustomer(bcmCustomer);
			log.info("PlaidServiceImpl: createPlaidPaymentAccount: PlaidPaymentAccount Account ID",
					paymentAccountResponseModel.getId());
			transactionRequestModel.setPaymentAccountUrl(paymentAccountResponseModel.getUrl());

			transactionCreateResponse = transactionService.createOrUpdateTransactionFinder(bcmCustomer,
					transactionRequestModel);
		} else {
			log.error("PlaidServiceImpl: createPlaidPaymentAccount: PlaidPaymentAccount Account ID is null");
			throw new BCMModuleException(BCMModuleExceptionEnum.PAYMENT_ACCOUNT_500);
		}
		log.info("PlaidServiceImpl: createPlaidPaymentAccount End ucid: ", ucid);
		if( null != transactionCreateResponse) {
			paymentAccountResponse = modelMapper.map(transactionCreateResponse, PaymentAccountResponseModel.class);
		}
		return paymentAccountResponse;
	}

	private VendorPaymentAccountResponseModel[] processPlaidPaymentAccountRequest(BCMCustomer customer,
			PaymentAccountRequestModel paymentAccountRequest) {
		VendorPaymentAccountRequestModel request = plaidRequestBuilder
				.buildPlaidVendorPaymentAccountRequestModel(paymentAccountRequest);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).createPlaidPaymentAccount(request,
				customer.getCustomerDataId(), apiNotificationHandler);

	}

	@Override
	public PaymentAccountResponseModel getPaymentAccountDetails(Long ucid, String brand) {
		PaymentAccountResponseModel paymentAccountResponse = null;
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid, brand);
		validator.validateCustomer(bcmCustomer);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		VendorPaymentAccountResponseModel vendorResponse = getPaymentAccountInfo(bcmCustomer, apiNotificationHandler);
		if(null != vendorResponse) {
			paymentAccountResponse = modelMapper.map(vendorResponse, PaymentAccountResponseModel.class);
		}
		return paymentAccountResponse;
	}


	private VendorPaymentAccountResponseModel getPaymentAccountInfo(BCMCustomer customer,
			VendorAPINotification apiNotificationHandler) {
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.getPlaidPaymentAccountDetails(customer.getCustomerDataId(), apiNotificationHandler);
	}

	@Override
	public PlaidReconnectResponseModel reconnectPlaid(Long ucid, String brand, String reqType) {
		PlaidReconnectResponseModel plaidReconnectResponse= null;
		BCMCustomer customer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(customer);
		validator.validatePaymentAccountId(customer);
		
		VendorPlaidReconnectResponseModel vendorResponse = processReconnectPlaidRequest(customer,reqType);
		if(null != vendorResponse){
			plaidReconnectResponse =  modelMapper.map(vendorResponse, PlaidReconnectResponseModel.class);
		}
		return plaidReconnectResponse;
	}
	
	private VendorPlaidReconnectResponseModel processReconnectPlaidRequest(BCMCustomer customer, String reqType)  {
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).reconnectPlaid(customer.getCustomerDataId(),customer.getPaymentAccountId(),apiNotificationHandler,reqType);

	}
}
