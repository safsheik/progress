package com.progrexion.bcm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BCMMessaginAppTest {
	
	@Test
	public void test_BCMMessagingAppInit() {
		String[] args = new String[0] ;
		BCMMessagingApp.main(args);
		assertNotNull(args);
	}
}