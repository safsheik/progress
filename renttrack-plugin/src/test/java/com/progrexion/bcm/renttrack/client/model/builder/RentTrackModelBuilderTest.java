package com.progrexion.bcm.renttrack.client.model.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserRequestModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;

@RunWith(MockitoJUnitRunner.class)
public class RentTrackModelBuilderTest {

	@Mock
	private ModelMapper modelMapper;
	@InjectMocks
	private RentTrackModelBuilder builder;
	
	@Mock
	RentTrackConfigProperties property;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(property.getTransactionAmountMaxBy()).thenReturn(99f);
		when(property.getTransactionAmountMinBy()).thenReturn(1f);
		when(property.getTransactionWindowOpenBy()).thenReturn(7);
		when(property.getTransactionWindowCloseBy()).thenReturn(5);
	}
	@Test
	public void test_buildCreateSubscriptionApiModel() {
		VendorSubscriptionRequestModel requestModel = new VendorSubscriptionRequestModel();
		requestModel.setPlan("consumer_lite");
		requestModel.setPromotionCode("CREDITCOMFREE");
		SubscriptionApiModel apiModel = builder.buildCreateSubscriptionApiModel(requestModel);
		assertEquals("consumer_lite",apiModel.getPlanName());
		assertEquals("CREDITCOMFREE",apiModel.getPromotionCode());
	}
	
	@Test
	public void test_createUserAccountApiRequestModel() {
		VendorCreateUserRequestModel requestModel = new VendorCreateUserRequestModel();
		requestModel.setFirstName("testuser");
		requestModel.setPassword("password@1");
		requestModel.setEmail("test@gmail.com");
		requestModel.setType("tenant");
		RTCreateUserRequestModel apiModel =builder.createUserAccountApiRequestModel(requestModel);
		assertEquals("testuser",apiModel.getFirstName());
		assertEquals("password@1",apiModel.getPassword());
		assertEquals("test@gmail.com",apiModel.getEmail());
		assertEquals("tenant",apiModel.getType());
		
	}

	@Test
	public void test_buildLeaseApiModel() {
		VendorLeaseRequestModel request = new VendorLeaseRequestModel();
		Address address=new Address("Plot A5","STREET","CITY","4900051","STATE","US");
		Landlord landlord=new Landlord("","John","7894561234","abc@test.com");
		request.setAddress(address);
		request.setLandlordModel(landlord);
		LeaseApiModel apiModel = builder.buildLeaseApiModel(request);
		assertEquals("Plot A5",apiModel.getAddress().getAddress1());
		assertEquals("STREET",apiModel.getAddress().getAddress2());
		assertEquals("CITY",apiModel.getAddress().getCity());
		assertEquals("STATE",apiModel.getAddress().getState());
		assertEquals("US",apiModel.getAddress().getCountry());
	}

	@Test
	public void test_updateLeaseApiModel() {
		VendorLeaseRequestModel request = new VendorLeaseRequestModel();
		Address address=new Address("Plot A5","STREET","CITY","4900051","STATE","US");
		Landlord landlord=new Landlord("","John","7894561234","abc@test.com");
		request.setAddress(address);
		request.setLandlordModel(landlord);
		LeaseApiModel apiModel = builder.updateLeaseApiModel(request);
		assertEquals("Plot A5",apiModel.getAddress().getAddress1());
		assertEquals("STREET",apiModel.getAddress().getAddress2());
		assertEquals("CITY",apiModel.getAddress().getCity());
		assertEquals("STATE",apiModel.getAddress().getState());
		assertEquals("US",apiModel.getAddress().getCountry());
	}

	@Test
	public void test_buildCreatePaymentAccountApiModel() {
		
		VendorPaymentAccountRequestModel request = new VendorPaymentAccountRequestModel();
		request.setPayType("nopay");
		request.setPublicToken("public-sandbox");
		PaymentAccountApiModel apiModel = new PaymentAccountApiModel();
		apiModel.setPayType(request.getPayType());
		apiModel.setPublicToken(request.getPublicToken());
		 when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(apiModel);
		apiModel = builder.buildCreatePaymentAccountApiModel(request);
		assertEquals("nopay",apiModel.getPayType());
		assertEquals("public-sandbox",apiModel.getPublicToken());
	}
	@Test
	public void test_buildTransactionApiModel() {
		VendorTransactionRequestModel request = new VendorTransactionRequestModel();
		request.setLeaseUrl("http://leases/890");
		request.setPaymentAccountUrl("http://payemnt_accountd/8902313");
		request.setRentAmount(10.0f);
		request.setDueDay(10);
		TransactionApiModel apiModel = builder.buildTransactionApiModel(request);
		assertEquals("http://payemnt_accountd/8902313",apiModel.getPaymentAccountUrl());
		assertEquals("http://leases/890",apiModel.getLeaseUrl());
	}

	@Test
	public void test_buildMatchTrxApiModel() {
		VendorMatchTransactionRequestModel request = new VendorMatchTransactionRequestModel();
		request.setTransactionID("134fsfsdfdsfdtr");

		MatchTransactionArrayApiModel apiModel = builder.buildMatchTrxApiModel(request);
		assertEquals("134fsfsdfdsfdtr",apiModel.getTransactions()[0].getTransactionID());
	}
	
	@Test
	public void test_buildUtiltyStatusApiModel() {
		VendorUtilityStatusRequestModel request = new VendorUtilityStatusRequestModel();
	    request.setStatus("current");
	    UtilityStatusApiModel apiModel = new UtilityStatusApiModel();
	    apiModel.setStatus("current");
	    when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(apiModel);
	    apiModel = builder.buildUtiltyStatusApiModel(request);
	    assertEquals("current",apiModel.getStatus());
		
	}

	@Test
	public void test_buildSearchTransactionApiModel() {
	    TransactionApiModel apiModel = new TransactionApiModel();
	    apiModel.setLeaseId(100l);
	    apiModel = builder.buildSearchTransactionApiModel(101l);
	    assertNotNull(apiModel);
		
	}
}
