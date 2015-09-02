package com.aexp.gcs.pod.tools.fieldextraction

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class PodFieldExtractorSpec extends Specification
{
	@Autowired
	PodFieldsExtractor anExtractor
	
	def "Output the result as Json"()
	{
		given:
			def aRes1 = Thread.currentThread().getContextClassLoader().getResource('xml/AGNUNUMS0005005.xml')
			def aRes2 = Thread.currentThread().getContextClassLoader().getResource('xml/CANENWEMUPD0011.xml')
			List aTemplateRecords = [	[	templateId: 'AGNUNUMS0005005',
											templatePath: aRes1.getFile(),
											feeder:'UMS'],
										[	templateId: 'CANENWEMUPD0011',
											templatePath: aRes2.getFile(),
											feeder:'WEM']
									]
			def json
		when:
			Map aResult = anExtractor.processAllTemplates(aTemplateRecords)
			json = JsonOutput.toJson(aResult)
			println JsonOutput.prettyPrint(json)
		then:
			json
	}

	def "Process all fields Spec"()
	{
		given:
			def aRes1 = Thread.currentThread().getContextClassLoader().getResource('xml/AGNUNUMS0005005.xml')
			def aRes2 = Thread.currentThread().getContextClassLoader().getResource('xml/CANENWEMUPD0011.xml')
			List aTemplateRecords = [	[	templateId: 'AGNUNUMS0005005',
											templatePath: aRes1.getFile(),
											feeder:'UMS'],
										[	templateId: 'CANENWEMUPD0011',
											templatePath: aRes2.getFile(),
											feeder:'WEM']
									]
			Map aResult
		when:
			aResult = anExtractor.processAllTemplates(aTemplateRecords)
		then:
			aResult.size()
			println aResult
			aResult.CommunLangDesc.size() == 2
	}	
	
	def "Retrieve all field information for a given template file record"()
	{
		given:
			def aResource = Thread.currentThread().getContextClassLoader().getResource('xml/CANENWEMUPD0011.xml')
			Map aTemplateRecord = [templateId: 'CANENWEMUPD0011', templatePath: aResource.getFile(), feeder: 'WEM']
			List aResult	=	null
		when:
			aResult = anExtractor.getTemplateFields(aTemplateRecord)
		then:
			aResult
			aResult.size() > 1
			println aResult
	}

	def "Retrieve Structure with Template and Feeder information"()
	{
		given:
			List aResult = null
			
		when:
			aResult = anExtractor.retrievePODTemplateFiles()
		then:
			aResult.size()
//			aResult.containsKey('ALE')
//			aResult.containsKey('WCC')
//			aResult.ALE.size()
//			println aResult.ALE
//			aResult.ALE[0].templateId
//			aResult.ALE[0].templatePath
//			aResult.ALE[0].feeder == 'ALE'
	}
	
	
}
