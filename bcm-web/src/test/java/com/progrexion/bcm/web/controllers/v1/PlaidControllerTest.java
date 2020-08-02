package com.progrexion.bcm.web.controllers.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import com.progrexion.bcm.common.model.v1.PaymentAccountResponseModel;
import com.progrexion.bcm.common.model.v1.PlaidReconnectResponseModel;
import com.progrexion.bcm.models.v1.ErrorDto;
import com.progrexion.bcm.models.v1.PaymentAccountRequestDto;
import com.progrexion.bcm.models.v1.PaymentAccountResponseDto;
import com.progrexion.bcm.models.v1.PlaidReconnectResponseDto;
import com.progrexion.bcm.services.v1.PlaidService;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class PlaidControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	 @Autowired
	private WebApplicationContext wac;
	
	@MockBean
	private PlaidService plaidService;
	
    @InjectMocks
    private PlaidController plaidController;
	@Autowired 
	private ObjectMapper  objectMapper;
	
	private PaymentAccountRequestDto paymentAccountRequestDto;
	private PaymentAccountResponseModel paymentAccountResposneModel;
	private Long ucid;	
	private String accessToken;	
	private String brand;
	private String api;
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(plaidController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        ucid = (long) 101;
        brand = "tenant";
        paymentAccountRequestDto = new PaymentAccountRequestDto();
        paymentAccountRequestDto.setPublicToken("public-sandbox-2346546");
        paymentAccountRequestDto.setPlaidAccountId("12345435");
		accessToken="Bearer "+getAccessToken();
		api = "/v1/customer/payment_account/"+ucid+"/"+brand;
		
		paymentAccountResposneModel = new PaymentAccountResponseModel();
		paymentAccountResposneModel.setId(1001l);

    }
    @Test
	public void testCreatePaymentAccountSuccessFlow() throws Exception {


		String requestPayload=objectMapper.writeValueAsString(paymentAccountRequestDto);
		when(plaidService.createPlaidPaymentAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(paymentAccountResposneModel);
		
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		PaymentAccountResponseDto reponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentAccountResponseDto.class);
		assertEquals(paymentAccountResposneModel.getId(), reponseDto.getId());
	}
    
	@Test
	public void testCreatePaymentAccountExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		
		when(plaidService.createPlaidPaymentAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		String requestPayload=objectMapper.writeValueAsString(paymentAccountRequestDto);
		
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
	public void testCreatePaymentAccountUnAuthorizedFlow() throws Exception {

		PaymentAccountResponseModel responseModel = new PaymentAccountResponseModel();
		responseModel.setId(1001L);
		String requestPayload=objectMapper.writeValueAsString(paymentAccountRequestDto);
		when(plaidService.createPlaidPaymentAccount(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(responseModel);
		
		mockMvc.perform(post(api)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetPaymentAccountSuccessFlow() throws Exception {
		when(plaidService.getPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenReturn(paymentAccountResposneModel);
		MvcResult result =mockMvc.perform(get(api)
					.header(HttpHeaders.AUTHORIZATION, accessToken)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();
		PaymentAccountResponseDto reponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentAccountResponseDto.class);
		assertEquals(paymentAccountResposneModel.getId(), reponseDto.getId());
	}
		
	@Test
	public void testGetPaymentAccountSuccessFlowWithNull() throws Exception {
		when(plaidService.getPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenReturn(null);
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andReturn();
		assertNotNull(result);
	}
	@Test
	public void testGetPaymentAccountExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		when(plaidService.getPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION,accessToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}		
	@Test
	public void testGetPaymentAccountUnAuthorizedFlow() throws Exception {
		PaymentAccountResponseModel responseModel = new PaymentAccountResponseModel();
		responseModel.setId(1001L);
		when(plaidService.getPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenReturn(responseModel);			
		mockMvc.perform(get(api)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetPublicTokenForReconnectSuccessFlow() throws Exception {
		when(plaidService.reconnectPlaid(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenReturn(new PlaidReconnectResponseModel());
		MvcResult result =mockMvc.perform(put(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();
		PlaidReconnectResponseDto reponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PlaidReconnectResponseDto.class);
		assertNotNull(reponseDto);
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
	public void testGetReconnectPlaidSuccessFlow() throws Exception {

		when(plaidService.reconnectPlaid(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(new PlaidReconnectResponseModel());
		MvcResult result = mockMvc.perform(patch(api).header(HttpHeaders.AUTHORIZATION, accessToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		PlaidReconnectResponseDto reponseDto = objectMapper.readValue(result.getResponse().getContentAsString(),
				PlaidReconnectResponseDto.class);

		assertNotNull(reponseDto);
	}
	
}
