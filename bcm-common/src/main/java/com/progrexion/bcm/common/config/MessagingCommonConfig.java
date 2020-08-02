package com.progrexion.bcm.common.config;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.net.ssl.TrustManager;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ErrorHandler;

import com.progrexion.bcm.common.properties.MessagingConnectionProperties;
import com.progrexion.bcm.common.properties.MessagingDestinationProperties;
import com.progrexion.bcm.common.properties.MessagingProperties;
import com.progrexion.bcm.messagingconfig.enums.MessageConfigEnum;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@ComponentScan(basePackages = { "com.progrexion.bcm.services" })
@Slf4j
public class MessagingCommonConfig {
	
	private class MessagingContext {
		Destination destination;
		DefaultJmsListenerContainerFactory jmsListenerContainerFactory;
		MessagingCredentials messagingCredentials;
		ActiveMQSslConnectionFactory connectionFactory;
		JmsTemplate jmsTemplate;
	}

	private class MessagingCredentials {
		String brokerUrl;
		String username;
		String password;
		String clientId;
		boolean needToRetry;

		public MessagingCredentials(String brokerUrl, String username, String password, String clientId,
				boolean needToRetry) {
			this.brokerUrl = brokerUrl;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
			this.needToRetry = needToRetry;
		}

		@Override
		public String toString() {
			return "MessagingCredentials [brokerUrl=" + brokerUrl + ", username=" + username + ", password=" + "<>"
					+ ", clientId=" + clientId + ", needToRetry=" + needToRetry + "]";
		}		
		
	}

	private Map<String, MessagingContext> contexts = new HashMap<>();
	
	@Autowired
	private BeanFactory springContextBeanFactory;
	
	@Autowired
	private MessagingProperties messagingProps;
	
	private MappingJackson2MessageConverter getMessageConverter() {
		MappingJackson2MessageConverter mc = new MappingJackson2MessageConverter();
		mc.setTargetType(MessageType.TEXT);
		mc.setTypeIdPropertyName("messageType");
		return mc;
	}

	private MessagingCredentials createMessagingCredential(MessagingConnectionProperties connection, MessagingDestinationProperties messagingDestinationProperties) {
		final String brokerUrl = connection.getBrokerurl();
		final String username = connection.getUsername();
		final String password = connection.getPassword();
		final String clientId = messagingDestinationProperties.getClientId();
		final boolean needToRetry = messagingDestinationProperties.isRetryRequired();

		final MessagingCredentials messagingCredentials = new MessagingCredentials(brokerUrl, username, password,
				clientId, needToRetry);
		
		log.info("Created MessagingCredentials: {}", messagingCredentials);
		return messagingCredentials;
	}

	/**
	 * Destinations used in the application. If the queue or topic with the
	 * specified name doesn't exist, a queue or topic with the specified name is
	 * created (assuming the user has rights).
	 */
	private ActiveMQDestination createQueueDestination(MessagingDestinationProperties messagingDestinationProperties) {
		final ActiveMQDestination activeMQQueueDestination;
		final String property = messagingDestinationProperties.getDestinationName();
			log.info("Create Queue: {}", property);
			activeMQQueueDestination = new ActiveMQQueue(property);
		return activeMQQueueDestination;
	}
	
	private ActiveMQDestination createTopicDestination(MessagingDestinationProperties messagingDestinationProperties) {
		final ActiveMQDestination activeMQTopicDestination;
		final String property = messagingDestinationProperties.getDestinationName();
		log.info("Create Topic: {}", property);
		activeMQTopicDestination = new ActiveMQTopic(property);
		return activeMQTopicDestination;
	}

	private ActiveMQSslConnectionFactory createConnectionFactory(MessagingCredentials credential,
			MessagingDestinationProperties messagingDestinationProperties) {
		final ActiveMQSslConnectionFactory activeMQSslConnectionFactory;

		activeMQSslConnectionFactory = new ActiveMQSslConnectionFactory();
		activeMQSslConnectionFactory.setKeyAndTrustManagers(null, new TrustManager[] { new AllTrustManager() },
				new SecureRandom());
		activeMQSslConnectionFactory.setBrokerURL(credential.brokerUrl);
		activeMQSslConnectionFactory.setUserName(credential.username);
		activeMQSslConnectionFactory.setPassword(credential.password);
		if (credential.needToRetry) {
			activeMQSslConnectionFactory.setRedeliveryPolicy(generateRedeliveryPolicy(messagingDestinationProperties));
		}

		return activeMQSslConnectionFactory;
	}

	private DefaultJmsListenerContainerFactory createJmsListenerContainerFactory(MessagingCredentials credential,
			ActiveMQSslConnectionFactory connectionFactory, MessageConverter messageConverter,
			MessagingDestinationProperties messagingDestinationProperties) {
		final DefaultJmsListenerContainerFactory factory;

		factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setDestinationResolver(new BeanFactoryDestinationResolver(this.springContextBeanFactory));
		factory.setMessageConverter(messageConverter);

		// Create Client Ids, durable subscription and recovery Interval only for Topic
		if (messagingDestinationProperties.isQueue()) {
			factory.setClientId(credential.clientId);
			factory.setSubscriptionDurable(true);
		}
		factory.setRecoveryInterval(messagingProps.getJmsRecoveryInterval());
		factory.setErrorHandler(errorHandler());

		return factory;
	}

