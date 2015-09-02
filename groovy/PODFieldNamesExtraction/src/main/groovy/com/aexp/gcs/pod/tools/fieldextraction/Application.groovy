package com.aexp.gcs.pod.tools.fieldextraction

import javax.security.auth.login.Configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication
class Application
{
	@Autowired
	AppSettings appSettings
	
	@Autowired
	PodFieldsExtractor podFieldsExtractor
	
	public void startExtraction()
	{
		println "Source Folder: ${this.appSettings.sourceFolder}"
		println "Output File:  ${this.appSettings.outputFile}"
		podFieldsExtractor.getAllPODFields()
	}

	public static void main(String[] args)
	{
		SpringApplication springApp	= new SpringApplication(Application.class)
		
		//	Customize the application (springApp) before running it (hardly needed) ...
		
		ApplicationContext appCtx	=	springApp.run(args);
		
		appCtx.getBean(Application.class).startExtraction()
	}
}
