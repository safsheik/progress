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
import com.progrexion.bcm.common.model.v1.MatchTransactionResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.models.v1.ErrorDto;
import com.progrexion.bcm.models.v1.MatchTransactionRequestDto;
import com.progrexion.bcm.models.v1.MatchTransactionResponseDto;
import com.progrexion.bcm.models.v1.TransactionRequestDto;
import com.progrexion.bcm.models.v1.TransactionResponseDto;
import com.progrexion.bcm.services.v1.TransactionService;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	 @Autowired
	private WebApplicationContext wac;
	
	@MockBean
	private TransactionService tranService;
	
    @InjectMocks
    private TransactionController transController;
	@Autowired 
	private ObjectMapper  objectMapper;
	
	private MatchTransactionRequestDto matchTransactionRequestDto;
	private MatchTransactionResponseModel matchTransactionResponseModel;
	private TransactionRequestDto transactionRequestDto;
	private TransactionResponseModel transactionResponseModel;
	private Long ucid;
	private String brand;

	private String api, apiTransaction;
	private String accessToken;
	
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        ucid = (long) 101;
        brand = "CCOM";
        matchTransactionRequestDto = new MatchTransactionRequestDto();
        
        matchTransactionRequestDto.setTransactionID("ojEBr8gApyspayb3EeZ1Uy8dNmyRz6FRe6E6m");
        matchTransactionRequestDto.setAmount("6.33");
        matchTransactionRequestDto.setName("Uber 072515 SF**POOL**");
        matchTransactionRequestDto.setCategoryId("22006001");
		accessToken="Bearer "+getAccessToken();
		api = "/v1/customer/transaction/match/"+ucid+"/"+brand;
		
		matchTransactionResponseModel = new MatchTransactionResponseModel();
		matchTransactionResponseModel.setId((long) 101);
		
		//TransactionFinder call
		transactionRequestDto = new TransactionRequestDto();
		
		transactionRequestDto.setRentAmount(95);
		transactionRequestDto.setDueDay(5);
		apiTransaction = "/v1/customer/transaction/"+ucid+"/"+brand;
		
		transactionResponseModel = new TransactionResponseModel();

		
    }
	@Test
	public void testMatchTrxSuccessFlow() throws Exception {

		MatchTransactionResponseModel matchTransactionResponseModel = new MatchTransactionResponseModel();
		matchTransactionResponseModel.setId(1001l);
		String requestPayload=objectMapper.writeValueAsString(matchTransactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"2019-10-15\"");
		when(tranService.matchTransaction(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(matchTransactionResponseModel);
		
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		MatchTransactionResponseDto matchTransactionResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), MatchTransactionResponseDto.class);
		assertEquals(matchTransactionResponseModel.getId(), Long.valueOf(matchTransactionResponseDto.getId()));
	}
	
	
	
	
	
	@Test
	public void testMatchTrxExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		MatchTransactionResponseModel matchTransactionResponseModel = new MatchTransactionResponseModel();
		matchTransactionResponseModel.setId(1001l);	
		when(tranService.matchTransaction(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		String requestPayload=objectMapper.writeValueAsString(matchTransactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"2019-10-15\"");
		MvcResult result =mockMvc.perform(post(api)
				.header(HttpHeaders.AUTHORIZATION,accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		//System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testMatchTrxUnAuthorizedFlow() throws Exception {

		MatchTransactionResponseModel matchTransactionResponseModel = new MatchTransactionResponseModel();
		matchTransactionResponseModel.setId(1001l);		
		String requestPayload=objectMapper.writeValueAsString(matchTransactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"2019-10-15\"");
		when(tranService.matchTransaction(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(matchTransactionResponseModel);
		
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
	public void testCreateTrxFinderSuccessFlow() throws Exception {

		String requestPayload = objectMapper.writeValueAsString(transactionRequestDto);
		when(tranService.createTransactionFinder(Mockito.anyLong(), Mockito.any(), Mockito.any()))
				.thenReturn(transactionResponseModel);

		MvcResult result = mockMvc
				.perform(
						post(apiTransaction).header(HttpHeaders.AUTHORIZATION, accessToken)
						.contentType(MediaType.APPLICATION_JSON)
							.content(requestPayload).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();

		TransactionResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(),
				TransactionResponseDto.class);
		assertNotNull(response);
		assertEquals(transactionResponseModel.getStatus(), response.getStatus());

	}
	
	@Test
	public void testCreateTrxFinderExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		transactionResponseModel.setStatus("Missing");
		when(tranService.createTransactionFinder(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		String requestPayload=objectMapper.writeValueAsString(transactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"95\"");
		MvcResult result =mockMvc.perform(post(apiTransaction)
				.header(HttpHeaders.AUTHORIZATION,accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());

	}
	
	@Test
	public void  testCreateTrxFinderUnAuthorizedFlow() throws Exception {
		
		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		transactionResponseModel.setStatus("Missing");	
		String requestPayload=objectMapper.writeValueAsString(transactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"95\"");
		when(tranService.createTransactionFinder(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(transactionResponseModel);
		
		mockMvc.perform(post(apiTransaction)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	public void testSearchTrxFinderExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;

		when(tranService.searchTransactionDetails(Mockito.anyLong(), Mockito.any()))
				.thenThrow(new BCMModuleException(exceptionEnum));

		MvcResult result = mockMvc.perform(get(apiTransaction).header(HttpHeaders.AUTHORIZATION, accessToken))
				.andExpect(status().is(exceptionEnum.getHttpStatusCode())).andReturn();
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());

	}
	
	@Test
	public void testSearchTrxFinderSuccessFlow() throws Exception {

		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		when(tranService.searchTransactionDetails(Mockito.anyLong(), Mockito.any()))
				.thenReturn(transactionResponseModel);

		mockMvc.perform(get(apiTransaction).header(HttpHeaders.AUTHORIZATION, accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}
	
	@Test
	public void  testSearchTrxFinderUnAuthorizedFlow() throws Exception {

		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		transactionResponseModel.setStatus("Missing");	
		String requestPayload=objectMapper.writeValueAsString(transactionRequestDto);
		requestPayload=requestPayload.replace("null", "\"2019-10-15\"");
		when(tranService.createTransactionFinder(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(transactionResponseModel);
		
		mockMvc.perform(post(apiTransaction)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

}
