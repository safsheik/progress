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
import com.progrexion.bcm.common.model.v1.CustomerStatusResponseModel;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.models.v1.AddressDto;
import com.progrexion.bcm.models.v1.CreateUserRequestDto;
import com.progrexion.bcm.models.v1.CreateUserResponseDto;
import com.progrexion.bcm.models.v1.CustomerStatusResponseDto;
import com.progrexion.bcm.models.v1.ErrorDto;
import com.progrexion.bcm.models.v1.LandlordDto;
import com.progrexion.bcm.models.v1.LeaseRequestDto;
import com.progrexion.bcm.services.v1.CustomerService;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	 @Autowired
	private WebApplicationContext wac;
	
	@MockBean
	private CustomerService custService;
	
    @InjectMocks
    private CustomerController customerController;
	@Autowired 
	private ObjectMapper  objectMapper;
	
	private CreateUserRequestDto createUserRequestDto;
	private LeaseRequestDto leaseRequestDto;
	private Long ucid;
	private String brand;
	private CustomerStatusResponseDto customerStatusResponseDto;
	private String api;
	private String accessToken;
	@Autowired 
	private ModelMapper  modelMapper;
	
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        ucid = (long) 101;
        brand = "CCOM";
		createUserRequestDto = new CreateUserRequestDto();
		createUserRequestDto.setEmail("suvija66@pgx.com");
		createUserRequestDto.setType("tenant");
		createUserRequestDto.setFirstName("TestUser");
		createUserRequestDto.setLastName("TestUser");
        leaseRequestDto = new LeaseRequestDto();
        Address address = new Address("Plot A5","STREET","CITY","4900051","STATE","US");
        Landlord landlord =new Landlord("Test","John","7894561234","abc@test.com");
        leaseRequestDto.setAddress(modelMapper.map(address,AddressDto.class));
        leaseRequestDto.setDueDay(1);
        leaseRequestDto.setLandlord(modelMapper.map(landlord,LandlordDto.class));
        leaseRequestDto.setRent((float)500.00);
        createUserRequestDto.setLeaseData(leaseRequestDto);
		accessToken="Bearer "+getAccessToken();
		api = "/v1/customer/"+ucid+"/"+brand;
		customerStatusResponseDto = new CustomerStatusResponseDto();
		customerStatusResponseDto.setStatus("ACTIVE");
    }
	@Test
	public void testCreateCustomerSuccessFlow() throws Exception {

		CreateUserResponseModel createUserResponseModel = new CreateUserResponseModel();
		createUserResponseModel.setId(1001l);	
		String requestPayload=objectMapper.writeValueAsString(createUserRequestDto);
		requestPayload=requestPayload.replace("null", "\"1992-01-01\"");
		when(custService.createUserAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(createUserResponseModel);
		
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		CreateUserResponseDto createUserReponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), CreateUserResponseDto.class);
		assertEquals(createUserResponseModel.getId(), Long.valueOf(createUserReponseDto.getId()));
	}
	
	@Test
	public void testCreateCustomerExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		
		when(custService.createUserAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		String requestPayload=objectMapper.writeValueAsString(createUserRequestDto);
		requestPayload=requestPayload.replace("null", "\"1992-01-01\"");
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
	public void testCreateCustomerUnAuthorizedFlow() throws Exception {

		CreateUserResponseModel createUserResponseModel = new CreateUserResponseModel();
		createUserResponseModel.setId(1001l);	
		String requestPayload=objectMapper.writeValueAsString(createUserRequestDto);
		requestPayload=requestPayload.replace("null", "\"1992-01-01\"");
		when(custService.createUserAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(createUserResponseModel);
		
		mockMvc.perform(post(api)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
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
	public void testGetCustomerStatusSuccessFlow() throws Exception {

		CustomerStatusResponseModel customerStatusResponseModel = new CustomerStatusResponseModel();
		customerStatusResponseModel.setStatus("ACTIVE");
		when(custService.getCustomerStatusInfo(Mockito.anyLong(), Mockito.any())).thenReturn(customerStatusResponseModel);

		MvcResult result = mockMvc
				.perform(get(api).header(HttpHeaders.AUTHORIZATION, accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		
		CustomerStatusResponseDto customerStatusResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerStatusResponseDto.class);
		assertEquals(customerStatusResponseModel.getStatus(), customerStatusResponseDto.getStatus());
	}
	
	@Test
	public void testGetCustomerStatusExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;

		when(custService.getCustomerStatusInfo(Mockito.anyLong(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		MvcResult result = mockMvc.perform(get(api).header(HttpHeaders.AUTHORIZATION, accessToken)).andExpect(status().is(exceptionEnum.getHttpStatusCode())).andReturn();

		System.out.println("Error " + result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
}
