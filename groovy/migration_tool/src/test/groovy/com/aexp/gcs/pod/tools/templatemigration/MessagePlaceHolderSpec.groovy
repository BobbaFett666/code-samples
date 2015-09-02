package com.aexp.gcs.pod.tools.templatemigration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class MessagePlaceHolderSpec extends Specification
{
	
	@Autowired
	MessagePlaceHolder aPlaceHolder
	
	@Autowired
	AppSettings appSettings

	//	This value should come from Application Configuration as is needed to verify existance of Class Name passed in
	//String objectModelLocation = 'C:/Dev/svnRepo/object-model'
	
	//	Here I am reading a ValidationMessages out of the test classpath, in real implementation this should be an entry in Application Configuration
	//String validationMessagesFile = 'C:/Dev/POA_Migration/migration_tool/src/test/resources/ValidationMessages.properties'
	//String validationMessagesFile =	Thread.currentThread().getContextClassLoader().getResource("ValidationMessages.properties").getFile()
	
	@Unroll()
	def "Verify that a PlaceHolder string is returned based on Class Name, Attribute Name and Validator Name"()
	{
		given:
			String aResult = null
		when:
			aResult = makePlaceholder("com.aexp.gcs.schema.sendcorr.${testClassName}", testAttributeName, testValidatorName)
		then:
			aResult == testResult
		where:
				testClassName			|	testAttributeName	|	testValidatorName											|	testResult
			'DeliveryInformationType'	|	'destinationCode'	|	'org.hibernate.validator.constraints.NotEmpty'				|	'com.aexp.gcs.schema.sendcorr.DeliveryInformationType.destinationCode.NotEmpty.message'
			'CustomerPIIType'			|	'relationShipID'	|	'NotEmpty'													|	'com.aexp.gcs.schema.sendcorr.CustomerPIIType.relationShipID.NotEmpty.message'
			'FeederDetailsType'			|	'feederSystemID'	|	'com.aexp.gcs.poa.custom.constraint.ValidateServiceDetails'	|	'com.aexp.gcs.schema.sendcorr.FeederDetailsType.feederSystemID.ValidateServiceDetails.message'
			'DummyDetailsType'			|	'feederSystemID'	|	'NotEmpty'													|	null
	}
	
	@Unroll
	def "Checking for the existance of a SendConn class file #testClassName : #testResult"()
	{
		expect:
			checkClassExists(testClassName)	== testResult
		where:
			testClassName											|		testResult
			'com.aexp.gcs.schema.sendcorr.DeliveryInformationType'	|		true
			'com.aexp.gcs.schema.sendcorr.CustomerPIIType'			|		true
			'com.aexp.gcs.schema.sendcorr.DummyStuffType'			|		false
	}
	
	private String getDefaultValidationMessages()
	{
"""
javax.validation.constraints.AssertFalse.message	=	must be false
javax.validation.constraints.AssertTrue.message	=	must be true
javax.validation.constraints.Digits.message	=	numeric value out of bounds (<{integer} digits>.<{fraction} digits> expected)
javax.validation.constraints.Future.message	=	must be in the future"""
	}
	
	@Unroll()
	def "Must check if a given Placehorder (#testPlaceholder) string already exists in ValidationMessages.properties"()
	{
		given:
			File aPlaceholderFile = new File("${appSettings.validationMessagesFile}")
			
			aPlaceholderFile.delete()
			aPlaceholderFile.withWriter{ it << getDefaultValidationMessages() }
		expect:
			aPlaceHolder.placeholderExists(testPlaceholder) == testResult
		where:
			testPlaceholder												|	testResult
			'javax.validation.constraints.AssertTrue.message'			|	true
			'javax.validation.constraints.Future.message'				|	true
			'org.hibernate.validator.constraints.NotEmpty'				|	false
			'com.aexp.gcs.poa.custom.constraint.ValidateServiceDetails'	|	false
	}

	@Unroll()
	def "Must add a given Placeholder (#testPlaceholder) to the ValidationMessages.properties only if it is not there already"()
	{
		given:
			File aPlaceholderFile = new File("${appSettings.validationMessagesFile}")
			
			aPlaceholderFile.delete()
			aPlaceholderFile.withWriter{ it << getDefaultValidationMessages() }
		
		when:
			aPlaceHolder.addPlaceholder(testPlaceholder)
		
		then:
			(old(new File("${appSettings.validationMessagesFile}").text.length()) < new File("${appSettings.validationMessagesFile}").text.length()) == testResult
		
		where:
			testPlaceholder												|	testResult
			'javax.validation.constraints.AssertTrue.message'			|	false
			'javax.validation.constraints.Future.message'				|	false
			'org.hibernate.validator.constraints.NotEmpty'				|	true
			'com.aexp.gcs.poa.custom.constraint.ValidateServiceDetails'	|	true
	}
	
	
	def "Checking test ValidationMessages.properties file path"()
	{
		expect:
			println "${appSettings.validationMessagesFile}"
			new File("${appSettings.validationMessagesFile}").exists()
	}
	
	public String makePlaceholder(pClassName, pAttrName, pValName)
	{
		String aPlaceholder = null
		
		if (checkClassExists(pClassName)) {
			String aValName = pValName.lastIndexOf('.') >= 0 ? pValName.substring(pValName.lastIndexOf('.') + 1) : pValName
			aPlaceholder = "${pClassName}.${pAttrName}.${aValName}.message"
		}
		
		aPlaceholder
	}
	
	public boolean checkClassExists(pClassName)
	{
		new File("${appSettings.objectModelLocation}/src/main/java/${pClassName.replaceAll('\\.', '/')}.java").exists()
	}
}
