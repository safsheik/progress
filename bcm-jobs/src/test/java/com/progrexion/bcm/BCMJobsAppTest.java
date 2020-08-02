package com.progrexion.bcm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BCMJobsAppTest {
	
	@Test
	public void test_BCMJobsAppInit() {
		String[] args = new String[0] ;
		BCMJobsApp.main(args);
		assertNotNull(args);
	}
}
