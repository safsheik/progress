package com.progrexion.bcm.messaging.listeners;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.FeatureDTO;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.ProvisioningAction;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.ProvisioningChangeTopicMessageDTO;
import com.progrexion.bcm.services.v1.MessagingService;

@RunWith(MockitoJUnitRunner.class)
public class PBSListenersTest {

	@InjectMocks
	private PBSListeners pbsListeners;

	@Mock
	private MessagingService messagingService;

	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private ModelMapper modelMapper;
	
	private ProvisioningChangeTopicMessageDTO validMessageDto;
	private ProvisioningChangeTopicMessageDTO invalidMessageDto;
	private List<String> values = new ArrayList<>();


	
	
	@Before
	public void setUp() {
		validMessageDto = new ProvisioningChangeTopicMessageDTO();
		validMessageDto.setUcid(101L);
		validMessageDto.setBrand("CCOM");
		FeatureDTO feature = new FeatureDTO();
		feature.setFeatureCd("Rent_Track");
		feature.setAction(ProvisioningAction.REMOVE);
		List<FeatureDTO> features = new ArrayList<>();
		features.add(feature);
		validMessageDto.setFeatureDTOList(features);
		invalidMessageDto = new ProvisioningChangeTopicMessageDTO();
		invalidMessageDto.setUcid(101L);
		invalidMessageDto.setBrand("SomeBrandName");
		FeatureDTO invalidFeature = new FeatureDTO();
		invalidFeature.setFeatureCd("SomeFeatureCode");
		List<FeatureDTO> invalidFeatures = new ArrayList<>();
		invalidMessageDto.setFeatureDTOList(invalidFeatures);
		
		values.add("CCOM");
		ReflectionTestUtils.setField(pbsListeners, 
				"cancelSubscriptionValidBrands",values);
		values = new ArrayList<>();
		values.add("Rent_Track");
		
		ReflectionTestUtils.setField(pbsListeners, 
				"cancelSubscriptionValidFeatureCodes",values);
	}

	@Test
	public void test_onProvisioningChangeMessage()
			throws Exception {
		String provisioningChangeMessageString = "{\"ucid\":10000,"
				+ "\"brand\":\"CCOM\","
				+ "\"timeStamp\":\"2019-10-17T03:04:44Z\","
				+ "\"featureDTOList\":[{"
					+ "\"featureCd\":\"Rent_Track\","
					+ "\"featureId\":\"1001\","
					+ "\"featureName\":\"Rent_Track\","
					+ "\"newFeatureCd\":null,"
					+ "\"newFeatureId\":null,"
					+ "\"newFeatureName\":null,"
					+ "\"action\":\"REMOVE\"}],"
				+ "\"subscriptionId\":123465,"
				+ "\"newSubscriptionId\":123466,"
				+ "\"externalId\":null,"
				+ "\"mergedUcids\":[],"
				+ "\"channel\":\"Online Signup\"}";
		
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(provisioningChangeMessageString);

		TextMessage message = new ActiveMQTextMessage();
		message.setText(provisioningChangeMessageString);
		pbsListeners.onProvisioningChangeMessage(message, null);
		assertNotNull(pbsListeners);

	}
	
