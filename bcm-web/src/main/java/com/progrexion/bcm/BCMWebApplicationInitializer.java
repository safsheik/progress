package com.progrexion.bcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.progrexion.bcm.common.properties.BCMConfigProperties;
import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.properties.MessagingProperties;
import com.progrexion.bcm.common.properties.RentTrackAPIConfigProperties;
import com.progrexion.bcm.common.properties.RentTrackAPIOAuthConfigProperties;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;

@SpringBootApplication(scanBasePackages = "com.progrexion.bcm")
@EnableConfigurationProperties({ RentTrackConfigProperties.class,
	RentTrackAPIConfigProperties.class, 
	RentTrackAPIOAuthConfigProperties.class,
	CustomerMasterConfigProperties.class, BCMConfigProperties.class, MessagingProperties.class})
public class BCMWebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BCMWebApplicationInitializer.class, args);
	}

}
