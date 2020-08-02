package com.progrexion.bcm.services.v1.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.enums.UtilityEnum;
import com.progrexion.bcm.common.enums.UtilityStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.UtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.UtilityRequestBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.UtilityService;
import com.progrexion.bcm.services.validator.UtilityValidator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UtilityServiceImpl extends BaseBCMService implements UtilityService {

	@Autowired
	private UtilityValidator validator;
	@Autowired
	private UtilityRequestBuilder builder;

	@Override
	public UtilityResponseModel[] getAllUtilities(Long ucid, String brand)
			 {
		log.info("UtilityServiceImpl: getAllUtilities for the customer ucid [{}]",ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		VendorUtilityResponseModel[] vendorResponse = getAllUtilitiesFromRT(bcmCustomer);
		return constructGetAllUtilitiesResponse(vendorResponse);
	}

	@Override
	public UtilityDetailsResponseModel[] getUtilityDetails(Long ucid, String brand, Long utilityTradeLineId)
			 {
		log.info("UtilityServiceImpl: getUtilityDetails for the customer ucid [{}]",ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		VendorUtilityDetailsResponseModel[] vendorResponse = getUtilityDetailsFromRT(bcmCustomer, utilityTradeLineId);
		return constructGetUtilityDetailsResponse(vendorResponse);
	}
	
	
	private VendorUtilityResponseModel[] getAllUtilitiesFromRT(BCMCustomer bcmCustomer)  {
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.getAllUtilities(bcmCustomer.getCustomerDataId(),apiNotificationHandler);

	}
	
	private UtilityResponseModel[] constructGetAllUtilitiesResponse(VendorUtilityResponseModel[] vendorResponseArray) {
		return modelMapper.map(vendorResponseArray, UtilityResponseModel[].class);

	}
	
	private VendorUtilityDetailsResponseModel[] getUtilityDetailsFromRT(BCMCustomer bcmCustomer, Long utilityTradeLineId)  {
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.getUtilityDetails(bcmCustomer.getCustomerDataId(),utilityTradeLineId,apiNotificationHandler);

	}

	private UtilityDetailsResponseModel[] constructGetUtilityDetailsResponse(VendorUtilityDetailsResponseModel[] vendorResponse) {
		return modelMapper.map(vendorResponse, UtilityDetailsResponseModel[].class);
	}

	@Override
	public boolean updateUtiltyStatus(Long ucid, String brand, UtilityStatusRequestModel request) {
		log.info("UtilityServiceImpl: updateUtiltyStatus for the customer ucid [{}]",ucid);
		BCMCustomer bcmCustomer = fetchLatestActiveCustomer(ucid,brand);
		request.setStatus(UtilityStatusEnum.UTILITY_STATUS_VALUE.getStatus());
		validator.validateUtiltiStatusRequestModel(request);
        return processUpdateUtiltyStatus(bcmCustomer, request);

	}
	
	private boolean processUpdateUtiltyStatus(BCMCustomer bcmCustomer, UtilityStatusRequestModel request) {
		VendorUtilityStatusRequestModel vendorRequest = builder.buildUtiltyStatusRequestModel(request);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		String utilEnumValue = getUtilEnumValue(request.getOptFor());
		if(utilEnumValue == null) {
			throw new BCMModuleException(BCMModuleExceptionEnum.PATCH_UTIL_INVALID_UTIL_TYPE);
		}
		boolean isUpdated = vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.updateUtiltyStatusRequest(bcmCustomer.getCustomerDataId(), utilEnumValue, vendorRequest, apiNotificationHandler);
		if(isUpdated)
		{
		log.info("UtilityServiceImpl: Customer ucid [{}] opted for the utility [{}]",bcmCustomer.getCustomerDataId() ,utilEnumValue);		
		}
		return isUpdated;
	}
	
	private String getUtilEnumValue(String utilType) {
		UtilityEnum[] enumArray = UtilityEnum.values();
		for(UtilityEnum enumObj:enumArray)
		{
			if(enumObj.name().equals(utilType))
			{
				return enumObj.getUtilityType();
			}			
		}
		return null;
	}	
	
	@Override
	public List<VendorOrdersResponseModel> getUtilityOrdersByBCMCustomer(BCMCustomer bcmCustomer) {
		List<VendorOrdersResponseModel> utilityOrders = new ArrayList<>();
		VendorUtilityResponseModel[] utilities =  getAllUtilitiesFromRT(bcmCustomer);
		if(utilities.length == 0) {
			return utilityOrders;
		}
		List<VendorUtilityResponseModel> activeUtilities = getActiveUtilities(utilities);
		if(activeUtilities.isEmpty()) {
			return utilityOrders;
		}
		return getUtilOrdersForActiveUtilities(activeUtilities, bcmCustomer);
	}
	
	private List<VendorUtilityResponseModel> getActiveUtilities(VendorUtilityResponseModel[] utilities) {
		List<VendorUtilityResponseModel> activeUtilities = new ArrayList<>();
		for(VendorUtilityResponseModel utility : utilities) {
			if( StringUtils.equals(utility.getStatus(), UtilityStatusEnum.UTILITY_STATUS_VALUE.getStatus())) {
				activeUtilities.add(utility);
			}
		}	
	return activeUtilities;
	}
	
	private List<VendorOrdersResponseModel> getUtilOrdersForActiveUtilities(List<VendorUtilityResponseModel> activeUtilities, BCMCustomer bcmCustomer) {
		List<VendorOrdersResponseModel> utilityOrders = new ArrayList<>();
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		for(VendorUtilityResponseModel activeUtility : activeUtilities) {
			
			List<VendorOrdersResponseModel> utilityOrdersfromRT = vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
					.getUtilityOrders(bcmCustomer.getCustomerDataId(), activeUtility.getId(), apiNotificationHandler);
			
			if(!utilityOrdersfromRT.isEmpty()) {
				utilityOrders.addAll(utilityOrdersfromRT);
			}
		}
		return utilityOrders;
	}
	
}
