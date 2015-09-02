package com.aexp.gcs.pod.tools.templatemigration

import javax.security.auth.login.Configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

/**
 * Main Application class. This is the SpringBoot application starter.<br/>
 * When run from the command line use the following instruction:<br/>
 * 
 *  java -jar ./path_to/migration_tool-0.0.1-SNAPSHOT.jar --spring.config.location=/path_to/Application.yml<br/>
 *  
 * Application.yml is a YAML file that contains the following entries:<br/>
 * spring:<br/>
	   application:<br/>
	      sourceFolder: path_to/Templates_0522<br/>
	      baseOutputFolder: C:/development/projects/migration_tool_itest/Migration<br/>
	      templateTransactionInfo: C:/development/projects/migration_tool_itest/template-transaction-info<br/>
	      objectModelLocation: C:/development/examples/object-model<br/>
	      validationMessagesFile: C:/development/projects/migration_tool_itest/ValidationMessages.properties<br/>
	      beanConstraintsFolder: C:/development/projects/migration_tool_itest/business-rules/src/main/resources/META-INF<br/><br/>
 *
 * Where each of the entries corresponds to:<br/>
 * 	sourceFolder: this is the absolute path to where the output folder from the Template Translation Tool (Kapil's tool) is.<br/>
 *  baseOutputFolder: this is the folder where most of this tool output is going to be generated, like for example the business rules methods<br/>
 *  templateTransactionInfo: the path to the template-transaction-info module. Files might be copied to this location.<br/>
 *  objectModelLocation: the location of the object-model module. Migration Tool uses the classes SOURCE CODE in this module to perform validation<br/>
 *  validationMessagesFile: the location of the ValidationMessages.properties file. New error message place holders might be added here.<br/>
 *  beanConstraintsFolder: the location to where the validation XML files are located. Migration Tool might create and/or update xml files at this location.<br/>
 *
 * @author Victor Navarro
 *
 */
@Configuration
@SpringBootApplication
class Application
{
	@Autowired
	AppSettings appSettings
	
	@Autowired
	TemplateMigrator templateMigrator
	
	public void startExtraction()
	{
		println "Source Folder: ${this.appSettings.sourceFolder}"
		println "Output Folder:  ${this.appSettings.outputFolder}"
		templateMigrator.performMigration()
	}

	public static void main(String[] args)
	{
		SpringApplication springApp	= new SpringApplication(Application.class)
		
		//	Customize the application (springApp) before running it (hardly needed) ...
		
		ApplicationContext appCtx	=	springApp.run(args);
		
		appCtx.getBean(Application.class).startExtraction()
	}
}
