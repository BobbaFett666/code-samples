package com.aexp.gcs.pod.tools.fieldextraction

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppSettings
{
	@Value('${spring.application.sourceFolder}')
	String sourceFolder
	
	@Value('${spring.application.outputFile}')
	String outputFile

}
