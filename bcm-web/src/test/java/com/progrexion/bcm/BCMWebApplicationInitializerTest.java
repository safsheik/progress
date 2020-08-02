package com.progrexion.bcm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BCMWebApplicationInitializerTest {
	
	@Test
	public void test_BCMWebApplicationInitializerInit() {
		String[] args = new String[0] ;
		BCMWebApplicationInitializer.main(args);
		assertNotNull(args);
	}
}