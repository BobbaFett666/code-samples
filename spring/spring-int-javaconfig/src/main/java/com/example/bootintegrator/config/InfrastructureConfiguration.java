package com.example.bootintegrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@IntegrationComponentScan("com.example.bootintegrator")
class InfrastructureConfiguration
{
	
	@Bean
	public MessageChannel ordersChannel()
	{
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel orderItemsChannel()
	{
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel bookItemsChannel()
	{
		return new PublishSubscribeChannel();
	}
	
	@Bean
	public MessageChannel musicItemsChannel()
	{
		return new PublishSubscribeChannel();
	}
	
	@Bean
	public MessageChannel softwareItemsChannel()
	{
		return new PublishSubscribeChannel();
	}
	
	@Bean
	public MessageChannel processedItemsChannel()
	{
		return new DirectChannel();
	}
	
	@Bean 
	public MessageChannel deliveriesChannel()
	{
		return new DirectChannel();
	}
	
}