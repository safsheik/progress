package com.progrexion.bcm.renttrack.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.config.BCMLoadPropOnStartUp;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUpdateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.properties.RentTrackAPIConfigProperties;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.common.webclients.utils.WebClientHttpRequestHeader;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserRequestModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserResponseModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;
import com.progrexion.bcm.renttrack.client.services.AccessTokenProviderService;
import com.progrexion.bcm.renttrack.client.services.TestUtils;

import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class RentTrackClientTest {

	@Mock
	private WebTestClient rentTrackWebClient;

	@Mock
	private WebClientHttpRequestHeader webClientHttpRequestHeader;

	@Mock
	private WebClientErrorHandler webClientErrorHandler;

	@Mock
	private AccessTokenProviderService rtTokenProvider;

	@Mock
	private RentTrackAPIConfigProperties apis;
	
	@InjectMocks
	private  RentTrackClient rentTrackClient;
	private VendorAPINotification apiNotification;
	@Mock
	private ObjectMapper mockObjectMapper;
	private Long customerId =101l;
	private Long idValue =102l;
	private String utilId="123456";
	@Mock
	private BCMLoadPropOnStartUp startupProperty;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		apiNotification = mock(VendorAPINotification.class);
		setProperties();
	}
	@Test
	public void test_createSubscription()	{
		getWebClientMock();
		SubscriptionApiModel request = new SubscriptionApiModel();
		VendorSubscriptionResponseModel response = rentTrackClient.createSubscription(request,customerId,apiNotification);
		assertNull(response);

	}
	
	@Test
