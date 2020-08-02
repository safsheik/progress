package com.progrexion.bcm.services.v1.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.TransactionRequestBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.v1.TransactionService;
import com.progrexion.bcm.services.validator.TransactionValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionServiceImpl extends BaseBCMService implements TransactionService {

	@Autowired
	private TransactionValidator validator;
	@Autowired
	private TransactionRequestBuilder transactionRequestBuilder;
	@Autowired
	private CustomerService customerService;

	@Override
	public TransactionResponseModel createTransactionFinder(Long ucid, String brand, TransactionRequestModel transactionRequestModel)
		{
		log.error("TransactionFinderServiceImpl :  createTransactionFinder");
		String transactionStatus = null ;
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(bcmCustomer);
		validator.validateTransactionFinder(transactionRequestModel);
		//Transaction transaction = new Transaction(); // DB entity call

		// get lease info
		VendorLeaseResponseModel vendorLeaseResponseModel = getLeaseDetails(bcmCustomer);
		validator.validateLeaseTransactionFinder(vendorLeaseResponseModel);
		transactionRequestModel.setLeaseUrl(vendorLeaseResponseModel.getLeaseUrl());

		// get payment account info
		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel = getPaymentAccountInfo(bcmCustomer);
		validator.validatePaymentAccount(vendorPaymentAccountResponseModel);
		transactionRequestModel.setPaymentAccountUrl(vendorPaymentAccountResponseModel.getUrl());	
		
		VendorTransactionResponseModel vendorResponse = creationTransactionFinderRequest(bcmCustomer, transactionRequestModel,
				vendorLeaseResponseModel.getLeaseId());

		// search Transaction finder Id
		if (null != vendorResponse) {
			transactionRequestModel.setTransactionFinderId(vendorResponse.getId());
			bcmCustomer.setTransactionFinderId(vendorResponse.getId());
			customerService.updateBcmCustomer(bcmCustomer);
			transactionStatus = vendorResponse.getStatus();
		}
		VendorSearchTransactionResponseModel[] searchVendorResponse = searchByTransactionRequest(bcmCustomer, transactionRequestModel,
				vendorLeaseResponseModel.getLeaseId());

		return constructTransFinderResponseModel(searchVendorResponse, transactionStatus);
	}

	private VendorTransactionResponseModel creationTransactionFinderRequest(BCMCustomer bcmCustomer,
			TransactionRequestModel transactionRequestModel, Long leaseId){
		VendorTransactionRequestModel request = transactionRequestBuilder.buildVendorTransactionFinderRequestModel(
				transactionRequestModel);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).createTransactionFinder(request, bcmCustomer.getCustomerDataId(),
				leaseId, apiNotificationHandler);

	}


	public VendorSearchTransactionResponseModel[]  searchByTransactinFinder(Long ucid, String brand)
		{
		log.info("TransactionFinderServiceImpl :  searchByTransactinFinder");

		TransactionRequestModel transactionRequestModel = new TransactionRequestModel();
		BCMCustomer customer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(customer);

		// get lease info
		VendorLeaseResponseModel vendorLeaseResponseModel = getLeaseDetails(customer);
		validator.validateLeaseTransactionFinder(vendorLeaseResponseModel);
		transactionRequestModel.setLeaseUrl(vendorLeaseResponseModel.getLeaseUrl());

		// get payment account info
		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel = getPaymentAccountInfo(customer);
		validator.validatePaymentAccount(vendorPaymentAccountResponseModel);
		transactionRequestModel.setPaymentAccountUrl(vendorPaymentAccountResponseModel.getUrl());


		return searchByTransactionRequest(customer, transactionRequestModel,
				vendorLeaseResponseModel.getLeaseId());

	}

	private VendorSearchTransactionResponseModel[] searchByTransactionRequest(BCMCustomer bcmCustomer,
			TransactionRequestModel transactionRequestModel, Long leaseId)
		{
		VendorTransactionRequestModel request = transactionRequestBuilder.buildVendorTransactionFinderRequestModel(
				transactionRequestModel);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).searchByTransactionFinder(request,bcmCustomer.getCustomerDataId(), apiNotificationHandler);

	}


	
	private VendorLeaseResponseModel getLeaseDetails(BCMCustomer bcmCustomer)
	{
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).getLeaseInfo(bcmCustomer.getCustomerDataId(),apiNotificationHandler);

	}


	private VendorPaymentAccountResponseModel getPaymentAccountInfo(BCMCustomer bcmCustomer)
	{
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).getPlaidPaymentAccountDetails(bcmCustomer.getCustomerDataId(), apiNotificationHandler);

	}

	private TransactionResponseModel constructTransFinderResponseModel(VendorSearchTransactionResponseModel[] searchVendorResponseArray, String transactionStatus) {
		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		String status = transactionStatus;
		List<TransactionModel> transactions = null;
		
		if(searchVendorResponseArray.length == 1 ) {
			status = AppConstants.TRANSACTION_STATUS_FOUND;
		}
		if(searchVendorResponseArray.length > 1 ) {
			status = AppConstants.TRANSACTION_STATUS_MULTIPLE;			
		}
		transactions = new ArrayList<>();			
		for(VendorSearchTransactionResponseModel searchResponse : searchVendorResponseArray) {
			transactions.add(convertVendorResponse(searchResponse));
		}
		transactionResponseModel.setStatus(status);
		transactionResponseModel.setTransactions(transactions);
		return transactionResponseModel;
	}
	
	private TransactionModel convertVendorResponse(VendorSearchTransactionResponseModel searchResponse)
	{
		return modelMapper.map(searchResponse, TransactionModel.class);
	}

	@Override
	public MatchTransactionResponseModel matchTransaction(Long ucid, String brand,
			MatchTransactionRequestModel matchTransactionRequestModel) {
		log.info("TransactionFinderServiceImpl: match transaction for the customer ucid ", ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid, brand);
		validator.validateCustomer(bcmCustomer);
		
		if (null == bcmCustomer.getTransactionFinderId()) {
			log.error("TransactionFinderServiceImpl: match transaction No Records ucid ", ucid);
			throw new BCMModuleException(BCMModuleExceptionEnum.MATCH_TRX_FINDER_NO_RECORDS);
		}
		VendorMatchTransactionResponseModel[] vendorResponse = processMatchTransactions(bcmCustomer,
				matchTransactionRequestModel);
		MatchTransactionResponseModel[] responseArray = constructMatchTransactionResponse(vendorResponse);
		return responseArray[0];
	}
	private VendorMatchTransactionResponseModel[] processMatchTransactions(BCMCustomer bcmCustomer, MatchTransactionRequestModel matchTransactionRequestModel) {
		VendorMatchTransactionRequestModel request = transactionRequestBuilder.buildVendorMatchTransactionRequestModel(
				matchTransactionRequestModel);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).matchTransaction(request,bcmCustomer.getCustomerDataId(),
				bcmCustomer.getTransactionFinderId(),apiNotificationHandler);

	}
	
	private MatchTransactionResponseModel[] constructMatchTransactionResponse(VendorMatchTransactionResponseModel[] vendorResponseArray) {
		return  new ModelMapper().map(vendorResponseArray, MatchTransactionResponseModel[].class);
		
	}
	
	public TransactionResponseModel searchTransactionDetails(Long ucid, String brand)
			
	{
		log.error("TransactionFinderServiceImpl :  searchTransactionDetails");
		Long transactionId = null;
		String transactionStatus = null;
		VendorSearchTransactionResponseModel[] searchVendorResponse = null;
		TransactionResponseModel transactionResponseModel =null;
		
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(bcmCustomer);
		transactionId = bcmCustomer.getTransactionFinderId();
		if( null != transactionId) {
			searchVendorResponse = searchTransFinderRequest(bcmCustomer, transactionId);
			transactionStatus = getTransactionFinderStatus(bcmCustomer);
			transactionResponseModel = constructTransFinderResponseModel(searchVendorResponse, transactionStatus);
		} else {
			throw new BCMModuleException(BCMModuleExceptionEnum.TRX_FINDER_NOT_FOUND);
		}
		return transactionResponseModel;

	}

	private VendorSearchTransactionResponseModel[] searchTransFinderRequest(BCMCustomer bcmCustomer, Long transactionId)
	{
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).searchTransactionByFinder(bcmCustomer.getCustomerDataId(),
				transactionId, apiNotificationHandler);

	}
	
	
	private void updateTransactionFinder(BCMCustomer bcmCustomer, TransactionRequestModel transactionRequestModel) {
		VendorTransactionRequestModel request = transactionRequestBuilder
				.buildVendorTransactionFinderRequestModel(transactionRequestModel);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);

		boolean isUpdated = vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.updateTransactionFinder(request, bcmCustomer.getCustomerDataId(), bcmCustomer.getTransactionFinderId(), apiNotificationHandler);

		if (isUpdated) {
			log.info("TransactionFinderServiceImpl: updateTransactionFinder, update status: " + isUpdated);

		} else {
			throw new BCMModuleException(BCMModuleExceptionEnum.UPDATE_TRX_FINDER_BCM_EXCEPTION);
		}

	}


	@Override
	public TransactionResponseModel createOrUpdateTransactionFinder(BCMCustomer bcmCustomer,
			TransactionRequestModel transactionRequestModel) {
		log.info("TransactionFinderServiceImpl: createOrUpdateTransactionFinder Start ucid: ", bcmCustomer.getUcId());
		VendorTransactionResponseModel vendorTransactionResponseModel = null;
		String transactionStatus =null;
		VendorLeaseResponseModel vendorLeaseResponseModel = getLeaseDetails(bcmCustomer);
		validator.validateLeaseTransactionFinder(vendorLeaseResponseModel);
		transactionRequestModel.setLeaseId(vendorLeaseResponseModel.getLeaseId());
		transactionRequestModel.setLeaseUrl(vendorLeaseResponseModel.getLeaseUrl());
		transactionRequestModel.setDueDay(Integer.valueOf(vendorLeaseResponseModel.getDueDay()));
		transactionRequestModel.setRentAmount(vendorLeaseResponseModel.getRent());
		if ( null != bcmCustomer.getTransactionFinderId()) {
			updateTransactionFinder(bcmCustomer, transactionRequestModel);
			transactionRequestModel.setTransactionFinderId(bcmCustomer.getTransactionFinderId());
			log.info("TransactionFinderServiceImpl: updateTransactionFinder ucid: ", bcmCustomer.getUcId());
			transactionStatus = getTransactionFinderStatus(bcmCustomer);
		} else {
			vendorTransactionResponseModel = createTransactionFinder(bcmCustomer, transactionRequestModel);
			if( null != vendorTransactionResponseModel ) {
				transactionRequestModel.setTransactionFinderId(vendorTransactionResponseModel.getId());
				transactionStatus = vendorTransactionResponseModel.getStatus();
			}
			log.info("TransactionFinderServiceImpl: createTransactionFinder ucid: ", bcmCustomer.getUcId());
		}
		VendorSearchTransactionResponseModel[] searchVendorResponse = searchByTransactionRequest(bcmCustomer,
				transactionRequestModel, vendorLeaseResponseModel.getLeaseId());
		log.info("TransactionFinderServiceImpl: createOrUpdateTransactionFinder End ucid: ", bcmCustomer.getUcId());
		return constructTransFinderResponseModel(searchVendorResponse, transactionStatus);
	}
	
	@Override
	public VendorTransactionResponseModel createTransactionFinder(BCMCustomer bcmCustomer, TransactionRequestModel transactionRequestModel) {
		log.error("TransactionFinderServiceImpl :  createTransactionFinder");
		Long transtaionFinderId = null;
		// get payment account info
		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel = getPaymentAccountInfo(bcmCustomer);
		validator.validatePaymentAccount(vendorPaymentAccountResponseModel);
		transactionRequestModel.setPaymentAccountUrl(vendorPaymentAccountResponseModel.getUrl());
		VendorTransactionResponseModel vendorResponse = creationTransactionFinderRequest(bcmCustomer,
				transactionRequestModel, transactionRequestModel.getLeaseId());
		// Save TransactionFinderId
		if (null != vendorResponse) {
			transtaionFinderId = vendorResponse.getId();
			bcmCustomer.setTransactionFinderId(transtaionFinderId);
			customerService.updateBcmCustomer(bcmCustomer);

		}
		return vendorResponse;

	}
	
	private String getTransactionFinderStatus(BCMCustomer bcmCustomer) {
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		VendorTransactionResponseModel vendorTransactionResponseModel =  vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).getTransactionFinderById(bcmCustomer.getCustomerDataId(),
				bcmCustomer.getTransactionFinderId(), apiNotificationHandler);
		return null != vendorTransactionResponseModel ? vendorTransactionResponseModel.getStatus() : "";
	}
}
