package com.aexp.gcs.pod.tools.templatemigration

import groovy.json.JsonSlurper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class BusinessRulesImplementationSpec extends Specification
{

	@Autowired
	AppSettings appSettings
	
	@Shared
	String TEMPLATE_ID = "AGNEUBOL0030001"
	
	@Shared
	String templateFilePath
	
	def setupSpec()
	{
		templateFilePath = Thread.currentThread().getContextClassLoader().getResource("${TEMPLATE_ID}.java.txt").getFile()
	}

	
	def "Print generated SPEL from Variable Rule"()
	{
		given:
			def json = new JsonSlurper().parseText(this.getJsonTest01())
			def varJson = json.variables[0]
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
		expect:
			println dynVarValidator.generateSpel(varJson.mapping, varJson."business-rules"[0].rule)
	}
	

	@Unroll	
	def "Generate SPEL expression"()
	{
		given:
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
		when:
			String spelExp = dynVarValidator.generateSpel(testMapping, testRule)
		then:
			spelExp == testSpelExp
		where:
			testMapping	<<	['Envelope.Body.Request.CommunicationInformation.RequestDate',
							 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.NameDetails.FullName']
			testRule	<<	['com.americanexpress.commutil.formatter.EffDateFormatter',
							 'com.americanexpress.commutil.formatter.CustNameFormatter']
			testSpelExp	<<	['body?.request?.communicationInformation?.requestDate = T(com.americanexpress.commutil.formatter.EffDateFormatter).execute(body?.request?.communicationInformation?.requestDate)',
							 'body?.request?.customerPII?.recipientContactDetails?.contactDetails?.nameDetails?.fullName = T(com.americanexpress.commutil.formatter.CustNameFormatter).execute(body?.request?.customerPII?.recipientContactDetails?.contactDetails?.nameDetails?.fullName)']
	}
	
	
	def "Add SpelExpressionParse and ExpressionParser to imports"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
			
		when:
			dynVarValidator.addImports()
			
		then:
			old(dynVarValidator.getTemplateSrc().indexOf('import org.springframework.expression.ExpressionParser;')) == -1
			old(dynVarValidator.getTemplateSrc().indexOf('import org.springframework.expression.spel.standard.SpelExpressionParser;')) == -1
			dynVarValidator.getTemplateSrc().indexOf('import org.springframework.expression.ExpressionParser;') > 0
			dynVarValidator.getTemplateSrc().indexOf('import org.springframework.expression.spel.standard.SpelExpressionParser;') > 0
	}
	
	
	def "Make transformation class concrete if abstract and provide execute method implementation"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
		when:
			dynVarValidator.convertToConcreteClass()
		then:
			String aSourceCodeText = dynVarValidator.getTemplateSrc()
			
			aSourceCodeText.indexOf('abstract class') == -1
			aSourceCodeText.indexOf("class ${TEMPLATE_ID}") > 0
			aSourceCodeText.indexOf("public void execute(EnvelopeType Envelope)") > 0
	}

		
	def "Parse out the execute method source"() {
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
		when:
			dynVarValidator.convertToConcreteClass()
			String executeMethordSrc = dynVarValidator.getExecuteMethodSource()		
		then:
			executeMethordSrc.matches(/(?s)^.*?public\s+void\s+execute\s*\(.*?$/)
	}
	
	
	def "Add transformation SPEL call if it does not already exists"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BusinessRulesImplementation dynVarValidator = new BusinessRulesImplementation(new File(templateFilePath))
			def json = new JsonSlurper().parseText(this.getJsonTest01())
		when:
			dynVarValidator.addTransformations(json.variables[0])
			println dynVarValidator.getTemplateSrc()
		then:
			String aSourceCodeText = dynVarValidator.getTemplateSrc()
			
			aSourceCodeText.indexOf("SpelExpressionParser") > 0
			aSourceCodeText.indexOf("spelParser.parseExpression") > 0
			aSourceCodeText.indexOf("T(com.americanexpress.commutil.formatter.EffDateFormatter).execute") > 0
	}
	
	
	private String getJsonTest01()
	{
		"""{"feeder": "BOL","lob": "BOL","template": "AGNEUBOL0030001","variables": [{"business-rules": [{"rule": "com.americanexpress.commutil.formatter.EffDateFormatter"}],"className": "com.aexp.gcs.schema.sendcorr.CommunicationInformationType","mapping": "Envelope.Body.Request.CommunicationInformation.RequestDate"}]}"""
	}
	
}