public void test_createSubscriptionException() {
		getWebClientMockForException();
		SubscriptionApiModel request = new SubscriptionApiModel();
		try
		{
			rentTrackClient.createSubscription(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTION_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_createSubscriptionClientException() {			
		getWebClientMockForClientException();
		SubscriptionApiModel request = new SubscriptionApiModel();
			try
			{
				rentTrackClient.createSubscription(request,customerId,apiNotification);
			}
			catch(BCMModuleException e)
			{
				assertEquals(BCMModuleExceptionEnum.SUBSCRIPTION_500.getCode(), e.getErrorCode());
			}
		}
	@Test
	public void test_createUserAccount() {
		getWebClientMock();
		RTCreateUserRequestModel request = new RTCreateUserRequestModel();
		RTCreateUserResponseModel response = rentTrackClient.createUserAccount(request,apiNotification);
		assertNull(response);

	}
	
	@Test
	public void test_createUserAccountExceptionClient() {
		getWebClientMockForClientException();
		RTCreateUserRequestModel request = new RTCreateUserRequestModel();
		try
		{
			 rentTrackClient.createUserAccount(request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_TENANT_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_createUserAccountException() {
		          getWebClientMockForException();
		RTCreateUserRequestModel request = new RTCreateUserRequestModel();
		try
		{
			 rentTrackClient.createUserAccount(request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_TENANT_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_updateTenantUserDetails() {
		getWebClientMock();		
		VendorUpdateUserRequestModel request = new VendorUpdateUserRequestModel();
		Boolean response = rentTrackClient.updateTenantUserDetails(customerId, request,apiNotification);
		assertEquals(true,response);

	}
	
	@Test
	public void test_updateTenantUserDetailsException() {
          getWebClientMockForException();
		VendorUpdateUserRequestModel request = new VendorUpdateUserRequestModel();
		try
		{
			rentTrackClient.updateTenantUserDetails(customerId, request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PATCH_TENANT_USER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}


	}
	@Test
	public void test_updateTenantUserDetailsExceptionClient() {
          getWebClientMockForClientException();
		VendorUpdateUserRequestModel request = new VendorUpdateUserRequestModel();
		try
		{
			rentTrackClient.updateTenantUserDetails(customerId, request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PATCH_TENANT_USER_500.getCode(), e.getErrorCode());
		}


	}
	@Test
	public void test_createLease() {
		getWebClientMock();	
		LeaseApiModel request = new LeaseApiModel();
		VendorLeaseResponseModel response = rentTrackClient.createLease(request, customerId, apiNotification);
		assertNull(response);

	}
	
	@Test
	public void test_createLeaseException() {
          getWebClientMockForException();
		LeaseApiModel request = new LeaseApiModel();
		try
		{
			 rentTrackClient.createLease(request, customerId, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_LEASE_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_createLeaseExceptionClient() {
          getWebClientMockForClientException();
		LeaseApiModel request = new LeaseApiModel();
		try
		{
			 rentTrackClient.createLease(request, customerId, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_LEASE_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getLeaseList() {
		getWebClientMock();		
		VendorLeaseResponseModel response = rentTrackClient.getLeaseList(customerId, apiNotification);
		assertNull(response);

	}

	@Test
	public void test_getLeaseListException() {
          getWebClientMockForException();
		try
		{
			 rentTrackClient.getLeaseList(customerId, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_LEASE_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_getLeaseListClientException() {
          getWebClientMockForClientException();
		try
		{
			 rentTrackClient.getLeaseList(customerId, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_LEASE_500.getCode(), e.getErrorCode());
		}
	}
	public void test_createPlaidPaymentAccount() {
		getWebClientMock();		
		PaymentAccountApiModel request = new PaymentAccountApiModel();
		VendorPaymentAccountResponseModel[] response = rentTrackClient.createPlaidPaymentAccount(request,customerId,apiNotification);
		assertNull(response);

	}

	@Test
	public void test_createPlaidPaymentAccountException() {
          getWebClientMockForException();
		PaymentAccountApiModel request = new PaymentAccountApiModel();
		try
		{
			 rentTrackClient.createPlaidPaymentAccount(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PAYMENT_ACCOUNT_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_createPlaidPaymentAccountClientException() {
          getWebClientMockForClientException();
		PaymentAccountApiModel request = new PaymentAccountApiModel();
		try
		{
			 rentTrackClient.createPlaidPaymentAccount(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PAYMENT_ACCOUNT_500.getCode(), e.getErrorCode());
		}
	}
	
	public void test_getPlaidPaymentAccountDetails() {
		getWebClientMock();		
		VendorPaymentAccountResponseModel response = rentTrackClient.getPlaidPaymentAccountDetails(customerId,apiNotification);
		assertNull(response);

	}
	
	@Test
	public void test_getPlaidPaymentAccountDetailsException() {
          getWebClientMockForException();
		try
		{
			 rentTrackClient.getPlaidPaymentAccountDetails(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_PAYMENT_ACCOUNT_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_getPlaidPaymentAccountDetailsClientException() {
          getWebClientMockForClientException();
		try
		{
			 rentTrackClient.getPlaidPaymentAccountDetails(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_PAYMENT_ACCOUNT_500.getCode(), e.getErrorCode());
		}
	}
	public void test_reconnectPlaid() {
		getWebClientMock();		
		VendorPlaidReconnectResponseModel response = rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION);
		assertNull(response);
		response = rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION_PATCH);
		assertNull(response);
	}
	
	@Test
	public void test_reconnectPlaidException() {
          getWebClientMockForException();
		try
		{
			 rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PLAID_RECONNECTION_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
		try
		{
			 rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION_PATCH);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PLAID_RECONNECTION_PATCH_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_reconnectPlaidClientException() {
          getWebClientMockForClientException();
		try
		{
			 rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PLAID_RECONNECTION_500.getCode(), e.getErrorCode());
		}
		try
		{
			 rentTrackClient.reconnectPlaid(customerId,idValue,apiNotification,AppConstants.PLAID_RECONNECTION_PATCH);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PLAID_RECONNECTION_PATCH_500.getCode(), e.getErrorCode());
		}
	}
	private  void getWebClientMock() {
		ExchangeFilterFunction mockFunction = ExchangeFilterFunction.ofResponseProcessor(clientResponse  -> {
			return Mono.just(clientResponse);});
		when(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter()).thenReturn(mockFunction);
		when((apis.getBaseUrl())).thenReturn("https://my.sandbox.rt-stg.com");
		when(webClientHttpRequestHeader.configureRentTrackHeaders(Mockito.any(),Mockito.any())).thenReturn(new HttpHeaders());	
	}
	private  void getWebClientMockForException() {
		ExchangeFilterFunction mockFunction = ExchangeFilterFunction.ofResponseProcessor(clientResponse  -> {
			return Mono.just(clientResponse);});
		when(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter()).thenReturn(mockFunction);
		when((apis.getBaseUrl())).thenReturn("https://my.sandbox.rt-stg.com");
		when(webClientHttpRequestHeader.configureRentTrackHeaders(Mockito.any(),Mockito.any()))
		.thenThrow(new NullPointerException());
	}
	private  void getWebClientMockForClientException() {
		ExchangeFilterFunction mockFunction = ExchangeFilterFunction.ofResponseProcessor(clientResponse  -> {
			return Mono.just(clientResponse);});
		when(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter()).thenReturn(mockFunction);
		when((apis.getBaseUrl())).thenReturn("https://my.sandbox.rt-stg.com");
		when(webClientHttpRequestHeader.configureRentTrackHeaders(Mockito.any(),Mockito.any())).thenThrow(new BCMWebClientResponseException("Service not Available", HttpStatus.INTERNAL_SERVER_ERROR, BCMModuleExceptionEnum.RENTTRACK_SERVER_ERROR));
	}
	@Test
	public void test_createTransactionFinder() {
		getWebClientMock();		
		TransactionApiModel request = new TransactionApiModel();
		VendorTransactionResponseModel response = rentTrackClient.createTransactionFinder(request,customerId,apiNotification);
		assertNotNull(response);

	}

	@Test
	public void test_createTransactionFinderException() {
          getWebClientMockForException();
		TransactionApiModel request = new TransactionApiModel();
		try
		{
			 rentTrackClient.createTransactionFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_TRX_FINDER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_createTransactionFinderClientException() {
          getWebClientMockForClientException();
		TransactionApiModel request = new TransactionApiModel();
		try
		{
			 rentTrackClient.createTransactionFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.CREATE_TRX_FINDER_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_searchTransactionInfoByFinder() {
		getWebClientMock();		
		TransactionApiModel request = new TransactionApiModel();
		VendorSearchTransactionResponseModel[] response = rentTrackClient.searchTransactionInfoByFinder(request,customerId,apiNotification);
		assertNotNull(response);

	}
	
	@Test
	public void test_searchTransactionInfoByFinderException() {
          getWebClientMockForException();
		TransactionApiModel request = new TransactionApiModel();
		try
		{
			 rentTrackClient.searchTransactionInfoByFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SEARCH_TRX_FINDER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_searchTransactionInfoByFinderClientException() {
          getWebClientMockForClientException();
		TransactionApiModel request = new TransactionApiModel();
		request.setLeaseId(101l);
		try
		{
			 rentTrackClient.searchTransactionInfoByFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SEARCH_TRX_FINDER_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_matchTransaction() {
		getWebClientMock();		
		MatchTransactionArrayApiModel request = new MatchTransactionArrayApiModel();
		VendorMatchTransactionResponseModel[] response = rentTrackClient.matchTransaction(request,customerId,101l,apiNotification);
		assertNull(response);

	}
	
	@Test
	public void test_matchTransactionException() {
          getWebClientMockForException();
		MatchTransactionArrayApiModel request = new MatchTransactionArrayApiModel();
		try
		{
			rentTrackClient.matchTransaction(request,customerId,101l,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.MATCH_TRX_FINDER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_matchTransactionClientException() {
          getWebClientMockForClientException();
		MatchTransactionArrayApiModel request = new MatchTransactionArrayApiModel();
		try
		{
			rentTrackClient.matchTransaction(request,customerId,101l,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.MATCH_TRX_FINDER_500.getCode(), e.getErrorCode());
		}
	}
	public void test_searchTransactionByFinder() {
		getWebClientMock();		
		TransactionApiModel request = new TransactionApiModel();
		VendorSearchTransactionResponseModel[] response = rentTrackClient.searchTransactionByFinder(request,customerId,apiNotification);
		assertNotNull(response);

	}
	
	@Test
	public void test_searchTransactionByFinderException() {
          getWebClientMockForException();
		TransactionApiModel request = new TransactionApiModel();
		try
		{
			rentTrackClient.searchTransactionByFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SEARCH_TRX_FINDER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_searchTransactionByFinderClientException() {
         getWebClientMockForClientException();
		TransactionApiModel request = new TransactionApiModel();
		request.setTransactionFinderId(101l);
		try
		{
			rentTrackClient.searchTransactionByFinder(request,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SEARCH_TRX_ID_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getLeaseOrders() {
		getWebClientMock();		
		VendorOrdersResponseModel[] response = rentTrackClient.getLeaseOrders(101l,customerId,apiNotification);
		assertNotNull(response);

	}

	@Test
	public void test_getLeaseOrdersException() {
          getWebClientMockForException();
		try
		{
			rentTrackClient.getLeaseOrders(101l,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_LEASE_ORDERS_LIST_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getLeaseOrdersClientException() {
          getWebClientMockForClientException();
		try
		{
			rentTrackClient.getLeaseOrders(101l,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_LEASE_ORDERS_LIST_500.getCode(), e.getErrorCode());
		}
	}

	@Test
	public void test_getAllUtilities() {
		getWebClientMock();		
		VendorUtilityResponseModel[] response = rentTrackClient.getAllUtilities(customerId,apiNotification);
		assertNotNull(response);

	}
	
	@Test
	public void test_getAllUtilitiesException() {
          getWebClientMockForException();

		try
		{
			rentTrackClient.getAllUtilities(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_ALL_UTILITIES_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getAllUtilitiesClientException() {
          getWebClientMockForClientException();

		try
		{
			rentTrackClient.getAllUtilities(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_ALL_UTILITIES_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getUtilityDetails() {
		getWebClientMock();		
		VendorUtilityDetailsResponseModel[] response = rentTrackClient.getUtilityDetails(customerId,101l,apiNotification);
		assertNotNull(response);

	}
	
	@Test
	public void test_getUtilityDetailsException() {
          getWebClientMockForException();

		try
		{
			rentTrackClient.getUtilityDetails(customerId,101l,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_UTILBY_TRDID_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getUtilityDetailsClientException() {
          getWebClientMockForClientException();

		try
		{
			rentTrackClient.getUtilityDetails(customerId,101l,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_UTILBY_TRDID_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_updateUtiltyStatus() {
		getWebClientMock();		
		UtilityStatusApiModel request = new UtilityStatusApiModel();
		boolean response = rentTrackClient.updateUtiltyStatus(customerId,utilId,request,apiNotification);
		assertEquals(false,response);

	}
	
	@Test
	public void test_updateUtiltyStatusException() {
        getWebClientMockForException();
		UtilityStatusApiModel request = new UtilityStatusApiModel();
		try
		{
			rentTrackClient.updateUtiltyStatus(customerId,utilId,request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PATCH_UTIL_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_updateUtiltyStatusClientException() {
        getWebClientMockForClientException();
		UtilityStatusApiModel request = new UtilityStatusApiModel();
		try
		{
			rentTrackClient.updateUtiltyStatus(customerId,utilId,request,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.PATCH_UTIL_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getSubscriptionsInfo() {
		getWebClientMock();		
		VendorSubscriptionResponseModel[] response = rentTrackClient.getSubscriptionsInfo(customerId,apiNotification);
		assertNull(response);

	}

	@Test
	public void test_getSubscriptionsInfoException() {
         getWebClientMockForException();
		try
		{
			rentTrackClient.getSubscriptionsInfo(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_SUBSCRIPTIONS_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_getSubscriptionsInfoClientException() {
         getWebClientMockForClientException();
		try
		{
			rentTrackClient.getSubscriptionsInfo(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_SUBSCRIPTIONS_500.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getActiveSubscriptions() {
		getWebClientMock();		
		List<VendorSubscriptionResponseModelV2> response = rentTrackClient.getActiveSubscriptions(customerId,apiNotification);
		assertNull(response);

	}

	@Test
	public void test_getActiveSubscriptionsException() {
         getWebClientMockForException();
		try
		{
			rentTrackClient.getActiveSubscriptions(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTIONS_GET_ALL_ACTIVE_500_BCM.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_getActiveSubscriptionsClientException() {
         getWebClientMockForClientException();
		try
		{
			rentTrackClient.getActiveSubscriptions(customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTIONS_GET_ALL_ACTIVE_500.getCode(), e.getErrorCode());
		}
	}

	@Test
	public void test_deleteSubscriptionException() {
         getWebClientMockForException();
		try
		{
			rentTrackClient.deleteSubscription(customerId, 101l, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTIONS_DELETE_500_BCM.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_deleteSubscriptionClientException() {
         getWebClientMockForClientException();
		try
		{
			rentTrackClient.deleteSubscription(customerId, 101l, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTIONS_DELETE_500.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_updateTransactionFinder() {
		getWebClientMock();		
		boolean response = rentTrackClient.updateTransactionFinder(new TransactionApiModel(), customerId, 101l, apiNotification);
		assertEquals(false,response);

	}

	@Test
	public void test_updateTransactionFinderException() {
         getWebClientMockForException();
		try
		{
			rentTrackClient.updateTransactionFinder(new TransactionApiModel(), customerId, 101l, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.UPDATE_TRX_FINDER_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_updateTransactionFinderClientException() {
         getWebClientMockForClientException();
		try
		{
			rentTrackClient.updateTransactionFinder(new TransactionApiModel(), customerId, 101l, apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.UPDATE_TRX_FINDER_500.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_getUtilityOrders() {
		getWebClientMock();		
		VendorOrdersResponseModel[] response = rentTrackClient.getUtilityOrders(101l,customerId,apiNotification);
		assertNotNull(response);

	}

	@Test
	public void test_getUtilityOrdersException() {
          getWebClientMockForException();
		try
		{
			rentTrackClient.getUtilityOrders(101l,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_UTILITY_ORDERS_LIST_BCM_EXCEPTION.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_getUtilityOrdersClientException() {
          getWebClientMockForClientException();
		try
		{
			rentTrackClient.getUtilityOrders(101l,customerId,apiNotification);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.GET_UTILITY_ORDERS_LIST_500.getCode(), e.getErrorCode());
		}
	}
	private void setProperties()
	{
		when(rtTokenProvider.getCurrentAccessTokenOfUser(Mockito.anyLong())).thenReturn(TestUtils.ACCESS_TOKEN);
		when((apis.getCreateSubscription())).thenReturn("/api/tenant/subscriptions");
		when((apis.getCreateUser())).thenReturn("/api/partner/users");
		when((apis.getUpdateUser())).thenReturn("/api/tenant/details");
		when((apis.getCreateLease())).thenReturn("/api/tenant/leases");
		when((apis.getCreatePlaidPaymentAccount())).thenReturn("/api/tenant/plaid/payment_accounts");
		when((apis.getPaymentAccount())).thenReturn("/api/tenant/payment_accounts");
		when((apis.getCreateTrxFinderByLeaseId())).thenReturn("/api/tenant/leases/LEASE_ID/transaction_finders");
		when((apis.getSearchTrxFinderByLeaseId())).thenReturn("/api/tenant/leases/LEASE_ID/transaction_finders/TRX_FINDER_ID/search");	
		when((apis.getMatchTrxFinderByLeaseId())).thenReturn("/api/tenant/transaction_finders/TRX_FINDER_ID/match");
		when((apis.getSearchTrxById())).thenReturn("/api/tenant/transaction_finders/TRX_FINDER_ID/search");
		when((apis.getLeaseOrders())).thenReturn("/api/tenant/leases/LEASE_ID/orders?representation=detailed");
		when((apis.getGetAllUtilities())).thenReturn("/api/tenant/utilities?representation=detailed");		
		when((apis.getGetUtilityByTradeLineId())).thenReturn("/api/tenant/utilities/UTIL_TRADE_LINE_ID/matches?representation=detailed");
		when((apis.getPatchUtility())).thenReturn("/api/tenant/utilities/UTIL_TRADE_LINE_ID/matches?representation=detailed");
		when(startupProperty.isLogOn()).thenReturn(true);
		when(startupProperty.getMethodTypeList()).thenReturn(new ArrayList<String>());
		when(apis.getPlaidReconnectByPayAccId()).thenReturn("/api/tenant/plaid/payment_accounts/PAYMENT_ACCOUNT_ID/reconnect");
		when(apis.getGetActiveSubscriptions()).thenReturn("/api/tenant/subscriptions?representation=only_active");
		when(apis.getDeleteSubscription()).thenReturn("/api/tenant/subscriptions}/{id}");
		when(apis.getUpdateTrxById()).thenReturn("/api/tenant/transaction_finders/");
		when(apis.getUtilityOrders()).thenReturn("/api/tenant/utilities/UTILITY_ID/orders?representation=detailed");
	}
}