	@Test
	public void test_onProvisioningChangeMessage1()
			throws Exception {
		String provisioningChangeMessageString = "{\"ucid\":10000,"
				+ "\"brand\":\"CCOM\","
				+ "\"timeStamp\":\"2019-10-17T03:04:44Z\","
				+ "\"featureDTOList\":[{"
					+ "\"featureCd\":\"Rent_Track\","
					+ "\"featureId\":\"1001\","
					+ "\"featureName\":\"Rent_Track\","
					+ "\"newFeatureCd\":null,"
					+ "\"newFeatureId\":null,"
					+ "\"newFeatureName\":null,"
					+ "\"action\":\"REMOVE\"}],"
				+ "\"subscriptionId\":123465,"
				+ "\"newSubscriptionId\":123466,"
				+ "\"externalId\":null,"
				+ "\"mergedUcids\":[],"
				+ "\"channel\":\"Online Signup\"}";
		
		
		FeatureDTO feature = new FeatureDTO();
		feature.setFeatureCd("Rent_Track");
		feature.setAction(ProvisioningAction.UPDATE);
		List<FeatureDTO> features = new ArrayList<>();
		features.add(feature);
		validMessageDto.setFeatureDTOList(features);
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(provisioningChangeMessageString);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(provisioningChangeMessageString);
		pbsListeners.onProvisioningChangeMessage(message, null);
		assertNotNull(pbsListeners);

	}
	
	
	@Test
	public void test_onProvisioningChangeMessage2()
			throws Exception {
		String provisioningChangeMessageString = "{\"ucid\":10000,"
				+ "\"brand\":\"CCOM\","
				+ "\"timeStamp\":\"2019-10-17T03:04:44Z\","
				+ "\"featureDTOList\":[{"
					+ "\"featureCd\":\"Rent_Track\","
					+ "\"featureId\":\"1001\","
					+ "\"featureName\":\"Rent_Track\","
					+ "\"newFeatureCd\":null,"
					+ "\"newFeatureId\":null,"
					+ "\"newFeatureName\":null,"
					+ "\"action\":\"REMOVE\"}],"
				+ "\"subscriptionId\":123465,"
				+ "\"newSubscriptionId\":123466,"
				+ "\"externalId\":null,"
				+ "\"mergedUcids\":[],"
				+ "\"channel\":\"Online Signup\"}";
		
		
		FeatureDTO feature = new FeatureDTO();
		feature.setFeatureCd("Rent_Track");
		feature.setAction(ProvisioningAction.UPDATE);
		List<FeatureDTO> features = new ArrayList<>();
		validMessageDto.setFeatureDTOList(features);
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(provisioningChangeMessageString);
		TextMessage message = new ActiveMQTextMessage();
		message.setText(provisioningChangeMessageString);
		pbsListeners.onProvisioningChangeMessage(message, null);
		assertNotNull(pbsListeners);

	}
	
	@Test
	public void test_onProvisioningChangeMessageWithNullpointerException()
			throws Exception {
		String provisioningChangeMessageString = "{\"ucid\":10000,"
				+ "\"brand\":\"CCOM\","
				+ "\"timeStamp\":\"2019-10-17T03:04:44Z\","
				+ "\"featureDTOList\":[{"
					+ "\"featureCd\":\"Rent_Track\","
					+ "\"featureId\":\"1001\","
					+ "\"featureName\":\"Rent_Track\","
					+ "\"newFeatureCd\":null,"
					+ "\"newFeatureId\":null,"
					+ "\"newFeatureName\":null,"
					+ "\"action\":\"REMOVE\"}],"
				+ "\"subscriptionId\":123465,"
				+ "\"newSubscriptionId\":123466,"
				+ "\"externalId\":null,"
				+ "\"mergedUcids\":[],"
				+ "\"channel\":\"Online Signup\"}";
		
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(provisioningChangeMessageString);

		TextMessage message = new ActiveMQTextMessage();
		message.setText(provisioningChangeMessageString);
		when(messagingService.processCancelSubscriptionMessage(Mockito.any(),Mockito.any()))
		.thenThrow(new NullPointerException());
		pbsListeners.onProvisioningChangeMessage(message, null);
		assertNotNull(pbsListeners);

	}
	
	@Test
	public void test_onProvisioningChangeMessageWithBCMException()
			throws Exception {

		String provisioningChangeMessageString = "{\"ucid\":10000,"
				+ "\"brand\":\"CCOM\","
				+ "\"timeStamp\":\"2019-10-17T03:04:44Z\","
				+ "\"featureDTOList\":[{"
					+ "\"featureCd\":\"Rent_Track\","
					+ "\"featureId\":\"1001\","
					+ "\"featureName\":\"Rent_Track\","
					+ "\"newFeatureCd\":null,"
					+ "\"newFeatureId\":null,"
					+ "\"newFeatureName\":null,"
					+ "\"action\":\"REMOVE\"}],"
				+ "\"subscriptionId\":123465,"
				+ "\"newSubscriptionId\":123466,"
				+ "\"externalId\":null,"
				+ "\"mergedUcids\":[],"
				+ "\"channel\":\"Online Signup\"}";
		
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(validMessageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(provisioningChangeMessageString);

		TextMessage message = new ActiveMQTextMessage();
		message.setText(provisioningChangeMessageString);
		when(messagingService.processCancelSubscriptionMessage(Mockito.any(),Mockito.any()))
		.thenThrow(new BCMModuleException(BCMModuleExceptionEnum.BCM_API_INVALID_INPUT));
		pbsListeners.onProvisioningChangeMessage(message, null);
		assertNotNull(pbsListeners);

	}
	
	@Test(expected = RuntimeException.class)
	public void test_onProvisioningChangeMessageWithException() throws Exception {
		pbsListeners.onProvisioningChangeMessage(null, null);
		assertNotNull(pbsListeners);
	}

}
