package com.progrexion.bcm.renttrack.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.config.BCMLoadPropOnStartUp;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUpdateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.properties.RentTrackAPIConfigProperties;
import com.progrexion.bcm.common.utils.BCMExceptionUtils;
import com.progrexion.bcm.common.utils.CommonUtils;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.common.webclients.utils.WebClientHttpRequestHeader;
import com.progrexion.bcm.common.webclients.utils.WebClientUtils;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserRequestModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserResponseModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;
import com.progrexion.bcm.renttrack.client.services.AccessTokenProviderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RentTrackClient {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebClient rentTrackWebClient;

	@Autowired
	private WebClientHttpRequestHeader webClientHttpRequestHeader;

	@Autowired
	private WebClientErrorHandler webClientErrorHandler;

	@Autowired
	private AccessTokenProviderService rtTokenProvider;

	@Autowired
	private RentTrackAPIConfigProperties apis;
	
	@Autowired
	private BCMLoadPropOnStartUp startupProperty;
	
	@Autowired
	private CustomerMasterConfigProperties property;

	private SubscriptionApiModel createSubsReqObj;
	private LeaseApiModel createLeaseReqObj;
	private PaymentAccountApiModel createPaymentAccountReqObj;
	private Long idValue;
	private String idValueString;
	private VendorAPINotification apiNotificationClient;
	private TransactionApiModel transactionFinderApiModelObj;
	private MatchTransactionArrayApiModel matchTTrxReqObj;
	private UtilityStatusApiModel utilityStatusObj;
	private VendorUpdateUserRequestModel vendorUpdateUserRequestObj;
	public static final String CUSTOMER_ID_STRING = "customerId";
	public static final String API_NODIFACTION_HANDLER_STRING = "apiNotificationHandler";
	
	public VendorSubscriptionResponseModel createSubscription(SubscriptionApiModel request, Long customerId,  VendorAPINotification apiNotification) {
		String response = null;
		String uri = apis.getCreateSubscription();
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.SUBSCRIPTION;
		try {
			createSubsReqObj = request;
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.POST)
					.uri(uri).syncBody(request).exchange().block().bodyToMono(String.class).block();
			
			return objectMapper.readValue(response, VendorSubscriptionResponseModel.class);

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				log.info("RT Unauthorized - 401 Exception occurred. The access token provided has expired while invoking RentTrack createSubscription");
				return (VendorSubscriptionResponseModel) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
	
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.createSubscription, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.SUBSCRIPTION_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, apis.getCreateUser(),request, response, AppConstants.HTTP_METHOD_POST);
		}

	}

	private Object getRentTrackRefreshedToken(Long customerId, RentTrackClientRequestTypesEnum reqType) {
		Object response = new Object();
		try {
			rtTokenProvider.refreshTokensOfUser(customerId,reqType);

			switch (reqType) {
				case SUBSCRIPTION:
					response = createSubscription(createSubsReqObj, customerId, apiNotificationClient);
					break;				
				case CREATE_LEASE:
					response = createLease(createLeaseReqObj, customerId, apiNotificationClient);
					break;					
				case PAYMENT_ACCOUNT:
					response = createPlaidPaymentAccount(createPaymentAccountReqObj, customerId, apiNotificationClient);
					break;
				case GET_LEASE_LIST:
					response = getLeaseList(customerId, apiNotificationClient);
					break;	
				case GET_LEASE_DETAILS:
					response = getLeaseDetails(idValue, customerId, apiNotificationClient);
					break;	
				case GET_PAYMENT_ACCOUNT:
					response = getPlaidPaymentAccountDetails(customerId, apiNotificationClient);
					break;
				case CREATE_TRX_FINDER:
					response = createTransactionFinder(transactionFinderApiModelObj, customerId, apiNotificationClient);
					break;
				case SEARCH_TRX_FINDER:
					response = searchTransactionInfoByFinder(transactionFinderApiModelObj, customerId, apiNotificationClient);
					break;
				case MATCH_TRX_FINDER:
					response = matchTransaction(matchTTrxReqObj, customerId, idValue, apiNotificationClient);
					break;
				case SEARCH_TRX_ID:
					response = searchTransactionByFinder(transactionFinderApiModelObj, customerId, apiNotificationClient);
					break;
				case GET_LEASE_ORDERS_LIST:
					response = getLeaseOrders(idValue, customerId, apiNotificationClient);
					break;					
				case GET_ALL_UTILITIES:
					response = getAllUtilities(customerId, apiNotificationClient);
					break;
				case GET_UTILBY_TRDID:
					response = getUtilityDetails(customerId, idValue, apiNotificationClient);
					break;
				case PATCH_UTIL:
					response = updateUtiltyStatus(customerId, idValueString, utilityStatusObj, apiNotificationClient);
					break;
				case GET_SUBSCRIPTIONS:
					response = getSubscriptionsInfo(customerId, apiNotificationClient);
					break;
				case UPDATE_TRX_FINDER:
					response = updateTransactionFinder(transactionFinderApiModelObj, idValue, customerId, apiNotificationClient);
					break;
				case PLAID_RECONNECTION:
					response = reconnectPlaid(customerId, idValue, apiNotificationClient, RentTrackClientRequestTypesEnum.PLAID_RECONNECTION.getName());
					break;
				case PLAID_RECONNECTION_PATCH:
					response = reconnectPlaid(customerId, idValue, apiNotificationClient, RentTrackClientRequestTypesEnum.PLAID_RECONNECTION_PATCH.getName());
					break;
				case PATCH_TENANT_USER:
					response = updateTenantUserDetails(customerId, vendorUpdateUserRequestObj, apiNotificationClient);
					break;
				case GET_UTILITY_ORDERS_LIST:
					response = getUtilityOrders(idValue, customerId, apiNotificationClient);
					break;
				case GET_TRX_FINDER:
					response = getTransactionFinder(customerId, idValue, apiNotificationClient);
					break;
				default:
					break;

			}
		}
		catch (BCMModuleException bcmEx) {
			throw bcmEx;		
		}
		catch (Exception e) {
			log.info("Exception while invoking RentTrack.getRentTrackRefreshedToken, ErrorMessage {}",ExceptionUtils.getRootCauseMessage(e));
			BCMModuleExceptionEnum exceptionEnum = CommonUtils.getExceptionEnum(reqType.name()+"_REFRESH_TOKEN_BCM_EXCEPTION");
			if(null == exceptionEnum)
				exceptionEnum = BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION;
			throw new BCMModuleException(exceptionEnum);
			
		}
		return response;
	}
	
	
	private Object refreshTokenAndRetry(RentTrackClientRequestTypesEnum reqType, Map<String, Object> params) {
		Object response = new Object();
		try {
			
			rtTokenProvider.refreshTokensOfUser((Long) params.get(CUSTOMER_ID_STRING), reqType);

			switch (reqType) {					
				case SUBSCRIPTIONS_GET_ALL_ACTIVE:
					response = getActiveSubscriptions((Long) params.get(CUSTOMER_ID_STRING), 
								(VendorAPINotification) params.get(API_NODIFACTION_HANDLER_STRING));
					break;
				
				case SUBSCRIPTIONS_DELETE:
					response = deleteSubscription((Long) params.get(CUSTOMER_ID_STRING), 
								(Long) params.get("id"), 
								(VendorAPINotification) params.get(API_NODIFACTION_HANDLER_STRING));
					break;
	
				default:
					log.error("Cannot perform refresh and retry for unknown request type: [{}]", reqType.toString());
					//throw Exception("Cannot perform refresh and retry for unknown request type: " + reqType.toString());//?
					break;
			}

		} catch (BCMModuleException e) {
			throw e;
		} catch (Exception e) {
			log.error("Exception while invoking RentTrack.refreshTokenAndRetry. Caused by: {}", ExceptionUtils.getRootCauseMessage(e));
			throw BCMExceptionUtils.generateBCMModuleGeneralException(e);
		}
		return response;
	}

	private WebClient getRentTrackWebClient(String token) {
		rentTrackWebClient = WebClient.builder().filter(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter())
				.baseUrl(apis.getBaseUrl())
				.defaultHeaders(headers -> webClientHttpRequestHeader.configureRentTrackHeaders(headers, token))
				.build();
		return rentTrackWebClient;
	}

	public RTCreateUserResponseModel createUserAccount(RTCreateUserRequestModel request,VendorAPINotification apiNotification)
			{
		
		String response = null;
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.CREATE_TENANT;
		try {
			request.setHoldingId(request.getResidentId());
			log.info("CreateUser Input: [{}]", request);
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getPartnerAccessToken());
			response = rentTrackWebClient.method(HttpMethod.POST)
								.uri(apis.getCreateUser())
								.syncBody(request).exchange().block().bodyToMono(String.class).block();

			return objectMapper.readValue(response, RTCreateUserResponseModel.class);

		} catch (BCMWebClientResponseException wcEx) {
			log.error("BCMModuleException while calling RentTrack CreateUser. ErrorMessage: {}, {}",
					wcEx.getErrorMessage(), wcEx);
			
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {			
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.CreateUser, ErrorMessage: {}, {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.CREATE_TENANT_BCM_EXCEPTION);
		}
		finally {
			notify(apiNotification, apis.getCreateUser(),request, response, AppConstants.HTTP_METHOD_POST);
		}
	}

	public Boolean updateTenantUserDetails(Long customerId,  VendorUpdateUserRequestModel request,VendorAPINotification apiNotification)
	{
		vendorUpdateUserRequestObj = request;
		String response = null;
		apiNotificationClient = apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.PATCH_TENANT_USER;
		try {
			log.info("PatchTenantUser Input: [{}]", request);
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.PATCH).uri(apis.getUpdateUser()).syncBody(request)
					.exchange().block().bodyToMono(String.class).block();

			// PATCH API currently responds with HttpStatus 204 and Blank response body,
			// which can cause NPE here if we try to parse it.
			log.info("PatchTenantUser Completed: [{}]", response);

			return true;

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				getRentTrackRefreshedToken(customerId, reqType);
				return true;
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("BCMModuleException while calling RentTrack PatchTenantUser, ErrorMessage: {}, {}",
					ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.PATCH_TENANT_USER_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, apis.getUpdateUser(), request, response, AppConstants.HTTP_METHOD_PATCH);
		}
	}

	public VendorLeaseResponseModel createLease(LeaseApiModel request, Long customerId,VendorAPINotification apiNotification)
		{
		String responseStatus = null;
		createLeaseReqObj = request;
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.CREATE_LEASE;
		try {

			log.info("RentTrackClient createLease");
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));

			responseStatus = rentTrackWebClient.method(HttpMethod.POST).uri(apis.getCreateLease()).syncBody(request)
					.exchange().block().bodyToMono(String.class).block();

			return objectMapper.readValue(responseStatus, VendorLeaseResponseModel.class);
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorLeaseResponseModel) getRentTrackRefreshedToken(customerId,  reqType);
			}
			responseStatus = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		}
		catch (Exception ex) {			
			responseStatus = ex.getMessage();
			log.error("BCMModuleException while calling RentTrack PatchTenantUser, ErrorMessage: {}, {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.CREATE_LEASE_BCM_EXCEPTION);
		}
		finally {
			notify(apiNotification, apis.getCreateLease(),request, responseStatus, AppConstants.HTTP_METHOD_POST);
		}

	}

	
	public VendorLeaseResponseModel getLeaseList(Long customerId, VendorAPINotification apiNotification) {
		String getLeaseResponse = null;
		VendorLeaseResponseModel[] leaseArray = null;
		VendorLeaseResponseModel vendorLeaseResponseModel = null;
		VendorLeaseResponseModel vendorLeaseDetails = null;
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_LEASE_LIST;
		try {

			log.info("RentTrackClient getLeaseList");
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));

			getLeaseResponse = rentTrackWebClient.method(HttpMethod.GET).uri(apis.getCreateLease()).exchange().block()
					.bodyToMono(String.class).block();

			if (StringUtils.isNotBlank(getLeaseResponse)) {

				leaseArray = objectMapper.readValue(getLeaseResponse, VendorLeaseResponseModel[].class);

				vendorLeaseResponseModel = getActiveLease(leaseArray);

				if (null != vendorLeaseResponseModel) {

					vendorLeaseDetails = getLeaseDetails(vendorLeaseResponseModel.getLeaseId(), customerId, apiNotification);
				}
			}

			return vendorLeaseDetails;
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorLeaseResponseModel) getRentTrackRefreshedToken(customerId,  reqType);
			}
			getLeaseResponse = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMModuleException while getting RentTrack getLeaseList, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);


		} catch (Exception ex) {
			getLeaseResponse = ex.getMessage();
			log.error("Exception while invoking RentTrack.GetLeaseList, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_LEASE_BCM_EXCEPTION);
		}
		finally {
			notify(apiNotification, apis.getCreateLease(),"", getLeaseResponse,  AppConstants.HTTP_METHOD_GET);
		}

	}
	
	
	private VendorLeaseResponseModel getLeaseDetails(Long leaseId, Long customerId, VendorAPINotification apiNotification) {
		idValue = leaseId;
		String getLeaseDetails = null;
		VendorLeaseResponseModel vendorLeaseResponseModel = null;
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_LEASE_DETAILS;
		try {

			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));

			getLeaseDetails = rentTrackWebClient.method(HttpMethod.GET).uri(apis.getCreateLease()+"/" + leaseId).exchange()
					.block().bodyToMono(String.class).block();

			if (StringUtils.isNotBlank(getLeaseDetails)) {
				vendorLeaseResponseModel = objectMapper.readValue(getLeaseDetails, VendorLeaseResponseModel.class);
			}
			return vendorLeaseResponseModel;
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorLeaseResponseModel) getRentTrackRefreshedToken(customerId,  reqType);
			}
			getLeaseDetails = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();

			log.error("BCMModuleException while getting RentTrack getLeaseDetails, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
			
		} catch (Exception ex) {
			getLeaseDetails = ex.getMessage();
			log.error("Exception while invoking RentTrack.GetLeaseList, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_LEASE_BCM_EXCEPTION);
		}
		finally {
			notify(apiNotification, apis.getCreateLease()+"/" + leaseId,
					leaseId, getLeaseDetails,  AppConstants.HTTP_METHOD_GET);
		}

	}

	private VendorLeaseResponseModel getActiveLease(VendorLeaseResponseModel[] existLeaseArray) {
		for (VendorLeaseResponseModel vendorActivetLeaseResponse : existLeaseArray) {
			if (vendorActivetLeaseResponse.getStatus().equalsIgnoreCase(AppConstants.LEASE_STATUS_ACTIVE)) {
				return vendorActivetLeaseResponse;			
			}
		}
		return null;
	}

	public VendorPaymentAccountResponseModel[] createPlaidPaymentAccount(PaymentAccountApiModel request, Long customerId, VendorAPINotification apiNotification) {
		String response = null;
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.PAYMENT_ACCOUNT;
		try {
			log.info("Invoking Create Plaid Payment Account API call to RentTrack for the customerId [{}]",customerId);
			createPaymentAccountReqObj = request;
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.POST)
					.uri(apis.getCreatePlaidPaymentAccount()).syncBody(request).exchange().block().bodyToMono(String.class).block();	
			log.info("Response Received for Plaid Payment Account API call to RentTrack");
			return objectMapper.readValue(response, VendorPaymentAccountResponseModel[].class);

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorPaymentAccountResponseModel[]) getRentTrackRefreshedToken(customerId,  reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack createPlaidPaymentAccount, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.PAYMENT_ACCOUNT_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, apis.getCreatePlaidPaymentAccount(),
					request, response,  AppConstants.HTTP_METHOD_POST);
		}
	}
	
	
	private void notify(VendorAPINotification apiNotification, String apiURI, Object request, String response,String httpMethod) {
		
		if(startupProperty.isLogOn())
		{					
			try {
			List<String> methodTypes = startupProperty.getMethodTypeList();
			response = methodTypes.contains(httpMethod)? AppConstants.DISABLED : response;
			apiNotification.notify(buildUrl(apiURI), objectMapper.writeValueAsString(request), response, httpMethod);
			}
			catch (Exception e) {
			log.error("Unable to notify client. Error in while inserting request", e.getMessage(), e);
			}
		}
	}

	private String buildUrl(String uri) {
		String baseUrl = apis.getBaseUrl();
		return baseUrl + uri;
	}

		public VendorPaymentAccountResponseModel getPlaidPaymentAccountDetails(Long customerId, VendorAPINotification apiNotification)
		{
		String response = null;
		VendorPaymentAccountResponseModel[] vendorPaymentAccountResponseModel = null;
		VendorPaymentAccountResponseModel vpAccountResponseModel = null;
		apiNotificationClient = apiNotification;
		String api = apis.getPaymentAccount();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_PAYMENT_ACCOUNT;
		try {

			log.info("RentTrackClient getPlaidPaymentAccountDetails");
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));

			response = rentTrackWebClient.method(HttpMethod.GET).uri(api).exchange().block().bodyToMono(String.class)
					.block();
			if (StringUtils.isNotBlank(response)) {
				vendorPaymentAccountResponseModel = objectMapper.readValue(response,
						VendorPaymentAccountResponseModel[].class);
				vpAccountResponseModel = vendorPaymentAccountResponseModel[0];
			}

			return vpAccountResponseModel;
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorPaymentAccountResponseModel) getRentTrackRefreshedToken(customerId,
						reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMWebClientResponseException while getting RentTrack getPlaidPaymentAccountDetails, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);

			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.getPlaidPaymentAccountDetails, ErrorMessage: {}",
					ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_PAYMENT_ACCOUNT_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, api, "NA", response,  AppConstants.HTTP_METHOD_GET);
		}

	}

	public VendorTransactionResponseModel createTransactionFinder(TransactionApiModel request, Long customerId, VendorAPINotification apiNotification)
		{
		String response = null;
		transactionFinderApiModelObj = request;
		apiNotificationClient=apiNotification;
		String api = apis.getCreateTrxFinderByLeaseId();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.CREATE_TRX_FINDER;
		try {

			log.info("RentTrackClient createTransactionFinder");

			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace(AppConstants.LEASE_ID, String.valueOf(request.getLeaseId()));
			response = rentTrackWebClient.method(HttpMethod.POST)
					.uri(api).syncBody(request)
					.exchange().block().bodyToMono(String.class).block();
			
			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorTransactionResponseModel.class);
			else {
				return new VendorTransactionResponseModel();
			}

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorTransactionResponseModel) getRentTrackRefreshedToken(customerId,reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack createTransactionFinder, ErrorMessage: {}", ex.getMessage(),
					ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.CREATE_TRX_FINDER_BCM_EXCEPTION);
		}finally {
			if (response !=null && response.length() > 900) {
				response = response.substring(0, 900);
			}
			notify(apiNotification, api, request, response,  AppConstants.HTTP_METHOD_POST);
		}

	}

	public VendorSearchTransactionResponseModel[] searchTransactionInfoByFinder(TransactionApiModel request,
			Long customerId, VendorAPINotification apiNotification)
			{
		String response = null;
		transactionFinderApiModelObj = request;
		apiNotificationClient = apiNotification;
		String api = apis.getSearchTrxFinderByLeaseId();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.SEARCH_TRX_FINDER;
		try {

			log.info("RentTrackClient searchTransactionInfoByFinder");

			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			
			api = api.replace(AppConstants.LEASE_ID, String.valueOf(request.getLeaseId()));
			api = api.replace(AppConstants.TRX_FINDER_ID, String.valueOf(request.getTransactionFinderId()));

			response = rentTrackWebClient.method(HttpMethod.GET).uri(api).syncBody(request).exchange().block()
					.bodyToMono(String.class).block();

			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorSearchTransactionResponseModel[].class);
			else {
				return new VendorSearchTransactionResponseModel[0];
			}

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorSearchTransactionResponseModel[]) getRentTrackRefreshedToken(customerId,
						reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMModuleException while getting RentTrack searchTransactionInfoByFinder, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack searchTransactionInfoByFinder, ErrorMessage: {}",
					ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.SEARCH_TRX_FINDER_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, api, request, response,  AppConstants.HTTP_METHOD_GET);
		}

	}

	public VendorMatchTransactionResponseModel[] matchTransaction(MatchTransactionArrayApiModel request,
			Long customerId, Long transactionId, VendorAPINotification apiNotification) {
		String response = null;
		apiNotificationClient=apiNotification;
		idValue = transactionId;
		String api = apis.getMatchTrxFinderByLeaseId();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.MATCH_TRX_FINDER;
		try {
			log.info("Invoking Match Transaction API call to RentTrack for the customerId [{}]",customerId);
			matchTTrxReqObj = request;
			
			api = api.replace(AppConstants.TRX_FINDER_ID,String.valueOf(transactionId));
			
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.POST)
					.uri(api).syncBody(request).exchange().block().bodyToMono(String.class).block();
			
			log.info("Response Received for Match Transaction API call to RentTrack");
			return objectMapper.readValue(response, VendorMatchTransactionResponseModel[].class);

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorMatchTransactionResponseModel[]) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack Match Transaction, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.MATCH_TRX_FINDER_BCM_EXCEPTION);
		} finally {
			notify(apiNotification,api,request, response,  AppConstants.HTTP_METHOD_POST);
		}
	}
	
	public VendorSearchTransactionResponseModel[] searchTransactionByFinder(TransactionApiModel request,Long customerId, VendorAPINotification apiNotification)
		{
		String response = null;
		transactionFinderApiModelObj = request;
		apiNotificationClient = apiNotification;
		String api = apis.getSearchTrxById();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.SEARCH_TRX_ID;
		try {
			
			log.info("RentTrackClient searchTransactionByFinder");

			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			
			api = api.replace(AppConstants.TRX_FINDER_ID, String.valueOf(request.getTransactionFinderId()));
			
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(api)
					.syncBody(request).exchange().block().bodyToMono(String.class).block();

			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorSearchTransactionResponseModel[].class);
			else {
				return new VendorSearchTransactionResponseModel[0];
			}

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorSearchTransactionResponseModel[]) getRentTrackRefreshedToken(customerId,
						reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMModuleException while invoking RentTrack searchTransactionByFinder, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack searchTransactionByFinder, ErrorMessage: {}", ex.getMessage(),
					ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.SEARCH_TRX_FINDER_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, api, request, response,  AppConstants.HTTP_METHOD_GET);
		}

	}
	
	public VendorOrdersResponseModel[] getLeaseOrders(Long leaseId, Long customerId,  VendorAPINotification apiNotification)
		{
		String response = null;
		apiNotificationClient=apiNotification;
		String api = apis.getLeaseOrders();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_LEASE_ORDERS_LIST;
		idValue = leaseId;
		try {

			log.info("RentTrackClient getLeaseOrders customerId: "+customerId);
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace(AppConstants.LEASE_ID, String.valueOf(leaseId));
			response = rentTrackWebClient.method(HttpMethod.GET).uri(api).exchange().block()
					.bodyToMono(String.class).block();
			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorOrdersResponseModel[].class);
			else {
				return new VendorOrdersResponseModel[0];
			}

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorOrdersResponseModel[]) getRentTrackRefreshedToken(customerId,
						reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMModuleException while invoking RentTrack getLeaseOrders, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);

			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.getLeaseOrders, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_LEASE_ORDERS_LIST_BCM_EXCEPTION);
		}finally {
			notify(apiNotification,api, leaseId, response,  AppConstants.HTTP_METHOD_GET);
		}

	}	
	
	@SuppressWarnings("unchecked")
	public List<VendorSubscriptionResponseModelV2> getActiveSubscriptions(Long customerId, VendorAPINotification apiNotificationHandler) {
		log.info("RentTrackClient::getActiveSubscriptions::Input: [{}]", customerId);
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.SUBSCRIPTIONS_GET_ALL_ACTIVE;
		String response 		= null;
		try {
			rentTrackWebClient	= getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			ClientResponse wcResponse = rentTrackWebClient.method(HttpMethod.GET)
								.uri(apis.getGetActiveSubscriptions())
								.exchange().block();
			if(wcResponse.statusCode() == HttpStatus.NO_CONTENT) {
				return new ArrayList<>();
			} else {
				response = wcResponse.bodyToMono(String.class).block();
			}
			
			return objectMapper.readValue(response, new TypeReference<List<VendorSubscriptionResponseModelV2>> () {});

		} catch (BCMWebClientResponseException wcEx) {
			
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (List<VendorSubscriptionResponseModelV2>) refreshTokenAndRetry(reqType, Map.of(CUSTOMER_ID_STRING, customerId, 
									API_NODIFACTION_HANDLER_STRING, apiNotificationHandler));
			}
			
			log.error("BCMModuleException while calling RentTrack GetActiveSubscriptions, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();

			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		
		} catch (Exception ex) {			
			response = ex.getMessage();
			log.error("BCMModuleException while calling RentTrack GetActiveSubscriptions, ErrorMessage: {}, {}", ex.getMessage(), ex);
			
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), ex);
		
		} finally {
			notify(apiNotificationHandler, apis.getGetActiveSubscriptions(), null, response, AppConstants.HTTP_METHOD_GET);
		}
		
	}
	
	public Boolean deleteSubscription(Long customerId, Long id, VendorAPINotification apiNotificationHandler) 
		{
		log.info("RentTrackClient::deleteSubscription::Input: [{}, {}, {}]", customerId, id);
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.SUBSCRIPTIONS_DELETE;
		String response 		= null;
		try {
			rentTrackWebClient 	= getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			HttpStatus status = rentTrackWebClient.method(HttpMethod.DELETE)
					.uri(uriBuilder -> {
						URI uri = uriBuilder.path(apis.getDeleteSubscription()).build(id); 
						log.info(reqType + "::URI: [{}, {}, {}]", uri, uri.getHost(), uri.getPath());						
						return uri;
					})
					.exchange()
					.retry(property.getApiRetryCount(), e -> 
						WebClientUtils.isRetryRequired(
									e, 
									RentTrackClient.class.getName(), 
									"deleteSubscription(Long customerId, Long id, VendorAPINotification apiNotificationHandler)", 
									customerId, id,  apiNotificationHandler)
						)
					.block().statusCode();
			
			log.info("DeleteSubcription Status: {}", status);
			return (status == HttpStatus.NO_CONTENT);			

		} catch (BCMWebClientResponseException wcEx) {
			
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (Boolean) refreshTokenAndRetry(reqType, Map.of(CUSTOMER_ID_STRING, customerId, 
									"id", id,
									API_NODIFACTION_HANDLER_STRING, apiNotificationHandler));
			}
			
			log.error("BCMModuleException while calling RentTrack DeleteSubscription, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		
		} catch (Exception ex) {			
			response = ex.getMessage();
			log.error("BCMModuleException while calling RentTrack DeleteSubscription, ErrorMessage: {}, {}", ex.getMessage(), ex);
			
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), ex);
		
		} finally {
			notify(apiNotificationHandler, apis.getDeleteSubscription() + "/" + id, null, response, AppConstants.HTTP_METHOD_DELETE);
		}
		
	}
	
	public VendorUtilityResponseModel[] getAllUtilities(Long customerId,
			VendorAPINotification apiNotification) {
		String response = null;
		apiNotificationClient=apiNotification;
		String api = apis.getGetAllUtilities();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_ALL_UTILITIES;
		try {
			log.info("Invoking Get All utilties API call to RentTrack for the customerId [{}]",customerId);
			
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(api).exchange().block().bodyToMono(String.class).block();
			log.info("Response Received for Get All utilties API call from RentTrack");
			if(StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorUtilityResponseModel[].class);
			return new VendorUtilityResponseModel[0];

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorUtilityResponseModel[]) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack getAllUtilities, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_ALL_UTILITIES_BCM_EXCEPTION);
		} finally {
			if(null !=response && response.length() > 900)
			{
				response = response.substring(0, 900);
			}
			notify(apiNotification,api,"NA", response,  AppConstants.HTTP_METHOD_GET);
		}
	}

	public VendorUtilityDetailsResponseModel[] getUtilityDetails(Long customerId, Long utilityTradeLineId,
			VendorAPINotification apiNotification) {
		String response = null;
		String api = apis.getGetUtilityByTradeLineId();
		apiNotificationClient=apiNotification;
		idValue = utilityTradeLineId;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_UTILBY_TRDID;
		try {
			log.info("Invoking Get Utility Details by TradeLineId API call to RentTrack for the customerId [{}]",customerId);
			
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace("UTIL_TRADE_LINE_ID", String.valueOf(utilityTradeLineId));
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(api).exchange().block().bodyToMono(String.class).block();
		
			log.info("Response Received for Get Utility Details by TradeLineId from RentTrack");
			if(StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorUtilityDetailsResponseModel[].class);
			return new VendorUtilityDetailsResponseModel[0];

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorUtilityDetailsResponseModel[]) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack Get Utility Details by TradeLineId, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_UTILBY_TRDID_BCM_EXCEPTION);
		} finally {
			if(null !=response && response.length() > 900)
			{
				response = response.substring(0, 900);
			}
			notify(apiNotification,api,"NA", response,  AppConstants.HTTP_METHOD_GET);
		}
	}

	public boolean updateUtiltyStatus(Long customerId, String utilId, UtilityStatusApiModel request,
			VendorAPINotification apiNotification) {
		String response = null;
		String api = apis.getPatchUtility();
		apiNotificationClient=apiNotification;
		idValueString = utilId;
		utilityStatusObj = request;
		int statusCode;
		boolean isUpdated = false;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.PATCH_UTIL;
		try {
			log.info("Invoking updateUtiltyStatus API call to RentTrack for the customerId [{}]",customerId);
			
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace("UTIL_TRADE_LINE_ID", utilId);
			statusCode = rentTrackWebClient.method(HttpMethod.PATCH)
					.uri(api).syncBody(request).exchange().block().statusCode().value();
			log.info("Response Received for updateUtiltyStatus by TradeLineId from RentTrack");
			if(statusCode == 200)
				isUpdated = true;
			return isUpdated;

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				getRentTrackRefreshedToken(customerId,  reqType);
				return true;
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack Get Utility Details by TradeLineId, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.PATCH_UTIL_BCM_EXCEPTION);
		} finally {
			notify(apiNotification,api,"NA", response,  AppConstants.HTTP_METHOD_PATCH);
		}
	}
	
	public VendorSubscriptionResponseModel[] getSubscriptionsInfo(Long customerId,
			VendorAPINotification apiNotification) {
		String response = null;
		String uri = apis.getAllSubscriptions();
		apiNotificationClient = apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_SUBSCRIPTIONS;
		try {

			log.info("RentTrackClient getSubscriptionsInfo UCID: " + customerId);
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(HttpMethod.GET).uri(uri).exchange().block().bodyToMono(String.class)
					.block();
			
			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorSubscriptionResponseModel[].class);
			return new VendorSubscriptionResponseModel[0];

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorSubscriptionResponseModel[]) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.getSubscriptionsInfo, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_SUBSCRIPTIONS_BCM_EXCEPTION);
		} finally {
			notify(apiNotification, apis.getAllSubscriptions(), "NA", response, AppConstants.HTTP_METHOD_GET);
		}

	}
	
	public Boolean updateTransactionFinder(TransactionApiModel request, Long customerId, Long transactionFinderId,
			VendorAPINotification apiNotification) {
		String response = null;
		transactionFinderApiModelObj = request;
		apiNotificationClient = apiNotification;
		idValue = transactionFinderId;
		String api = apis.getUpdateTrxById();
		int statusCode;
		boolean isUpdated = false;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.UPDATE_TRX_FINDER;
		try {

			log.info("RentTrackClient updateTransactionFinder");

			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));

			statusCode = rentTrackWebClient.method(HttpMethod.PATCH).uri(api + idValue).syncBody(request)
					.exchange().block().statusCode().value();

			if (statusCode == 204)
				isUpdated = true;

			return isUpdated;

		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				getRentTrackRefreshedToken(customerId, reqType);
				return true;
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);

		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack updateTransactionFinder, ErrorMessage: {}", ex.getMessage(),
					ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.UPDATE_TRX_FINDER_BCM_EXCEPTION);
		} finally {
			if (null !=response && response.length() > 900) {
				response = response.substring(0, 900);
			}
			notify(apiNotification, api, request, response, AppConstants.HTTP_METHOD_POST);
		}

	}
	public VendorPlaidReconnectResponseModel reconnectPlaid(Long customerId, Long paymentAccId, VendorAPINotification apiNotification, String requestType) {
		String response = null;
		String uri = apis.getPlaidReconnectByPayAccId();
		apiNotificationClient=apiNotification;
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.valueOf(requestType);
		HttpMethod httpMethod = HttpMethod.POST;
		idValue = paymentAccId;
		try {
			uri = uri.replace(AppConstants.PAYMENT_ACCOUNT_ID, String.valueOf(paymentAccId));
			if(requestType.equals(RentTrackClientRequestTypesEnum.PLAID_RECONNECTION_PATCH.getName())){
				httpMethod = HttpMethod.PATCH;
			}
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			response = rentTrackWebClient.method(httpMethod)
					.uri(uri).exchange().block().bodyToMono(String.class).block();		
			if(StringUtils.isNotEmpty(response)){
				return objectMapper.readValue(response, VendorPlaidReconnectResponseModel.class);
			}		
			return null;
	
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorPlaidReconnectResponseModel) getRentTrackRefreshedToken(customerId, reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
	
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.getPublicTokenForReconnect, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.valueOf(requestType+"_BCM_EXCEPTION"));
		} finally {
			notify(apiNotification, uri,"NA", response,httpMethod.name());
		}
	}
	
	public VendorOrdersResponseModel[] getUtilityOrders(Long customerId, Long utilityId,
			VendorAPINotification apiNotification) {
	
		String response = null;
		apiNotificationClient=apiNotification;
		String api = apis.getUtilityOrders();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_UTILITY_ORDERS_LIST;
		idValue = utilityId;
		try {
	
			log.info("RentTrackClient getUtilityOrders customerId: "+customerId);
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace(AppConstants.UTILITY_ID, String.valueOf(utilityId));
			response = rentTrackWebClient.method(HttpMethod.GET).uri(api).exchange().block()
					.bodyToMono(String.class).block();
			if (StringUtils.isNotEmpty(response))
				return objectMapper.readValue(response, VendorOrdersResponseModel[].class);
			else {
				return new VendorOrdersResponseModel[0];
			}
	
		 } catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorOrdersResponseModel[]) getRentTrackRefreshedToken(customerId,
						reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			log.error("BCMModuleException while invoking RentTrack getUtilityOrders, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
	
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack.getUtilityOrders, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_UTILITY_ORDERS_LIST_BCM_EXCEPTION);
		}finally {
			notify(apiNotification,api, utilityId, response,  AppConstants.HTTP_METHOD_GET);
		}
	}
	public VendorTransactionResponseModel getTransactionFinder(Long customerId, Long transactionFinderId, VendorAPINotification apiNotification)
	{
		String response = null;
		apiNotificationClient=apiNotification;
		String api = apis.getTransactionFinder();
		RentTrackClientRequestTypesEnum reqType = RentTrackClientRequestTypesEnum.GET_TRX_FINDER;
		idValue = transactionFinderId;
		VendorTransactionResponseModel vendorTransactionResponseModel = null;
		try {
	
			log.info("RentTrackClient get TransactionFinder");
	
			rentTrackWebClient = getRentTrackWebClient(rtTokenProvider.getCurrentAccessTokenOfUser(customerId));
			api = api.replace(AppConstants.TRX_FINDER_ID, String.valueOf(transactionFinderId));
			response = rentTrackWebClient.method(HttpMethod.GET).uri(api).exchange().block()
					.bodyToMono(String.class).block();
			
			if (StringUtils.isNotEmpty(response)) {
				vendorTransactionResponseModel = objectMapper.readValue(response, VendorTransactionResponseModel.class);
			}
			return vendorTransactionResponseModel;
	
		} catch (BCMWebClientResponseException wcEx) {
			if (BCMExceptionUtils.isTokenExpired(wcEx)) {
				return (VendorTransactionResponseModel) getRentTrackRefreshedToken(customerId,reqType);
			}
			response = wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage();
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(reqType.getName(), wcEx);
	
		} catch (Exception ex) {
			response = ex.getMessage();
			log.error("Exception while invoking RentTrack get TransactionFinder, ErrorMessage: {}", ex.getMessage(),
					ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.GET_TRX_FINDER_BCM_EXCEPTION);
		} finally {
			if (response !=null && response.length() > 900) {
				response = response.substring(0, 900);
			}
			notify(apiNotification, api, transactionFinderId, response,  AppConstants.HTTP_METHOD_GET);
		}

	}
}
