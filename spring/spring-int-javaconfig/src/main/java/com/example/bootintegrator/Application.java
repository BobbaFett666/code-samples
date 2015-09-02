package com.example.bootintegrator;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
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