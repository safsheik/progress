package com.progrexion.bcm.services.jobs.publishers;

import static org.junit.Assert.assertNotNull;

import javax.jms.Destination;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;
import com.progrexion.bcm.services.jobs.publisher.CustomerProcessOrderMessagePublisher;

@RunWith(MockitoJUnitRunner.class)
public class CustomerProcessOrderMessagePublisherTest {

	@InjectMocks
	private CustomerProcessOrderMessagePublisher publisher;
	@Mock
	private JmsTemplate jmsTemplate;
	@Mock
	private Destination destination;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}
	
	@Test
	public void test_publish() {
		publisher.publish(new ProcessOrderMessageDTO());
		assertNotNull(publisher);
	}

}
