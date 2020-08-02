package com.progrexion.bcm.messagingconfig.enums;

public enum PropertiesEnum {

	// Persistence Configuration -Rewards DB:

	BCM_DB_DATABASE_PLATFORM("spring.datasource.database-platform"),
	BCM_DB_DRIVER_CLASS("spring.datasource.driver-class-name"),
	BCM_DB_URL("spring.datasource.url"), BCM_DB_USERNAME("spring.datasource.username"),
	BCM_DB_PASSWORD("spring.datasource.password"),
	BCM_DB_HBM2DDL("spring.datasource.hibernate.ddl-auto"),
	BCM_DB_DATABASE_LOB_NON_CONTEXT_CREATION("spring.datasource.properties.hibernate.jdbc.lob.non_contextual_creation"),
//	// Spring Security DataSource Configuration:
//	SPRING_SECURITY_DB_URL("spring.datasource.security.url"),
//	SPRING_SECURITY_DB_USER_NAME("spring.datasource.security.username"),
//	SPRING_SECURITY_DB_PASSWORD("spring.datasource.security.password"),
//	SPRING_SECURITY_DB_DRIVER_CLASS("spring.datasource.security.driver-class-name"),

	BCM_API_RETRY("bcm.api.retry"),

	

	FAILED_DOWNSTREAM_MESSAGE_RETRY_COUNT("faileddownstream.message.retry.count"),

	// Retry
	ACTIVEMQ_REWARDS_PUBLISHER_RETRYBACKOFF("activemq.rewards.publisher.retrybackoff"),
	ACTIVEMQ_REWARDS_PUBLISHER_MAXRETRYATTEMPT("activemq.rewards.publisher.maxretryattempts"),

	;

	

	private String configKey;

	PropertiesEnum(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigKey() {
		return configKey;
	}

}
