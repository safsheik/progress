package com.progrexion.bcm.web.controllers.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.progrexion.bcm.common.model.v1.UtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityResponseModel;
import com.progrexion.bcm.models.v1.ErrorDto;
import com.progrexion.bcm.models.v1.UtilityDetailsResponseDto;
import com.progrexion.bcm.models.v1.UtilityResponseDto;
import com.progrexion.bcm.models.v1.UtilityStatusRequestDto;
import com.progrexion.bcm.services.v1.UtilityService;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UtilityControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	 @Autowired
	private WebApplicationContext wac;
	
	@MockBean
	private UtilityService utilityService;
	
    @InjectMocks
    private UtilityController utilityController;
	@Autowired 
	private ObjectMapper  objectMapper;
	
	private UtilityResponseDto[] responseDtoArray;
	private UtilityDetailsResponseDto[] responseDetailsDtoArray;
	private UtilityResponseModel[] responseModelArray;
	private UtilityDetailsResponseModel[] responseDetailsModelArray;
	private Long ucid;
	private String brand;
	private String api;
	private String accessToken;
	private UtilityStatusRequestDto utilityStatusRequestDto;
	
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        ucid = (long) 101;
        brand = "CCOM";
        responseDtoArray = new UtilityResponseDto[3];
        responseDetailsDtoArray = new UtilityDetailsResponseDto[3];
		accessToken="Bearer "+getAccessToken();
		api = "/v1/customer/utility/"+ucid+"/"+brand;
		
		utilityStatusRequestDto = new UtilityStatusRequestDto();
		utilityStatusRequestDto.setOptFor("GAS");
		utilityStatusRequestDto.setUtilityId("4013310819");
    }
	@Test
	public void testGetAllUtiltiesSuccessFlow() throws Exception {
		responseModelArray = new UtilityResponseModel[3];
		when(utilityService.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(responseModelArray);
		
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		responseDtoArray = objectMapper.readValue(result.getResponse().getContentAsString(), UtilityResponseDto[].class);
		assertEquals(responseDtoArray.length,responseModelArray.length);
	}
	
	
	
	
	
	@Test
	public void testGetAllUtiltiesExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		responseModelArray = new UtilityResponseModel[3];
		when(utilityService.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testGetAllUtiltiesUnAuthorizedFlow() throws Exception {

		responseModelArray = new UtilityResponseModel[3];	
		
		when(utilityService.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(responseModelArray);
		
		mockMvc.perform(get(api)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetUtilityDetailsSuccessFlow() throws Exception {
		responseDetailsModelArray = new UtilityDetailsResponseModel[3];
		when(utilityService.getUtilityDetails(Mockito.anyLong(), Mockito.any(),Mockito.anyLong())).thenReturn(responseDetailsModelArray);
		api = api + "/4146935397";
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		responseDetailsDtoArray = objectMapper.readValue(result.getResponse().getContentAsString(), UtilityDetailsResponseDto[].class);
		assertEquals(responseDetailsDtoArray.length,responseDetailsModelArray.length);
	}
	
	
	
	
	
	@Test
	public void testGetUtilityDetailsExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		responseDetailsModelArray = new UtilityDetailsResponseModel[3];
		when(utilityService.getUtilityDetails(Mockito.anyLong(), Mockito.any(),Mockito.anyLong())).thenThrow(new BCMModuleException(exceptionEnum));
		api = api + "/4146935397";
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testGetUtilityDetailsUnAuthorizedFlow() throws Exception {

		responseDetailsModelArray = new UtilityDetailsResponseModel[3];	
		api = api + "/4146935397";
		when(utilityService.getUtilityDetails(Mockito.anyLong(), Mockito.any(),Mockito.anyLong())).thenReturn(responseDetailsModelArray);
		
		mockMvc.perform(get(api)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testUpdateUtilityStatusSuccessFlow() throws Exception {
		String requestPayload=objectMapper.writeValueAsString(utilityStatusRequestDto);
		when(utilityService.updateUtiltyStatus(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(true);
		mockMvc.perform(patch(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
	}
	
	
	
	@Test
	public void testUpdateUtilityStatusExceptionFlow() throws Exception {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
		String requestPayload=objectMapper.writeValueAsString(utilityStatusRequestDto);
		when(utilityService.updateUtiltyStatus(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenThrow(new BCMModuleException(exceptionEnum));
		MvcResult result =mockMvc.perform(patch(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(exceptionEnum.getHttpStatusCode()))
				.andReturn();
		System.out.println("Error "+result.getResponse().getContentAsString());
		ErrorDto error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);
		assertNotNull(error);
		assertEquals(exceptionEnum.getCode(), error.getErrorCode());
	}
	
	@Test
	public void testUpdateUtilityStatusUnAuthorizedFlow() throws Exception {

		String requestPayload=objectMapper.writeValueAsString(utilityStatusRequestDto);
		when(utilityService.updateUtiltyStatus(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(true);
		
		mockMvc.perform(patch(api)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}
	@Test
	public void testGetAllUtiltiesSuccessWithEmptyFlow() throws Exception {
		responseModelArray = new UtilityResponseModel[0];
		when(utilityService.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(responseModelArray);
		
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();

		responseDtoArray = objectMapper.readValue(result.getResponse().getContentAsString(), UtilityResponseDto[].class);
		assertEquals(responseDtoArray.length,responseModelArray.length);
	}
	
	@Test
	public void testGetUtilityDetailsSuccessWithEmptyFlow() throws Exception {
		responseDetailsModelArray = new UtilityDetailsResponseModel[0];
		when(utilityService.getUtilityDetails(Mockito.anyLong(), Mockito.any(),Mockito.anyLong())).thenReturn(responseDetailsModelArray);
		api = api + "/4146935397";
		MvcResult result =mockMvc.perform(get(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();

		responseDetailsDtoArray = objectMapper.readValue(result.getResponse().getContentAsString(), UtilityDetailsResponseDto[].class);
		assertEquals(responseDetailsDtoArray.length,responseDetailsModelArray.length);
	}
	@Test
	public void testUpdateUtilityStatusSuccessWithNegativeFlow() throws Exception {
		String requestPayload=objectMapper.writeValueAsString(utilityStatusRequestDto);
		when(utilityService.updateUtiltyStatus(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenReturn(false);
		mockMvc.perform(patch(api)
				.header(HttpHeaders.AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestPayload).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
		
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
}
