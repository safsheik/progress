package com.progrexion.bcm.services.v1.impl;

import java.util.Optional;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.enums.ResidentStatusEnum;
import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.CreateUserResponseModel;
import com.progrexion.bcm.common.model.v1.CustomerStatusResponseModel;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.common.model.v1.UpdateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUpdateUserRequestModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerSubscription;
import com.progrexion.bcm.renttrack.client.services.AccessTokenProviderService;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.v1.LeaseService;
import com.progrexion.bcm.services.v1.SubscriptionService;
import com.progrexion.bcm.services.validator.LeaseValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl extends BaseBCMService implements CustomerService {


	@Autowired
	private AccessTokenProviderService rtTokenProvider;
	@Autowired
	private EntityDataBuilder entityBuilder;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private LeaseService leaseService;
	@Autowired
	private LeaseValidator leaseValidator;
	
	public BCMCustomer createBcmCustomer(BCMCustomer bcmCustomer) {
		BCMCustomer customerCreated = null;
		try
		{
			customerCreated = customerRepository.save(bcmCustomer);
		}
		catch(Exception e)
		{
			throw new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION);
		}
		return customerCreated;
	}

	public BCMCustomer updateBcmCustomer(BCMCustomer bcmCustomer) {
		BCMCustomer updatedCustomer = null;
		Optional<BCMCustomer> opt = customerRepository.findById(bcmCustomer.getCustomerDataId());
		if(opt.isPresent()) {
			bcmCustomer.setModifiedDate();
			updatedCustomer = customerRepository.saveAndFlush(bcmCustomer);

		}
		else {
			throw new BCMModuleException(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND);
		}
		return updatedCustomer;
	}
	
	public BCMCustomer getCustomerById(Long customerId) {
		BCMCustomer bcmCustomer = null;
		Optional<BCMCustomer> opt = customerRepository.findById(customerId);
		if (opt.isPresent()) {
			bcmCustomer = opt.get();
		}
		return bcmCustomer;
	}

	@Override
	public CreateUserResponseModel createUserAccount(Long ucid, String brand, CreateUserRequestModel reqModel) {
		log.info("CustomerServiceImpl:createUserAccount: Start", ucid);
		CreateUserResponseModel createUserResponseModel = new CreateUserResponseModel();
		boolean istenantLeaseCreated = false;
		boolean tenantUserCreated = false;
		boolean isRTSubscriptionDone = false;
		boolean isDOBUpdated = false;
		boolean isAccessTokenCreated = false;
		VendorSubscriptionResponseModel vendorSubscriptionModel = null;
		boolean isBCMSubscriptionAdded = false;
		boolean isTenantCreationDone = false;
		SubscriptionResponseModel[] rtSubs = null;
		Long rtSubsId = 0L;

		BCMCustomer bcmCustomer = null;
		ucid = fetchCustomerMasterParentUCID(ucid);
		bcmCustomer = getCustomerByEmail(reqModel.getEmail());
		if (bcmCustomer == null) {
			bcmCustomer = entityBuilder.buildCustomerEntityForNewUser(ucid, brand, reqModel.getEmail());
			bcmCustomer = createBcmCustomer(bcmCustomer);
			log.info("CustomerServiceImpl:createUserAccount: successful", ucid);
		} else if (bcmCustomer.getResidentStatus() == ResidentStatusEnum.CUSTOMER_CREATED.getStatus()) {
			log.info("CustomerServiceImpl:createUserAccount: An account has already been created for this ucid: ",
					ucid);
			tenantUserCreated = true;
		}
		
		leaseValidator.validateLeaseRequest(reqModel.getLeaseData());
		
		if (StringUtils.isEmpty(reqModel.getType()))
			reqModel.setType(AppConstants.DEFAULT_RENTTRACK_USER_ACCOUNT_TYPE);
		if (StringUtils.isEmpty(reqModel.getPassword()))
			reqModel.setPassword(AppConstants.DEFAULT_PREFIX + bcmCustomer.getUcId());
	//// False tolerance case check
		if (!tenantUserCreated) {
			tenantUserCreated = createTenantUser(bcmCustomer, reqModel);

		}

		if (bcmCustomer.getAccessToken() == null && tenantUserCreated) {
			isAccessTokenCreated = createTenantAccessToken(bcmCustomer, reqModel);
			log.info("CustomerServiceImpl:createUserAccount: User Token Creation.");

		} else if (bcmCustomer.getAccessToken() != null && tenantUserCreated){
			isAccessTokenCreated = true;
		}

		if (isAccessTokenCreated) {
			isDOBUpdated = updateTenantDOB(bcmCustomer, reqModel);
			log.info("CustomerServiceImpl:createUserAccount: User DOB Updated.");
		}

		rtSubs = leaseValidator.getCustomerRTSubscription(bcmCustomer);
		if(rtSubs.length != 0) {
			isRTSubscriptionDone = true;
			rtSubsId = rtSubs[0].getSubscriptionId();
		}

		if (isDOBUpdated && !isRTSubscriptionDone) {
			vendorSubscriptionModel = processSubscriptionRequest(bcmCustomer, vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK));
			log.info("CustomerServiceImpl: Creating subscription for customer");
			 isRTSubscriptionDone = true;
		     isTenantCreationDone = true;
			rtSubsId = vendorSubscriptionModel.getSubscriptionId();
			
		} else if(!isDOBUpdated) {
			log.error("CustomerServiceImpl:createUserAccount an account not done successfully");
			throw new BCMModuleException("CreateUserAccount an account not done successfully for this ucid: " + bcmCustomer.getUcId(),
				BCMModuleExceptionEnum.TENANT_USER_NOT_CREATED);
		}
		isBCMSubscriptionAdded = addBCMSubscription(bcmCustomer, rtSubsId);
		boolean leaseExists = leaseService.getLeaseInfo(bcmCustomer);
		if (isBCMSubscriptionAdded && !leaseExists) {
			istenantLeaseCreated = createTenantLease(bcmCustomer, reqModel);
			log.info("CustomerServiceImpl:createUserAccount : Lease Created");
			isTenantCreationDone = true;
		} else if(leaseExists) {
			istenantLeaseCreated = true;
		}
		if(!isBCMSubscriptionAdded || !istenantLeaseCreated){
			log.error("CreateUserAccount : user creation not completed successfully for this ucid: " + bcmCustomer.getUcId());
			throw new BCMModuleException("CreateUserAccount : user creation not completed successfully for this ucid: " + bcmCustomer.getUcId(),
					BCMModuleExceptionEnum.TENANT_USER_NOT_CREATED);
		} else if(!isTenantCreationDone) {
			log.error("CustomerServiceImpl:createUserAccount an account has already been created");
			throw new BCMModuleException("An account has already been created for this ucid: " + bcmCustomer.getUcId(),
					BCMModuleExceptionEnum.CUSTOMER_INFO_ALREADY_EXISTS);
		}
		
		createUserResponseModel.setStatus(istenantLeaseCreated);
		log.info("CustomerServiceImpl:createUserAccount: End", ucid);
		return createUserResponseModel;
	}
	
	private boolean createTenantLease(BCMCustomer bcmCustomer, CreateUserRequestModel reqModel) {
		log.info("CustomerServiceImpl: Subcription has been created for the customer ucid ", bcmCustomer.getUcId());
		return leaseService.createLease(bcmCustomer, reqModel.getLeaseData());
	}
	
	private boolean updateTenantDOB(BCMCustomer bcmCustomer, CreateUserRequestModel reqModel ) {
			UpdateUserRequestModel updateUserRequest = new UpdateUserRequestModel();
			updateUserRequest.setDob(reqModel.getDob());
			return processUpdateUserRequest(bcmCustomer, updateUserRequest);
	}
	
	private boolean createTenantAccessToken(BCMCustomer bcmCustomer, CreateUserRequestModel reqModel) {
		log.info("createTenantAccessToken: getFirstTimeUserTokens ");
		VendorTokenResponseModel tokens = rtTokenProvider.getFirstTimeUserTokens(reqModel.getEmail(),
				reqModel.getPassword());
		log.info("CustomerServiceImpl:createUserAccount: User Account specific tokens fetched.");
		bcmCustomer = entityBuilder.getCustomerEntityForNewUserAccountFlow(bcmCustomer, tokens);
		updateBcmCustomer(bcmCustomer);
		return true;
	}
	
	private boolean createTenantUser(BCMCustomer bcmCustomer, CreateUserRequestModel reqModel) {
		boolean userCreateDone = false;
		VendorCreateUserResponseModel vendorResponse = processCreateUserRequest(reqModel, bcmCustomer);
		log.info("CustomerServiceImpl:createUserAccount: User Account created in RentTrack: [{}]", vendorResponse);
		if (vendorResponse.getId() != null) {
			bcmCustomer.setResidentStatus(ResidentStatusEnum.CUSTOMER_CREATED.getStatus());
			updateBcmCustomer(bcmCustomer);
			log.info("CustomerServiceImpl:createUserAccount: update", bcmCustomer.getUcId());
			userCreateDone = true;
		} else {
			log.error(BCMModuleExceptionEnum.CREATE_TENANT_BCM_EXCEPTION.getDescription());
			throw new BCMModuleException(BCMModuleExceptionEnum.CREATE_TENANT_BCM_EXCEPTION);

		}
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		apiNotificationHandler.addActivity("CREATE_TENANT");
		log.info("CustomerServiceImpl:createUserAccount: User Account Activity saved in database.");
		return userCreateDone;
	}
	
	private VendorCreateUserResponseModel processCreateUserRequest(CreateUserRequestModel svcRequest,BCMCustomer bcmCustomer) {
		VendorCreateUserRequestModel vendorRequest = modelMapper.map(svcRequest, VendorCreateUserRequestModel.class);
		
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer, BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).createUserAccount(vendorRequest,apiNotificationHandler);
	}
	
	private Boolean processUpdateUserRequest(BCMCustomer customer, UpdateUserRequestModel svcRequest) {
		VendorUpdateUserRequestModel vendorRequest = modelMapper.map(svcRequest, VendorUpdateUserRequestModel.class);
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer, BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).updateTenantUserDetails(customer.getCustomerDataId(), vendorRequest,apiNotificationHandler);
	}
	
	private VendorSubscriptionResponseModel processSubscriptionRequest(BCMCustomer bcmCustomer,
			VendorProcessor vendorProccessor) {
		return subscriptionService.processSubscriptionRequest(bcmCustomer, vendorProccessor);
	}
	@Override
	public BCMCustomer getCustomerByEmail(String custEmail) {
		BCMCustomer customer = null;
		Optional<BCMCustomer> opt = customerRepository.findByCustEmail(custEmail);
		if(opt.isPresent()) {
		customer = opt.get();
		}
		return customer;
	}


	@Override
	public BCMCustomer getCustomerByUcIdAndBrand(Long ucId, String brand) {
		BCMCustomer customer = null;
		Optional<BCMCustomer> opt = customerRepository.findByUcIdAndBrand(ucId, brand);
		if(opt.isPresent()) {
		customer = opt.get();
		}
		return customer;
	}


	@Override
	public Long getTransactionFinderId(Long ucId, String brand) {
		Long trxId = null;
		BCMCustomer customer = getCustomerByUcIdAndBrand(ucId,brand);
		if(customer != null)
		{
			trxId = customer.getTransactionFinderId();
		}
		return trxId;
	}

	@Override
	public BCMCustomer getCustomerByUcId(Long ucId) {
		BCMCustomer customer = null;
		Optional<BCMCustomer> opt = customerRepository.findByUcId(ucId);
		if(opt.isPresent()) {
		customer = opt.get();
		}
		return customer;
	}
	
	public boolean addBCMSubscription(BCMCustomer bcmCustomer, Long scriptionId) {
		CustomerSubscription bcmSubscription = new CustomerSubscription();
		boolean isBCMSubscriptionDone = false;
		try {
			log.info("CustomerServiceImpl: created BCMCustomerSubscription: User create the subscriptions.");
			bcmSubscription = customerSubscriptionRepository.findFirstByCustomerOrderByCreatedDateDesc(bcmCustomer);
			if (null == bcmSubscription || SubscriptionStatusEnum.INACTIVE.equals(bcmSubscription.getStatus())) {
				bcmSubscription = entityBuilder.buildCustomerSubscription(bcmCustomer, scriptionId, SubscriptionStatusEnum.ACTIVE);
				customerSubscriptionRepository.save(bcmSubscription);
			}
			isBCMSubscriptionDone = true;
		} catch (Exception e) {
			return isBCMSubscriptionDone;
		}
		return isBCMSubscriptionDone;
	}
	
	@Override
	public CustomerStatusResponseModel getCustomerStatusInfo(Long ucid, String brand) {
		log.info("CustomerServiceImpl:getCustomerStatusInfo: ucid: ", ucid);
		CustomerStatusResponseModel customerStatusResponseModel = new CustomerStatusResponseModel();
		String cutomerStatus = null;
		cutomerStatus = fetchCustomerStatusInfo(ucid, brand);
		if (null != cutomerStatus) {
			customerStatusResponseModel.setStatus(cutomerStatus);
		}
		log.info("CustomerServiceImpl:getCustomerStatusInfo: End", ucid);
		return customerStatusResponseModel;
	}	

}
