package com.progrexion.bcm.messaging.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.ProcessOrderMessageRequestModel;
import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;
import com.progrexion.bcm.services.jobs.processors.JobService;

@RunWith(MockitoJUnitRunner.class)
public class BCMJobsListenersTest {

	@InjectMocks
	private BCMJobsListeners bcmJobsListeners;

	@Mock
	private JobService jobService;
	@Mock
	private ModelMapper modelmapper;
	@Mock
	private ObjectMapper mockObjectMapper;
	private ObjectMapper objectMapper = new ObjectMapper();

	private ProcessOrderMessageDTO validMessageDto;
	
	@Before
	public void setUp() {
		validMessageDto = new ProcessOrderMessageDTO();
		validMessageDto.setCustomerDataId(79l);
		validMessageDto.setCustomerOrderProcessId(1l);
		validMessageDto.setJobId(1l);
	}

	@Test
	public void test_onProcessOrderMessage()
			throws Exception {
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		when(modelmapper.map(Mockito.any(),Mockito.any())).thenReturn(new ProcessOrderMessageRequestModel());
		doNothing().when(jobService).processOrderReport(Mockito.any());
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage1()
			throws Exception {
		validMessageDto.setCustomerDataId(null);
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage2()
			throws Exception {
		validMessageDto.setJobId(null);
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage3()
			throws Exception {
		validMessageDto.setCustomerOrderProcessId(null);
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage4()
			throws Exception {
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		when(modelmapper.map(Mockito.any(),Mockito.any())).thenThrow(new NullPointerException());
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage5()
			throws Exception {
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		when(modelmapper.map(Mockito.any(),Mockito.any()))
		.thenThrow(new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION));
		bcmJobsListeners.onProcessOrderMessage(message, null);
		assertNotNull(bcmJobsListeners);

	}
	
	@Test
	public void test_onProcessOrderMessage6()
			throws Exception {
		try {
		String messageString=objectMapper.writeValueAsString(validMessageDto);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(messageString);		
		when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class)))
		.thenThrow(new BCMModuleException(BCMModuleExceptionEnum.UNABLE_TO_PARSE_MESSAGE_BCM_MESSAGING_EXCEPTION));
		bcmJobsListeners.onProcessOrderMessage(message, null);
		}
		catch(BCMModuleException ex) {
			assertEquals(ex.getErrorCode(),BCMModuleExceptionEnum.UNABLE_TO_PARSE_MESSAGE_BCM_MESSAGING_EXCEPTION.getCode());
		}
				

	}
	
	@Test(expected = RuntimeException.class)
	public void test_onProcessOrderMessageWithException() throws Exception {
		bcmJobsListeners.onProcessOrderMessage(null, null);
		assertNotNull(bcmJobsListeners);
	}

}
