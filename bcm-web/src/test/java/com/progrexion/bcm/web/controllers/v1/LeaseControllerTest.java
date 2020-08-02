package com.progrexion.bcm.web.controllers.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.CreateUserResponseModel;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.v1.LeaseOrdersResponseModel;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.models.v1.AddressDto;
import com.progrexion.bcm.models.v1.CreateUserResponseDto;
import com.progrexion.bcm.models.v1.ErrorDto;
import com.progrexion.bcm.models.v1.LandlordDto;
import com.progrexion.bcm.models.v1.LeaseOrdersResponseDto;
import com.progrexion.bcm.models.v1.LeaseRequestDto;
import com.progrexion.bcm.models.v1.LeaseResponseDto;
import com.progrexion.bcm.services.v1.LeaseService;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class LeaseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	 @Autowired
	private WebApplicationContext wac;
	
	@MockBean
	private LeaseService leaseService;
	
    @InjectMocks
    private LeaseController leaseController;
	@Autowired 
	private ObjectMapper  objectMapper;
	
	@Autowired 
	private ModelMapper  modelMapper;
	
	private LeaseRequestDto leaseRequestDto;
	private Long ucid;
	private String accessToken;	
	private LeaseResponseModel leaseResponseModel;
	private String brand;
	private String api;
	private String ordersApi;
	private LeaseOrdersResponseModel[] leaseOrdersResponseModel;
	private LeaseOrdersResponseDto[] leaseOrdersResponseDto;
	private boolean isLeaseCreated;
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        ucid = (long) 101;
        brand = "CCOM";
        leaseRequestDto = new LeaseRequestDto();
        Address address = new Address("Plot A5","STREET","CITY","4900051","STATE","US");
        Landlord landlord =new Landlord("","John","7894561234","abc@test.com");
        leaseRequestDto.setAddress(modelMapper.map(address,AddressDto.class));
        leaseRequestDto.setDueDay(1);
        leaseRequestDto.setLandlord(modelMapper.map(landlord,LandlordDto.class));
        leaseRequestDto.setRent((float)500.00);
		accessToken="Bearer "+getAccessToken();
		api = "/v1/customer/lease/"+ucid+"/"+brand;
		ordersApi = "/v1/customer/lease/orders/"+ucid+"/"+brand;
		
		leaseResponseModel=new LeaseResponseModel();
		leaseResponseModel.setLeaseId(1001l);
		leaseResponseModel.setAddress(address);
		leaseResponseModel.setDueDay("1");
		leaseResponseModel.setLandlordModel(landlord);
		leaseResponseModel.setRent("500.00");
		isLeaseCreated = true;
		
		leaseOrdersResponseModel = new LeaseOrdersResponseModel[3];
		leaseOrdersResponseDto = new LeaseOrdersResponseDto[3];

    }
    
	@Test
	public void testCreateLeaseSuccessFlow() throws Exception {
	
		String requestPayload=objectMapper.writeValueAsString(leaseRequestDto);
		when(leaseService.createOrUpdateLease(Mockito.anyLong(), Mockito.any(), Mockito.any(),  Mockito.anyBoolean() )).thenReturn(leaseResponseModel);
		
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		LeaseResponseDto reponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), LeaseResponseDto.class);
		assertEquals(leaseResponseModel.getLeaseId(), reponseDto.getId());
		assertEquals(leaseResponseModel.getAddress().getAddress1(), reponseDto.getAddress().getAddress1());
		assertEquals(leaseResponseModel.getAddress().getAddress2(), reponseDto.getAddress().getAddress2());
		assertEquals(leaseResponseModel.getAddress().getCity(), reponseDto.getAddress().getCity());
		assertEquals(leaseResponseModel.getAddress().getZip(), reponseDto.getAddress().getZip());
		assertEquals(leaseResponseModel.getAddress().getCountry(), reponseDto.getAddress().getCountry());
	}
		
	@Test
	public void testCreateLeaseExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		
		when(leaseService.createLease(Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		String requestPayload=objectMapper.writeValueAsString(leaseRequestDto);
		
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION,accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testCreateLeaseUnAuthorizedFlow() throws Exception {

		String requestPayload=objectMapper.writeValueAsString(leaseRequestDto);
		
		when(leaseService.createLease( Mockito.any(), Mockito.any())).thenReturn(isLeaseCreated);
		
		mockMvc.perform(post(api)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	
	@Test
	public void testGetLeaseSuccessFlow() throws Exception {

		
		when(leaseService.getLeaseInfo(Mockito.anyLong(), Mockito.any())).thenReturn(leaseResponseModel);
		
		mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
	}
	
	
	
	
	
	@Test
	public void testGetLeaseExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		
		when(leaseService.getLeaseInfo(Mockito.anyLong(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION,accessToken))
				.andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testGetLeaseUnAuthorizedFlow() throws Exception {
		
		when(leaseService.getLeaseInfo(Mockito.anyLong(), Mockito.any())).thenReturn(leaseResponseModel);
		
		mockMvc.perform(get(api)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	
	private String getAccessToken() throws Exception {
		  
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("username", "masanaliyarshahulhameed@progrexion.com");
	    params.add("password", "saf");
	 
	    ResultActions result 
	      = mockMvc.perform(post("/oauth/token")
	        .params(params)
	        .header(HttpHeaders.AUTHORIZATION,"Basic Y2xpZW50OnNlY3JldA==")
	        .accept("application/x-www-form-urlencoded;charset=UTF-8"))
	        .andExpect(status().isOk());
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    log.info("Token Created");
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	@Test
	public void testGetLeaseOrderSuccessFlow() throws Exception {

		when(leaseService.getLeaseOrders(Mockito.anyLong(), Mockito.any())).thenReturn(leaseOrdersResponseModel);
		
		MvcResult result =mockMvc.perform(get(ordersApi)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		leaseOrdersResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), LeaseOrdersResponseDto[].class);
		assertEquals(leaseOrdersResponseDto.length,leaseOrdersResponseModel.length);
		
	}
	
	@Test
	public void testGetLeaseOrderSuccessFlowWithEmptyResponse() throws Exception {

		when(leaseService.getLeaseOrders(Mockito.anyLong(), Mockito.any())).thenReturn(new LeaseOrdersResponseModel[0]);
		
		MvcResult result =mockMvc.perform(get(ordersApi)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
		assertNotNull(result);
		
	}
	
	@Test
	public void testGetLeaseOrderExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		
		when(leaseService.getLeaseOrders(Mockito.anyLong(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		
		MvcResult result =mockMvc.perform(get(ordersApi)
				.header(HttpHeaders.AUTHORIZATION,accessToken))
				.andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testGetLeaseOrderUnAuthorizedFlow() throws Exception {
		
		when(leaseService.getLeaseOrders(Mockito.anyLong(), Mockito.any())).thenReturn(leaseOrdersResponseModel);
		
		mockMvc.perform(get(ordersApi)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
}