	private RedeliveryPolicy generateRedeliveryPolicy(MessagingDestinationProperties messagingDestinationProperties) {
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		// The initial redelivery delay in milliseconds.
		redeliveryPolicy.setInitialRedeliveryDelay(messagingDestinationProperties.getInitialRedeliveryDelay());
		redeliveryPolicy.setRedeliveryDelay(messagingDestinationProperties.getRedeliveryDelay());
		// Sets the maximum number of times a message will be redelivered before it is
		// considered a poisoned pill and returned to the broker so it can go to a Dead
		// Letter Queue if Configured
		redeliveryPolicy.setMaximumRedeliveries(messagingDestinationProperties.getMaximumRedeliveries());
		// to exponentially increase the timeout.
		redeliveryPolicy.setUseExponentialBackOff(messagingDestinationProperties.getExponentialBackoffRequired());
		// The back-off multiplier.
		redeliveryPolicy.setBackOffMultiplier(messagingDestinationProperties.getBackoffMultiplier());
		return redeliveryPolicy;
	}

	private JmsTemplate createJmsTemplate(ActiveMQSslConnectionFactory connectionFactory,
			MessageConverter messageConverter, Boolean isTopic) {
		final JmsTemplate jmsTemplate;

		jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory);
		jmsTemplate.setMessageConverter(messageConverter);
		jmsTemplate.setPubSubDomain(isTopic);

		return jmsTemplate;
	}

	private void initContexts() throws Exception {
		if(messagingProps == null || messagingProps.getModules() == null ) {
			String param = null;
			param = (messagingProps == null ? "messagingModules" : "modules");
			throw new Exception("Cannot find configuration: " + param);
		}
		
		log.info("Number of Messaging modules configured: {}", (messagingProps.getModules() == null ? "NONE" : messagingProps.getModules().size()));
		
		messagingProps.getModules().forEach((module, moduleProps) -> {
			MessagingConnectionProperties connection = moduleProps.getConnection();
			moduleProps.getDestinations().forEach((dest, destProps) -> {
				MappingJackson2MessageConverter messageConverter = getMessageConverter();

				MessagingContext ctx = new MessagingContext();
				ctx.messagingCredentials = createMessagingCredential(connection, destProps);
				boolean isTopic = destProps.isTopic();
				boolean isQueue = destProps.isQueue();
				if(isTopic)
					ctx.destination = createTopicDestination(destProps);
				if(isQueue)
					ctx.destination = createQueueDestination(destProps);
				ctx.connectionFactory = createConnectionFactory(ctx.messagingCredentials, destProps);
				ctx.jmsListenerContainerFactory = createJmsListenerContainerFactory(ctx.messagingCredentials,
						ctx.connectionFactory, messageConverter, destProps);
				ctx.jmsTemplate = createJmsTemplate(ctx.connectionFactory, messageConverter, destProps.isQueue());
				
				contexts.put(module + "_" + dest, ctx);
				
			} );
		});
	}

	
	private Map<String, MessagingContext> getContexts() throws Exception {
		if (contexts.isEmpty()) {
			log.info("MessagingConfig::getContexts::Initializing messaging contexts. Timestamp: [{}, {}]", ZonedDateTime.now(), ZoneId.systemDefault());
			initContexts();
		}
		return this.contexts;
	}
	// BCM ProcessingOrdersQueue

	
	/**
	 * Destinations used in the application. If the queue or topic with the
	 * specified name doesn't exist, a queue or topic with the specified name is
	 * created (assuming the user has rights).
	 * @throws Exception 
	 */

	// PBS ProvisioningChangeTopic
	@Bean
	public Destination pbsProvisioningChangeTopicDestination() throws Exception {
		return getContexts().get(MessageConfigEnum.PBS_PROVISIONING_CHANGE.getKey()).destination;
	}

	@Bean
	public DefaultJmsListenerContainerFactory pbsProvisioningChangeTopicJmsListenerContainerFactory() throws Exception {
		return getContexts().get(MessageConfigEnum.PBS_PROVISIONING_CHANGE.getKey()).jmsListenerContainerFactory;
	}

	@Bean
	public JmsTemplate pbsProvisioningChangeTopicJmsTemplate() throws Exception {
		return getContexts().get(MessageConfigEnum.PBS_PROVISIONING_CHANGE.getKey()).jmsTemplate;
	}
	
	//BCM Processing Order Queue
	@Bean
	public Destination bcmJobsProcessingOrdersQueueDestination() throws Exception {
		return getContexts().get(MessageConfigEnum.BCM_JOBS_PROCESSING_ORDER.getKey()).destination;
	}

	@Bean
	public DefaultJmsListenerContainerFactory bcmJobsProcessingOrdersQueueJmsContainerFactory() throws Exception {
		return getContexts().get(MessageConfigEnum.BCM_JOBS_PROCESSING_ORDER.getKey()).jmsListenerContainerFactory;
	}

	@Bean
	public JmsTemplate bcmJobsProcessingOrdersQueueJmsTemplate() throws Exception {
		return getContexts().get(MessageConfigEnum.BCM_JOBS_PROCESSING_ORDER.getKey()).jmsTemplate;
	}
	
	// Other Beans	
	@Bean
	public ErrorHandler errorHandler() {
		return e -> MessagingCommonConfig.log.error("MessagingErrorHandler::Caught Error::[{}, {}]", e.getMessage(), e);
	}


}
