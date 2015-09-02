package com.example.bootintegrator;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@IntegrationComponentScan("com.example.bootintegrator")
@EnableIntegration
@ImportResource( "classpath:integration.xml" )
class Application
{
	public static void main(String[] args)
	{
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("Beans provided by Spring Boot:");
		String [] beanNames  = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String aBeanName : beanNames) {
			System.out.println(aBeanName);
		}
		
	}
}