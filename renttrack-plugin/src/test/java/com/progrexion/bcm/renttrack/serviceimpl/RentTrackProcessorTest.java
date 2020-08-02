package com.progrexion.bcm.renttrack.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import org.modelmapper.ModelMapper;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.vendor.LeaseAddress;
import com.progrexion.bcm.common.model.vendor.LeaseLandlord;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUpdateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.renttrack.client.RentTrackClient;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserRequestModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserResponseModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;
import com.progrexion.bcm.renttrack.client.model.builder.RentTrackModelBuilder;
@RunWith(MockitoJUnitRunner.class)
public class RentTrackProcessorTest{

	@InjectMocks
	private RentTrackProcessor rentTrackProcessor;
	
	@Mock
	private RentTrackModelBuilder modelBuilder;

	@Mock
	private RentTrackClient serviceClient;
	
	private VendorAPINotification apiNotification;
	
	@Mock
	private ModelMapper modelMapper;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		apiNotification = mock(VendorAPINotification.class);
	}
	@Test
	public void test_createSubscription(){
		
		VendorSubscriptionRequestModel request = new VendorSubscriptionRequestModel();
		request.setPlan("consumer_lite");
		request.setPromotionCode("CREDITCOMFREE");
		
		SubscriptionApiModel subscriptionApiModel =new SubscriptionApiModel();
		when(modelBuilder.buildCreateSubscriptionApiModel(Mockito.any())).thenReturn(subscriptionApiModel);
		
		VendorSubscriptionResponseModel responseModel = new VendorSubscriptionResponseModel();
		responseModel.setPlanName(request.getPlan());
		when(serviceClient.createSubscription(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenReturn(responseModel);
		VendorSubscriptionResponseModel response = rentTrackProcessor.createSubscription(request, 101l, apiNotification);
		 assertNotNull(response);
		 assertEquals(response.getPlanName(), responseModel.getPlanName());
	}

	
	@Test
	public void test_createUserAccount(){
		
		VendorCreateUserRequestModel request = new VendorCreateUserRequestModel();
		request.setFirstName("testuser");
		request.setPassword("password@1");
		request.setEmail("test@gmail.com");
		request.setType("tenant");
		
		RTCreateUserRequestModel apiModel =new RTCreateUserRequestModel();
		when(modelBuilder.createUserAccountApiRequestModel(Mockito.any())).thenReturn(apiModel);
		
		RTCreateUserResponseModel responseModel = new RTCreateUserResponseModel();
		responseModel.setId(234324l);
		
		when(serviceClient.createUserAccount(Mockito.any(), Mockito.any())).thenReturn(responseModel);
		VendorCreateUserResponseModel response = new VendorCreateUserResponseModel();
		response.setId(responseModel.getId());
		when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(response);
		
		response = rentTrackProcessor.createUserAccount(request, apiNotification);
		 assertNotNull(response);
		 assertEquals(response.getId(), responseModel.getId());
	}
	
	
	@Test
	public void test_updateTenantUserDetails(){
		
		VendorUpdateUserRequestModel request = new VendorUpdateUserRequestModel();
		request.setDob(LocalDate.now());
		when(serviceClient.updateTenantUserDetails(Mockito.anyLong(),Mockito.any(), Mockito.any())).thenReturn(true);
		 assertEquals(rentTrackProcessor.updateTenantUserDetails(101l,request, apiNotification),true);
	}

	@Test
	public void test_createLease(){
		
		VendorLeaseRequestModel request = new VendorLeaseRequestModel();
		Address address=new Address("Plot A5","STREET","CITY","4900051","STATE","US");
		Landlord landlord=new Landlord("","John","7894561234","abc@test.com");
		request.setAddress(address);
		request.setLandlordModel(landlord);
		
		VendorLeaseResponseModel response = new VendorLeaseResponseModel();
		LeaseAddress leaseAddress=new LeaseAddress();
		leaseAddress.setAddress1("Plot A5");
		LeaseLandlord leaseLandlord = new LeaseLandlord();	
		response.setAddress(leaseAddress);
		response.setLandlord(leaseLandlord);
		
		LeaseApiModel apiModel =new LeaseApiModel();
		when(modelBuilder.buildLeaseApiModel(Mockito.any())).thenReturn(apiModel);
		
		when(serviceClient.createLease(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenReturn(response);
		VendorLeaseResponseModel responseModel = rentTrackProcessor.createLease(request, 101l,apiNotification);
		 assertNotNull(response);
		 assertEquals(response.getAddress().getAddress1(), responseModel.getAddress().getAddress1());
	}
	
	@Test
	public void test_createPlaidPaymentAccount(){
		VendorPaymentAccountRequestModel request = new VendorPaymentAccountRequestModel();
		request.setPayType("nopay");
		request.setPublicToken("public-sandbox");
		
		PaymentAccountApiModel apiModel = new PaymentAccountApiModel();
		apiModel.setPayType(request.getPayType());
		apiModel.setPublicToken(request.getPublicToken());

		when(modelBuilder.buildCreatePaymentAccountApiModel(Mockito.any())).thenReturn(apiModel);
		
		VendorPaymentAccountResponseModel[] responseArrayMock = new VendorPaymentAccountResponseModel[3];
		when(serviceClient.createPlaidPaymentAccount(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenReturn(responseArrayMock);
		VendorPaymentAccountResponseModel[] responseArray = rentTrackProcessor.createPlaidPaymentAccount(request, 101l,apiNotification);
		assertEquals(responseArray.length,responseArrayMock.length);
	}
	
	@Test
	public void test_getPlaidPaymentAccountDetails(){	
		VendorPaymentAccountResponseModel responseMock = new VendorPaymentAccountResponseModel();
		responseMock.setId(101l);
		when(serviceClient.getPlaidPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenReturn(responseMock);
		VendorPaymentAccountResponseModel response = rentTrackProcessor.getPlaidPaymentAccountDetails(101l,apiNotification);
		assertEquals(response.getId(),responseMock.getId());
	}
	
	@Test
	public void test_getLeaseInfo(){	
		VendorLeaseResponseModel responseMock = new VendorLeaseResponseModel();
		LeaseAddress leaseAddress=new LeaseAddress();
		leaseAddress.setAddress1("Plot A5");
		LeaseLandlord leaseLandlord = new LeaseLandlord();	
		responseMock.setAddress(leaseAddress);
		responseMock.setLandlord(leaseLandlord);
		
		when(serviceClient.getLeaseList(Mockito.anyLong(), Mockito.any())).thenReturn(responseMock);
		VendorLeaseResponseModel response = rentTrackProcessor.getLeaseInfo(101l,apiNotification);
		 assertNotNull(response);
		 assertEquals(response.getAddress().getAddress1(), responseMock.getAddress().getAddress1());
	}
	@Test
	public void test_createTransactionFinder(){	
		VendorTransactionRequestModel request = new VendorTransactionRequestModel();
		Long leaseId = 1000l;
		request.setLeaseUrl("http://leases/890");
		request.setPaymentAccountUrl("http://payemnt_accountd/8902313");
		request.setRentAmount(10.0f);
		request.setDueDay(10);
		TransactionApiModel apiModel = new TransactionApiModel();
		VendorTransactionResponseModel responseMock = new VendorTransactionResponseModel();
		responseMock.setId(1099l);
		when(modelBuilder.buildTransactionApiModel(Mockito.any())).thenReturn(apiModel);
		when(serviceClient.createTransactionFinder( Mockito.any(),Mockito.anyLong(), Mockito.any())).thenReturn(responseMock);
		VendorTransactionResponseModel response = rentTrackProcessor.createTransactionFinder(request, 101l,leaseId,apiNotification);
		 assertNotNull(response);

	}
	
	@Test
	public void test_searchByTransactionFinder(){	
		VendorTransactionRequestModel request = new VendorTransactionRequestModel();
		request.setLeaseUrl("http://leases/890");
		request.setPaymentAccountUrl("http://payemnt_accountd/8902313");
		request.setRentAmount(10.0f);
		request.setDueDay(10);
		TransactionApiModel apiModel = new TransactionApiModel();
		VendorSearchTransactionResponseModel[] mockResponseArray = new VendorSearchTransactionResponseModel[3];
		when(modelBuilder.buildTransactionApiModel(Mockito.any())).thenReturn(apiModel);
		when(serviceClient.searchTransactionInfoByFinder( Mockito.any(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorSearchTransactionResponseModel[] resposneArray = rentTrackProcessor.searchByTransactionFinder(request, 101l,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_matchTransaction(){	
		VendorMatchTransactionRequestModel request = new VendorMatchTransactionRequestModel();
		Long id = 1000l;
		MatchTransactionArrayApiModel matchTransactionArrayApiModel = new MatchTransactionArrayApiModel();
		VendorMatchTransactionResponseModel[] mockResponseArray = new VendorMatchTransactionResponseModel[3];
		when(modelBuilder.buildMatchTrxApiModel(Mockito.any())).thenReturn(matchTransactionArrayApiModel);
		when(serviceClient.matchTransaction( Mockito.any(),Mockito.anyLong(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorMatchTransactionResponseModel[] resposneArray = rentTrackProcessor.matchTransaction(request, 101l,id,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	@Test
	public void test_searchTransactionByFinder(){	
		VendorTransactionRequestModel request = new VendorTransactionRequestModel();
		Long leaseId = 1000l;
		request.setLeaseUrl("http://leases/890");
		request.setPaymentAccountUrl("http://payemnt_accountd/8902313");
		request.setRentAmount(10.0f);
		request.setDueDay(10);
		TransactionApiModel apiModel = new TransactionApiModel();
		VendorSearchTransactionResponseModel[] mockResponseArray = new VendorSearchTransactionResponseModel[3];
		when(modelBuilder.buildSearchTransactionApiModel(Mockito.anyLong())).thenReturn(apiModel);
		when(serviceClient.searchTransactionByFinder( Mockito.any(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorSearchTransactionResponseModel[] resposneArray = rentTrackProcessor.searchTransactionByFinder(101l,leaseId,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_getLeaseOrders(){	
		VendorOrdersResponseModel[] mockResponseArray = new VendorOrdersResponseModel[3];
		when(serviceClient.getLeaseOrders( Mockito.any(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorOrdersResponseModel[] resposneArray = rentTrackProcessor.getLeaseOrders(101l,101l,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_getAllUtilities(){	
		VendorUtilityResponseModel[] mockResponseArray = new VendorUtilityResponseModel[3];
		when(serviceClient.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorUtilityResponseModel[] resposneArray = rentTrackProcessor.getAllUtilities(101l,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_getUtilityDetails(){	
		VendorUtilityDetailsResponseModel[] mockResponseArray = new VendorUtilityDetailsResponseModel[3];
		when(serviceClient.getUtilityDetails(Mockito.anyLong(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorUtilityDetailsResponseModel[] resposneArray = rentTrackProcessor.getUtilityDetails(102l,101l,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_getSubscriptionsInfo(){	
		VendorSubscriptionResponseModel[] mockResponseArray = new VendorSubscriptionResponseModel[3];
		when(serviceClient.getSubscriptionsInfo(Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		VendorSubscriptionResponseModel[] resposneArray = rentTrackProcessor.getSubscriptionsInfo(101l,apiNotification);
		 assertEquals(resposneArray.length, mockResponseArray.length);

	}
	
	@Test
	public void test_updateUtiltyStatusRequest(){
		VendorUtilityStatusRequestModel request = new VendorUtilityStatusRequestModel();
		UtilityStatusApiModel apiModel= new UtilityStatusApiModel(); 
		when(modelBuilder.buildUtiltyStatusApiModel(Mockito.any())).thenReturn(apiModel);
		when(serviceClient.updateUtiltyStatus(Mockito.anyLong(),Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		 assertEquals(true, rentTrackProcessor.updateUtiltyStatusRequest(101l,"GAS",request,apiNotification));

	}
	
	@Test
	public void test_reconnectPlaid(){	
		when(serviceClient.reconnectPlaid(Mockito.anyLong(),Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(new VendorPlaidReconnectResponseModel());
		VendorPlaidReconnectResponseModel response = rentTrackProcessor.reconnectPlaid(102l,101l,apiNotification,AppConstants.PLAID_RECONNECTION_PATCH);
		 assertNotNull(response);

	}
	@Test
	public void test_getActiveSubscriptions(){	
		List<VendorSubscriptionResponseModelV2> mockResponse = new ArrayList<VendorSubscriptionResponseModelV2>();
		when(serviceClient.getActiveSubscriptions(Mockito.anyLong(), Mockito.any())).thenReturn(mockResponse);
		List<VendorSubscriptionResponseModelV2> response = rentTrackProcessor.getActiveSubscriptions(101l,apiNotification);
		 assertEquals(response.size(), mockResponse.size());

	}
	@Test
	public void test_deleteSubscription(){	
		when(serviceClient.deleteSubscription(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(true);
		boolean flag = rentTrackProcessor.deleteSubscription(101l, 102l, apiNotification);
		 assertEquals(true,flag);
	}
	@Test
	public void test_getUtilityOrders(){	
		VendorOrdersResponseModel[] mockResponseArray = new VendorOrdersResponseModel[3];
		when(serviceClient.getUtilityOrders( Mockito.any(),Mockito.anyLong(), Mockito.any())).thenReturn(mockResponseArray);
		List<VendorOrdersResponseModel> resposne = rentTrackProcessor.getUtilityOrders(101l,101l,apiNotification);
		 assertEquals(resposne.size(), mockResponseArray.length);

	}
}
