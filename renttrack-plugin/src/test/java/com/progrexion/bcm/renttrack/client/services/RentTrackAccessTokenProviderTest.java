package com.progrexion.bcm.renttrack.client.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;
import com.progrexion.bcm.common.properties.RentTrackAPIOAuthConfigProperties;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.renttrack.client.model.RTAccessTokenResponseModel;

import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class RentTrackAccessTokenProviderTest{
	
	@Mock
	CustomerRepository customerRepo;
	
	@Mock
	private RentTrackAPIOAuthConfigProperties rtPartnerProperties;
	
	@InjectMocks
	private  RentTrackAccessTokenProvider accessTokenProvider;
	private BCMCustomer customer;
	private Optional<BCMCustomer> customerOpt;
	private VendorTokenResponseModel tokenModel;
	private RTAccessTokenResponseModel rtTokenModel;
	@Mock
	private WebTestClient webClient;
	
	@Autowired
	private ObjectMapper  objectMapper = new ObjectMapper();
	@Mock
	private ObjectMapper mockObjectMapper;
	
	@Mock
	private WebClientErrorHandler webClientErrorHandler;
	@Mock
	private ModelMapper modelMapper = new ModelMapper() ;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		customer = new BCMCustomer();
		customer.setAccessToken("324dvgdghghj4353egdgh");
		customer.setCustomerDataId(1l);
		customer.setCustEmail("test@gmail.com");
		customer.setBrand("CCOM");
		customer.setRefreshToken("adsfsfs45456467ytgghgdgf");
		customerOpt = Optional.of(customer);
		
		tokenModel = new VendorTokenResponseModel();
		tokenModel.setAccessToken("ASDDD543693654GHGHJMH");
		tokenModel.setExpiresIn(3l);
		tokenModel.setRefreshToken("GHKKASDDD543693654GHGHJMH");
		tokenModel.setTokenType("access_token");
		tokenModel.setScope("testScope");
		when(customerRepo.findByUcIdAndBrand(Mockito.anyLong(),Mockito.any())).thenReturn(customerOpt);	
		when(customerRepo.findByCustomerDataId(Mockito.anyLong())).thenReturn(customerOpt);	
		when(rtPartnerProperties.getBaseUrl()).thenReturn("https://my.sandbox.rt-stg.com");
		when(rtPartnerProperties.getTokenUrl()).thenReturn("/oauth/v2/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&username=USER_NAME&password=SECRET&grant_type=GRANT_TYPE");
		when(rtPartnerProperties.getClientId()).thenReturn("26_26twn8ycdge8wo4gcc8k8kwwkwwocw00goc4o488s08g00w00w");
		when(rtPartnerProperties.getClientSecret()).thenReturn("4k7iv14um3k0cwks4gwwg8k80oc4o48cgkog8g0cs48g884cww");
		when(rtPartnerProperties.getGrantType()).thenReturn("password");
		when(rtPartnerProperties.getUsername()).thenReturn("ibell@progrexion.com");
		when(rtPartnerProperties.getPassword()).thenReturn("cdcR0cks!");
		when(rtPartnerProperties.getRefreshTokenUrl()).thenReturn("/oauth/v2/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&refresh_token=REFRESH_TOKEN&grant_type=GRANT_TYPE");
		
		rtTokenModel = new RTAccessTokenResponseModel();
		rtTokenModel.setAccessToken("ASDDD543693654GHGHJMH");
		rtTokenModel.setExpiresIn(3l);
		rtTokenModel.setRefreshToken("GHKKASDDD543693654GHGHJMH");
		rtTokenModel.setTokenType("access_token");		
	}
	
	@Test
	public void test_getCurrentTokensOfUser() {
			
		VendorTokenResponseModel tokens = accessTokenProvider.getCurrentTokensOfUser(101l, "CCOM");
		assertNotNull(tokens);
	}
	
	@Test
	public void test_refreshTokensOfUser() throws JsonProcessingException {

		when(customerRepo.findByUcIdAndBrand(Mockito.anyLong(),Mockito.any())).thenReturn(customerOpt);
		when(customerRepo.save(Mockito.any())).thenReturn(null);
		getWebClientMock(objectMapper.writeValueAsString(tokenModel));
		mockObjectMapper = objectMapper;
		 when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(tokenModel);
		VendorTokenResponseModel tokens = accessTokenProvider.refreshTokensOfUser(101l, "CCOM","CREATE_TENANT");
		assertNotNull(tokens);
	}

	
	@Test
	public void test_getCurrentAccessTokenOfUser() {
		String token = accessTokenProvider.getCurrentAccessTokenOfUser(101l, "CCOM");
		assertNotNull(token);
	}
	
	@Test
	public void test_refreshAndGetAccessTokenOfUser() throws JsonProcessingException {
		when(customerRepo.findByUcIdAndBrand(Mockito.anyLong(),Mockito.any())).thenReturn(customerOpt);
		getWebClientMock(objectMapper.writeValueAsString(tokenModel));
		mockObjectMapper = objectMapper;
		 when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(tokenModel);
		String token = accessTokenProvider.refreshAndGetAccessTokenOfUser(101l, "CCOM");
		assertNotNull(token);
	}
	
	@Test
	public void test_getFirstTimeUserTokens() throws JsonProcessingException {
		getWebClientMock(objectMapper.writeValueAsString(tokenModel));
		mockObjectMapper = objectMapper;
		VendorTokenResponseModel token = accessTokenProvider.getFirstTimeUserTokens("test@gmail.com","Password@1");
		assertNull(token);
	}
	
	@Test
	public void test_getPartnerTokens() throws IOException {
		getWebClientMock(objectMapper.writeValueAsString(tokenModel));
		mockObjectMapper = objectMapper;
		VendorTokenResponseModel token = accessTokenProvider.getPartnerTokens();
		assertNull(token);
	}
	
	
	
	@Test
	public void test_getCurrentTokensOfUserbyCustomerId() {
		when(customerRepo.findByCustomerDataId(Mockito.anyLong())).thenReturn(customerOpt);		
		VendorTokenResponseModel tokens = accessTokenProvider.getCurrentTokensOfUser(101l);
		assertNotNull(tokens);
	}

	@Test
	public void test_getCurrentAccessTokenOfUserbyCustomerId() {
		when(customerRepo.findByCustomerDataId(Mockito.anyLong())).thenReturn(customerOpt);		
		String token = accessTokenProvider.getCurrentAccessTokenOfUser(101l);
		assertNotNull(token);
	}

	@Test
	public void test_refreshTokensOfUserbyCustomerId() throws JsonProcessingException
	 {
		when(customerRepo.findByCustomerDataId(Mockito.anyLong())).thenReturn(customerOpt);	
		getWebClientMock(objectMapper.writeValueAsString(tokenModel));
		mockObjectMapper = objectMapper;
		 when(modelMapper.map(Mockito.any(),Mockito.any())).thenReturn(tokenModel);
		VendorTokenResponseModel tokens = accessTokenProvider.refreshTokensOfUser(101l, RentTrackClientRequestTypesEnum.CREATE_LEASE);
		assertNotNull(tokens);	
	}
	

	
	private  void getWebClientMock(final String resp) {
		ExchangeFilterFunction mockFunction = ExchangeFilterFunction.ofResponseProcessor(clientResponse  -> {
			return Mono.just(clientResponse);});
		when(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter()).thenReturn(mockFunction);
		when((rtPartnerProperties.getBaseUrl())).thenReturn("https://my.sandbox.rt-stg.com");

	}
	
	
}
