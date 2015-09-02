package com.aexp.gcs.pod.tools.templatemigration

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification
import spock.lang.Unroll

/**
 * This is intended to parse the TEMPLATE-NAME.TXT file into a Map with the following structure:

static: [
	PoaVariableName: {
		className: SendCorr4ClassName
		template: TemplateName
		feeder: FeederName
		validators: [
			{
				className: ValidatorClass
				elements: [
					{ << Variable List of validation elements >>  }
				]
			}
		]
	}
]

where:
PoaVariableName: This is the new Attribute name of a class that lives in com.aexp.gcs.schema.sendcorr package
PoaVariableName.className: This is the actual class in com.aexp.gcs.schema.sendcorr that 'PoaVariable' belongs to (not in TXT file)


 * @author vnavarr
 *
 */

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class VariablesInformationParserSpec extends Specification
{
	
	@Autowired
	AppSettings appSettings

	
	def "Parse TEMPLATE-NAME.TXT file and extract static variables information into a Map"()
	{
		given:
			appSettings.sourceFolder = 'C:/development/projects/migration_tool/src/test/resources/Templates_0515'
			String aFeederName = 'ALE'
			String aTemplateName = 'ALEENALEOBC0002'
		
		when:
			Map aVarMap = parseTemplate(aFeederName, aTemplateName)
			
			//	Only takes one line to convert to JSON! :)
			println JsonOutput.prettyPrint(JsonOutput.toJson(aVarMap))
		then:
			aVarMap.static?.size() > 0
			aVarMap.static.each{ aVarName, aVarInfo ->
				aVarInfo.template == aTemplateName
				aVarInfo.feeder == aFeederName
				['ReferenceNumber', 'RelationShipTypeCode', 'RequestDate'].contains(aVarInfo.name)
				aVarInfo.className.startsWith('com.aexp.gcs.schema.sendcorr')
				aVarInfo.validators
			}
	}
	
	@Unroll()
	def "For a Variable Path #testVarPath the corresponding sendcorr class name is #testClassName"()
	{
		when:
			String aClassName = getSendCorrClassName(testVarPath)
		then:
			aClassName == testClassName
		where:
									testVarPath																	|			testClassName
			'Envelope.Body.Request.CommunicationInformation.TemplateID'											|	'com.aexp.gcs.schema.sendcorr.CommunicationInformationType'
			'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.ElectronicAddress.EmailID'|	'com.aexp.gcs.schema.sendcorr.ElectronicAddressType'
			'Envelope.Body.Request.CustomerPII.ReferenceNumber'													|	'com.aexp.gcs.schema.sendcorr.CustomerPIIType'
	}
	
	
	public Map parseTemplate(pFeederName, pTemplateName)
	{
		Map 	aResult			=	[:]			
		File	aTxtFile		=	new File("${appSettings.sourceFolder}/${pFeederName}/POA/${pTemplateName}.txt")
		def		aStaticPattern	=	/(?m)^(.+)?:static--(.+)?--(.+)$/
		def		aStaticMatcher	=	null
		
		if (!aTxtFile.exists()) {
			throw new FileNotFoundException("Template output file ${pTemplateName}.txt for feeder ${pFeederName} not found")
		}
		
		aResult.static = [:]
		aStaticMatcher = aTxtFile.text =~ aStaticPattern
		aStaticMatcher.each { aMatch ->
			String aVarName = aMatch[1]
			
			if (!aResult.static[aVarName]) {
				aResult.static[aVarName] = [
					className: getSendCorrClassName(aMatch[2]),
					template: pTemplateName,
					feeder: pFeederName,
					validations: []
				]
			}
			//	ADD VALIDATORS
			
//			println "${aMatch[1]}\t${aMatch[2]}\t${aMatch[3]}"
		}
		
		aResult
	}
	
	private String getSendCorrClassName(String pVariablePath)
	{
		String noVarName = pVariablePath.substring(0, pVariablePath.lastIndexOf('.'))
		"com.aexp.gcs.schema.sendcorr.${noVarName.substring(noVarName.lastIndexOf('.')+1)}Type"
	}

}
