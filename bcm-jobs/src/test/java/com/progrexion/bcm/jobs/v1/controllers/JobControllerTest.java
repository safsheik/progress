package com.progrexion.bcm.jobs.v1.controllers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.progrexion.bcm.common.utils.CommonUtils;
import com.progrexion.bcm.services.jobs.processors.JobProcessor;




@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

	

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	@Autowired
	private WebApplicationContext wac;
	@InjectMocks
	private JobController jobController;
	private String accessToken;
	private String api;
	private String apiForGetAndStop;
	@Mock
	private JobProcessor jobProcessor;
	 @Before
	 public void setUp() throws Exception {

	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
	        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
	                .addFilter(springSecurityFilterChain).build();
	        accessToken = getAccessToken();
	        api = "/v1/jobs/RENTTRACK_REPORTED_ORDERS?async=false&brand=CCOM";
	        apiForGetAndStop = "/v1/jobs?jobName=RENTTRACK_REPORTED_ORDERS&JobId=1&brand=CCOM";
	 }
	 
	 @Test
	 public void testPostjobSuccessFlow() throws Exception {

			when(jobProcessor.startJob(Mockito.any(), Mockito.any(), Mockito.anyBoolean())).thenReturn(new ArrayList<>());		
			MvcResult result =mockMvc.perform(post(api)
					.header(HttpHeaders.AUTHORIZATION, accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertNotNull(result.getResponse().getContentAsString());
	 }
	 @Test
	 public void testStatusOfjobSuccessFlow() throws Exception {

			when(jobProcessor.status(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());		
			MvcResult result =mockMvc.perform(get(apiForGetAndStop)
					.header(HttpHeaders.AUTHORIZATION, accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertNotNull(result.getResponse().getContentAsString());
	 }
	 @Test
	 public void testDeletejobSuccessFlow() throws Exception {

			when(jobProcessor.requestStop(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());		
			MvcResult result =mockMvc.perform(delete(apiForGetAndStop)
					.header(HttpHeaders.AUTHORIZATION, accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertNotNull(result.getResponse().getContentAsString());
	 }

	 @Test
	 public void testHealthCheckSuccessFlow() throws Exception {

		 MvcResult result = mockMvc.perform(get("/v1/jobs/healthcheck")
					.header(HttpHeaders.AUTHORIZATION, accessToken)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		 assertNotNull(result.getResponse().getContentAsString());
	 }

	
	private String getAccessToken() { 
	    return CommonUtils.getBasicAuthCred("saftest@test.com", "saf");
	}
}
