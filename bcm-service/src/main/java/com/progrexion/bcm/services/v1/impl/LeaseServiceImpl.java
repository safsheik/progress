package com.progrexion.bcm.services.v1.impl;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseOrdersResponseModel;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.LeaseRequestBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.LeaseService;
import com.progrexion.bcm.services.validator.LeaseValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeaseServiceImpl extends BaseBCMService implements LeaseService {

	@Autowired
	private LeaseValidator validator;
	@Autowired
	private LeaseRequestBuilder leaseRequestBuilder;

	@Override
	public LeaseResponseModel getLeaseInfo(Long ucid, String brand)
	{
		log.info("LeaseServiceImpl getLeaseInfo: ", ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(bcmCustomer);
		VendorLeaseResponseModel vendorResponse = getLeaseList(bcmCustomer);
		// Lease not available
		if (null == vendorResponse) {
			log.info("LeaseServiceImpl getLeaseInfo: Lease not found ", ucid);
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_NOT_FOUND);

		}
		return new ModelMapper().map(vendorResponse, LeaseResponseModel.class);
	}

	private VendorLeaseResponseModel getLeaseList(BCMCustomer bcmCustomer)
	{
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).getLeaseInfo(bcmCustomer.getCustomerDataId(),apiNotificationHandler);

	}

	@Override
	public boolean createLease(BCMCustomer bcmCustomer, LeaseRequestModel leaseRequest) {
		boolean isLeaseCreated = false; 
		log.info("LeaseServiceImpl:createLease for the customer");
		if( null == getLeaseList(bcmCustomer))
		{
			validator.validateLeaseRequest(leaseRequest);
			VendorLeaseResponseModel vendorResponse = processLeaseRequest(bcmCustomer, leaseRequest);
			if(null != vendorResponse) isLeaseCreated = true;
		} else {
			log.error("LeaseServiceImpl:createLease Lease Already Availble");
			isLeaseCreated = true;
		}
		return isLeaseCreated;
	}
	
	@Override
	public LeaseResponseModel createOrUpdateLease(Long ucid, String brand, LeaseRequestModel leaseRequest, boolean isSubscribeCheckRequried)
		{

		log.info("LeaseServiceImpl:createLease for the customer ucid [{}]",ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		// Subscription availability check
		if (isSubscribeCheckRequried) {			
			validator.validateCustomer(bcmCustomer);
		}
		if( null != getLeaseList(bcmCustomer)) {
			log.error("LeaseServiceImpl:createLease Lease Already Availble",ucid);
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_ALREADY_AVAILABLE);
		}
		validator.validateLeaseRequest(leaseRequest);
		VendorLeaseResponseModel vendorResponse = processLeaseRequest(bcmCustomer, leaseRequest);
		return new ModelMapper().map(vendorResponse, LeaseResponseModel.class);
	}

	private VendorLeaseResponseModel processLeaseRequest(BCMCustomer bcmCustomer, LeaseRequestModel leaseRequest)
	{
		VendorLeaseRequestModel request = leaseRequestBuilder.buildVendorLeaseRequestModel(leaseRequest);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).createLease(request,bcmCustomer.getCustomerDataId(),apiNotificationHandler);

	}

	/*
	 * 
	 * Get Lease orders List
	 */
	@Override
	public LeaseOrdersResponseModel[] getLeaseOrders(Long ucid, String brand)
	{
		log.info("LeaseServiceImpl getLeaseOrders for the ucid {[]} and brand {[]}",ucid,brand);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid, brand);
		VendorOrdersResponseModel[] vendorResponse =  getLeaseOrdersList(bcmCustomer);
		return new ModelMapper().map(vendorResponse, LeaseOrdersResponseModel[].class);		
	}
	
	@Override
	public List<VendorOrdersResponseModel> getLeaseOrdersByBCMCustomer(BCMCustomer bcmCustomer)
	{
		log.info("LeaseServiceImpl getLeaseOrdersForBCMCustomer of the customer customer Id []", bcmCustomer.getCustomerDataId());
		return Arrays.asList(getLeaseOrdersList(bcmCustomer));	
	}

	private VendorOrdersResponseModel[] getLeaseOrdersList(BCMCustomer bcmCustomer)
	{
		VendorLeaseResponseModel leaseResponseModel = getLeaseList(bcmCustomer);
		if (null == leaseResponseModel) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_NOT_FOUND);
		}
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		VendorOrdersResponseModel[] vendorResponse =  vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.getLeaseOrders(leaseResponseModel.getLeaseId(), bcmCustomer.getCustomerDataId(),
				 apiNotificationHandler);
		if (null == vendorResponse) {
			throw new BCMModuleException(BCMModuleExceptionEnum.LEASE_NOT_FOUND);
		}
		return vendorResponse;
	}
	
	@Override
	public boolean getLeaseInfo(BCMCustomer bcmCustomer) {
		log.info("LeaseServiceImpl getLeaseInfo: bcmCustomer");
		boolean isLeaseAvailable = false;
		VendorLeaseResponseModel response = getLeaseList(bcmCustomer);
		if (null != response) {
			isLeaseAvailable = true;
		}
		return isLeaseAvailable;
	}
}
