package com.progrexion.bcm.services.builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.enums.CustomerActivityEnum;
import com.progrexion.bcm.common.enums.OrderBureauTypeEnum;
import com.progrexion.bcm.common.enums.OrderTypeEnum;
import com.progrexion.bcm.common.enums.ResidentStatusEnum;
import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.common.model.WebServiceLogsModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersReportedResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerActivity;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.CustomerSubscription;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.model.entities.FailedDeprovision;
import com.progrexion.bcm.model.entities.FailedTransactionPull;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.Order;
import com.progrexion.bcm.model.entities.OrderReportedInfo;
import com.progrexion.bcm.model.entities.WebServiceLog;
import com.progrexion.bcm.model.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityDataBuilder {

	@Autowired
	OrderRepository orderRepo;
	/**
	 * This method creates the WebServiceLog entity
	 * @param webServiceLogsModel
	 * @return
	 */
	public WebServiceLog getWebServiceLogsEntity(WebServiceLogsModel webServiceLogsModel) {

		WebServiceLog webServiceLog = new WebServiceLog();
		webServiceLog.setUcid(webServiceLogsModel.getUcid());
		webServiceLog.setHttpMethod(webServiceLogsModel.getHttpMethod());
		webServiceLog.setHttpStatusCode(Long.valueOf(webServiceLogsModel.getHttStatusCode()));
		webServiceLog.setRequestPayload(webServiceLogsModel.getRequestBody());
		webServiceLog.setResponsePayload(webServiceLogsModel.getResponseBody());
		webServiceLog.setUrl(webServiceLogsModel.getUrl());
		webServiceLog.setBrand(webServiceLogsModel.getBrand());
		return webServiceLog;
	}
	
	public BCMCustomer getCustomerEntityForNewUserAccountFlow(BCMCustomer customer, VendorTokenResponseModel tokens) {
		customer.setAccessToken(tokens.getAccessToken());
		customer.setRefreshToken(tokens.getRefreshToken());
		customer.setTokenExpiry(tokens.getExpiresIn());
		customer.setTokenCreatedDate();
		return customer;		
	}

	public ExternalLog buildExternalLogs(BCMCustomer customer, Long vendorId, String url, String request, String response, String httpMethod) {
		ExternalLog externalLog = new ExternalLog();
		externalLog.setUcid(customer.getUcId());
		externalLog.setBrand(customer.getBrand());
		externalLog.setCustomerId(customer.getCustomerDataId());
		externalLog.setVendorId(vendorId);
		externalLog.setUrl(url);
		externalLog.setVendorRequestPayload(request);
		externalLog.setVendorResponsePayload(response);
		externalLog.setHttpMethod(httpMethod);
		return externalLog;
	}
	
	public CustomerActivity buildCustomerActivity(BCMCustomer customer, String requestType) {
		CustomerActivity customerActivity = null;
		CustomerActivityEnum activityEnum = null;
		activityEnum = getActivity(requestType.concat("_CUSTACTIVITY"));
		if(activityEnum != null && activityEnum.isRequired()) {
			customerActivity = new CustomerActivity();
			customerActivity.setCustomer(customer);
			customerActivity.setUcId(customer.getUcId());
			customerActivity.setBrand(customer.getBrand());	
			customerActivity.setActivityUsage(activityEnum.getActivityType());
			return customerActivity;
		}
		log.info("Customer Activity Not Updated for the Request Type : {}", requestType);
		return null;
	}

	private CustomerActivityEnum getActivity(String enumName) {
		CustomerActivityEnum[] enumArray = CustomerActivityEnum.values();
		for(CustomerActivityEnum enumObj : enumArray) {
			if(enumObj.name().equals(enumName)) {
				return enumObj;
			}			
		}
		return null;
	}
	
	public BCMCustomer buildCustomerEntityForNewUser(Long ucid, String brand, String email) {
		BCMCustomer customer = new BCMCustomer();
		customer.setUcId(ucid);
		customer.setCustEmail(email);
		customer.setBrand(brand);
		customer.setResidentStatus(ResidentStatusEnum.CUSTOMER_CREATION_INITIATED.getStatus());
		return customer;		
	}
	
	public CustomerSubscription buildCustomerSubscription(BCMCustomer bcmCustomer, Long subscriptionId, SubscriptionStatusEnum subscriptionStatus) {
		CustomerSubscription customerSubscription = new CustomerSubscription();
		customerSubscription.setSubscriptionId(subscriptionId);
		customerSubscription.setCustomer(bcmCustomer);
		customerSubscription.setStatus(subscriptionStatus);
		return customerSubscription;		
	}

	public Order buildOrderEntity(VendorOrdersResponseModel rtOrder, BCMCustomer bcmCustomer, OrderTypeEnum orderType) {
		Order bcmOrder = new Order();
		bcmOrder.setRtOrderId(Long.valueOf(rtOrder.getOrderId()));
		Order existingBCMOrder = orderRepo.findByRtOrderId(bcmOrder.getRtOrderId());
		if(existingBCMOrder != null) {
			bcmOrder = existingBCMOrder;
		} else {
			bcmOrder.setOrderType(orderType);
			bcmOrder.setBcmCustomer(bcmCustomer);
			bcmOrder.setFee(String.valueOf(rtOrder.getFee()));
			bcmOrder.setRent(String.valueOf(rtOrder.getRent()));
			bcmOrder.setStatus(rtOrder.getStatus());
			bcmOrder.setOther(String.valueOf(rtOrder.getOther()));
			bcmOrder.setPaidfor(rtOrder.getPaidFor());
			bcmOrder.setTotal(String.valueOf(rtOrder.getTotal()));
		}
		bcmOrder.setCreatedDate();
		buildOrderReportedInfoEntityList(rtOrder.getOrdersReportResponse(), bcmOrder);
		return bcmOrder;		
	}
	
	private void buildOrderReportedInfoEntityList(VendorOrdersReportedResponseModel rtOrdersReported, Order bcmOrder ) {	
	
		List<OrderBureauTypeEnum> existingBureauTypes = bcmOrder.getOrderReportedInfoList().stream()
				.map(OrderReportedInfo::getBureauType).collect( Collectors.toList());		
		List<OrderBureauTypeEnum> allBureauTypes = Arrays.asList(OrderBureauTypeEnum.values());	
		if(existingBureauTypes.isEmpty()) {
			return;
		}
		for(OrderBureauTypeEnum bureauTypeEnum : allBureauTypes) {
			if( !existingBureauTypes.contains(bureauTypeEnum) ) {
				getReportedAtByBureauType (rtOrdersReported, bureauTypeEnum, bcmOrder);				
			}			
		}	
	}
	
	private void getReportedAtByBureauType(VendorOrdersReportedResponseModel rtOrdersReported, OrderBureauTypeEnum bureauType, Order bcmOrder) {
		String reportedAt = null;
		OrderReportedInfo orderReportedInfo = null;		
		if(bureauType.equals(OrderBureauTypeEnum.TRANS_UNION) ) {
			reportedAt = rtOrdersReported.getTransUnion();
		}
		if(bureauType.equals(OrderBureauTypeEnum.EQUIFAX) ) {
			reportedAt =  rtOrdersReported.getEquifax();
		}
		if(bureauType.equals(OrderBureauTypeEnum.EXPERIAN) ) {
			reportedAt = rtOrdersReported.getExperian();	
		}		
		if( StringUtils.isNotBlank(reportedAt) ) {
			orderReportedInfo = new OrderReportedInfo();
			orderReportedInfo.setOrder(bcmOrder);
			orderReportedInfo.setBureauType(bureauType);
			orderReportedInfo.setReportedAt(LocalDate.parse(reportedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			bcmOrder.getOrderReportedInfoList().add(orderReportedInfo);
		}
	}

	public FailedTransactionPull buildFailedTransactionPull(BCMCustomer bcmCustomer, Job job, String errorMessage) {
		FailedTransactionPull failedTransactionPull = new FailedTransactionPull();
		failedTransactionPull.setCustomer(bcmCustomer);
		failedTransactionPull.setJob(job);
		failedTransactionPull.setErrorMessage(errorMessage.getBytes());
		return failedTransactionPull;
		
	}
	public FailedDeprovision buildCustomerSubscriptionFailedDeprovision(BCMCustomer bcmCustomer, String pbsMessage, String errorMessage ) {
		FailedDeprovision failedDeprovision = new FailedDeprovision();
		failedDeprovision.setUcId(bcmCustomer.getUcId());
		failedDeprovision.setBrand(bcmCustomer.getBrand());
		failedDeprovision.setPbsMessage(pbsMessage);
		failedDeprovision.setErrorMessage(errorMessage);
		return failedDeprovision;
	}

	public CustomerOrderProcess buildCustomerOrderProcessEntity(BCMCustomer bcmCustomer, Job job,
			OrderProcessStatusEnum orderStatus) {
		CustomerOrderProcess customerOrderProcess = new CustomerOrderProcess();
		customerOrderProcess.setJob(job);
		customerOrderProcess.setCustomer(bcmCustomer);
		customerOrderProcess.setStatus(orderStatus);
		return customerOrderProcess;
	}

	public ProcessOrderMessageDTO buildProcessOrderMessageDto(CustomerOrderProcess customerOrderProcess) {
		ProcessOrderMessageDTO processOrderMessageDTO = new ProcessOrderMessageDTO();
		processOrderMessageDTO.setCustomerDataId(customerOrderProcess.getCustomer().getCustomerDataId());
		processOrderMessageDTO.setJobId(customerOrderProcess.getJob().getJobId());
		processOrderMessageDTO.setCustomerOrderProcessId(customerOrderProcess.getId());
		return processOrderMessageDTO;
	}
}
