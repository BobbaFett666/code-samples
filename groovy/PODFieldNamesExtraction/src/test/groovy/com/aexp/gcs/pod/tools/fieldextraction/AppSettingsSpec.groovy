package com.aexp.gcs.pod.tools.fieldextraction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class AppSettingsSpec extends Specification
{
	@Autowired
	AppSettings appSettings
	
	
	def "Check Application Settings" ()
	{
		expect:
			println appSettings?.sourceFolder
			println appSettings?.outputFile
			appSettings
			appSettings.sourceFolder
			appSettings.outputFile
	}
}
