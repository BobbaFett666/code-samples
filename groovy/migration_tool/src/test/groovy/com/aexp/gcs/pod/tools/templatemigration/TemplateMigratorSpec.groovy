package com.aexp.gcs.pod.tools.templatemigration

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class TemplateMigratorSpec extends Specification
{
	
	@Autowired
	TemplateMigrator aMigrator
	
	@Autowired
	AppSettings appSettings
	
	def "Read and parse JSON section out of a TEMPLATEID.txt file"()
	{
		when:
			def json = this.aMigrator.parseJsonFromTemplateFile("BOL", "AGNEUBOL0030001")
		then:
			json?.feeder == "BOL"
			json?.template == "AGNEUBOL0030001"
			json?.variables?.size()
			json?.variables[0].mapping.indexOf('Dynamic') == -1
			json?.variables[0].validations?.each {
				println "${it.validator}\t${it.elements?.getClass()?.getName()}"
			}
			json?.variables[1].validations?.each {
				println it.validator
			} == null
	}
	
	@Unroll()
	def "Check that mapping elements start with Envelope item"()
	{
		given:
			JsonSlurper jsonSlurper = new JsonSlurper()
		when:
			def json =	jsonSlurper.parseText(this."${jsonRetrievalMethod}"())
			json = this.aMigrator.checkEnvelopePresence(json)
			
			println json.variables.findAll{ println it }
		then:
			json.variables.findAll{ it.mapping && it.mapping.indexOf('Probably Dynamic') == -1 }
				.each {
					assert it.mapping.startsWith('Envelope')
				}
		where:
			jsonRetrievalMethod << ['goodJsonStr', 'notSoGoodJsonStr']
	}
	
	@Unroll()
	def "Check and Ajust case of mapping elements"()
	{
		when:
			def json = new JsonSlurper().parseText(this."${jsonRetrievalMethod}"())
			json = this.aMigrator.fixCase(json)
			
		then:
			json.variables.each{
				it.mapping?.split('\\.').each {
					assert it.matches(/^[A-Z].*/)
				}
			}
		where:
			jsonRetrievalMethod << ['goodJsonStr', 'notSoGoodJsonStr']
	}
	
	@Unroll("Variable #testVarName is dynamic: #testResult")
	def "Dynamic variables check"()
	{
		when:
			boolean aResult = aMigrator.isDynamicVariable(testVarName)
		then:
			aResult == testResult
		where:
				testVarName																								|	testResult
			'Envelope.Body.Request.DeliveryInformation.DestinationCode'													|	false
			'Envelope.Body.Request.CommunicationInformation.CommunicationVariables.Variable.CommunRcpntAddrLineTxt1'	|	true
	}
	
	
	def "Create Business Rules Module structure"()
	{
		given:
			String aFeeder = 'tst'
			String aModuleName = "/business-rules-${aFeeder.toLowerCase()}"
			File aModuleDir = new File("${appSettings.outputFolder}${aModuleName}")
			
			aModuleDir.deleteDir()
		
		when:
			aMigrator.createBusinessRuleModule(aFeeder)
		
		then:
			new File("${appSettings.outputFolder}${aModuleName}").exists()
			new File("${appSettings.outputFolder}${aModuleName}/pom.xml").exists()
			new File("${aModuleDir.absolutePath}/src/test/java").exists()
			new File("${aModuleDir.absolutePath}/src/main/resources").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/feeder/${aFeeder.toUpperCase()}").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/lob/${aFeeder.toUpperCase()}").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/template").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/feeder").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/lob").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/template").exists()
	}
	
	
	def "Copy files over to the Module directory structure for a brand new Module"()
	{
		given:
			String	aFeeder		= 'TST'
			String	aTemplate	= 'BOLENBOLGPR0002'
			File	aModuleDir	= new File("${appSettings.outputFolder}/business-rules-${aFeeder.toLowerCase()}")
			
			aModuleDir.deleteDir()
			
//			aMigrator.appSettings.sourceFolder = "C:/development/examples/TemplateMigration/src/test/resources/Templates_good"
			aMigrator.appSettings.sourceFolder = "C:/development/projects/migration_tool/src/test/resources/Templates_good"
			
			aMigrator.createBusinessRuleModule(aFeeder)
		
		when:
			aMigrator.addPOAFilesToBusinessRulesModule(aFeeder, aTemplate)
		
		then:
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/feeder/${aFeeder}/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/lob/${aFeeder}/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/template/${aTemplate}/${aTemplate}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/feeder/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/lob/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/template/${aTemplate}.java").exists()
	}

	
	@Ignore("Just needed one time to generate test WCC module")
	def "Copy files over for WCC - ARGESWCCCAE0001"()
	{
		given:
			String	aFeeder		= 'WCC'
			String	aTemplate	= 'ARGESWCCCAE0001'
			File	aModuleDir	= new File("${appSettings.outputFolder}/business-rules-${aFeeder.toLowerCase()}")
			
			aModuleDir.deleteDir()
			
			aMigrator.appSettings.sourceFolder = "C:/development/examples/TemplateMigration/src/test/resources/Templates_good"
			aMigrator.createBusinessRuleModule(aFeeder)
		
		when:
			aMigrator.addPOAFilesToBusinessRulesModule(aFeeder, aTemplate)
		
		then:
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/feeder/${aFeeder}/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/lob/${aFeeder}/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/transform/template/${aTemplate}/${aTemplate}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/feeder/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/lob/${aFeeder}.java").exists()
			new File("${aModuleDir.absolutePath}/src/main/java/com/aexp/gcs/poa/validate/template/${aTemplate}.java").exists()
	}
	
	
	@Ignore()
	def "Merge getters and setters at validate.feeder.<feeder>.java interface"()
	{
		given:
			String	aFeeder			= 'WCC'
			File	aModuleDir		= new File("${appSettings.outputFolder}/business-rules-${aFeeder.toLowerCase()}")
			String 	aPartialPath	= "/src/main/java/com/aexp/gcs/poa/validate/feeder/${aFeeder}.java"	
			aMigrator.appSettings.sourceFolder = "C:/development/examples/TemplateMigration/src/test/resources/Templates_good"
			
			File aModuleFile		=	new File("${aModuleDir}${aPartialPath}")
			File aPoaFile			=	new File("${aMigrator.appSettings.sourceFolder}/${aFeeder}/POA/${aPartialPath.substring(aPartialPath.indexOf('/com/'))}")
			
			aModuleFile.withWriter { it << getTestValidateFeederWCC() }
			
			Map aFieldsMap = [:]
			
		when:
			String aModuleTxt	= aModuleFile.text
			String aPoaTxt		= aPoaFile.text
			String aNewMethods	= ""
			def aPattern		= /(?m)^.*?public\s+(.+?)\sget(.*)\(.*$/
			def aModMatcher		= aModuleTxt =~ aPattern
			def aPoaMatcher		= aPoaTxt =~ aPattern
			
			aModMatcher.each { aFieldsMap[it[2]] = it[1] }
			aPoaMatcher.each { aMatcher ->
				if (!aFieldsMap.containsKey(aMatcher[2])) {
					aNewMethods += buildGetterSetter(aMatcher[2], aMatcher[1])
				}
			}
			
			if (aNewMethods) {
				aModuleTxt = "${aModuleTxt.substring(0, aModuleTxt.lastIndexOf('\n}'))}\n${aNewMethods}}"
			}
			
			println aModuleTxt
		then:
			1==1
	}
	
	
	def "Merge validate.feeder.<feeder>.java interface files"()
	{
		given:
			String	aFeeder			= 'WCC'
			File	aModuleDir		= new File("${appSettings.outputFolder}/business-rules-${aFeeder.toLowerCase()}")
			String 	aPartialPath	= "/src/main/java/com/aexp/gcs/poa/validate/feeder/${aFeeder}.java"
			File	aModuleFile		= new File("${aModuleDir}${aPartialPath}")
			File	aPoaFile		= new File("${aMigrator.appSettings.sourceFolder}/${aFeeder}/POA/${aPartialPath.substring(aPartialPath.indexOf('/com/'))}")
			def 	aPattern		= /(?m)^.*?public\s+(.+?)\sget(.*)\(.*$/

			aMigrator.appSettings.sourceFolder = "C:/development/examples/TemplateMigration/src/test/resources/Templates_good"	
			aModuleFile.withWriter { it << getTestValidateFeederWCC() }
		
		when:
			aMigrator.mergeValidateFeederInterfaces(aModuleFile, aPoaFile)
		
		then:
			def m1 = new File("${aModuleDir}${aPartialPath}").text =~ aPattern
			def m2 = getTestValidateFeederWCC() =~ aPattern
			
			m1.size() > m2.size()
	}
	
	
	@Unroll()
	def "Copy generated transaction information xml file over to the template-transaction-info module"()
	{
		given:
			String	aFeeder			= 'TST'
			String	aTemplate		= 'BOLENBOLGPR0002'
			File	aTemplateDir	= new File("${appSettings.templateTransactionInfo}/src/main/resources/transactionInfo/${aFeeder.toUpperCase()}")
			File	aTemplateFile	= new File("${appSettings.templateTransactionInfo}/src/main/resources/transactionInfo/${aFeeder.toUpperCase()}/${aTemplate}.xml")
			boolean aResult			= null
			if (deleteTargetFile && aTemplateFile.exists()) aTemplateFile.delete()
			if (deleteTargetDir && aTemplateDir.exists()) aTemplateDir.deleteDir()
			aMigrator.appSettings.sourceFolder = "C:/development/projects/migration_tool/src/test/resources/Templates_good"
		
		when:
			aResult = aMigrator.addPOAFileToTransactionInfoModule(aFeeder, aTemplate)
		
		then:
			aResult == testFileWritten
			aTemplateDir.exists()
			aTemplateFile.exists()

		where:
			deleteTargetDir	|	deleteTargetFile	|	testFileWritten
				true		|		true			|		true
				false		|		true			|		true
				false		|		false			|		false
	}


	private String buildGetterSetter(pFieldName, pFieldType)
	{
		String SPACES_BEFORE = '    '
		String SPACES_AFTER = '\n\n'
		
		"${SPACES_BEFORE}public ${pFieldType} get${pFieldName}();${SPACES_AFTER}" +
		"${SPACES_BEFORE}public void set${pFieldName}(${pFieldType} p${pFieldName});${SPACES_AFTER}"
	}
	
	private String getTestValidateFeederWCC()
	{
"""
package com.aexp.gcs.poa.validate.feeder;



/**
 *  This Interface will contain field level validation for different templates
 * 
 */
public interface WCC {


    public String getTotalExpRcpBalAmt();

    public void setTotalExpRcpBalAmt(String TotalExpRcpBalAmt);

    public String getEnvironment();

    public void setEnvironment(String Environment);

    public String getOffrLink1();

    public void setOffrLink1(String OffrLink1);

    public String getNgopenAmt();

    public void setNgopenAmt(String NgopenAmt);

    public String getTodayDate();

    public void setTodayDate(String TodayDate);

    public String getCommunRcpntEmailAddrTxt();

    public void setCommunRcpntEmailAddrTxt(String CommunRcpntEmailAddrTxt);

}
"""		
	}
	
	private String goodJsonStr()
	{
		"""{"feeder":"BOL","lob":"BOL","template":"AGNEUBOL0030001","variables":[{"className":"com.aexp.gcs.schema.sendcorr.DeliveryInformationType","mapping":"Envelope.Body.Request.DeliveryInformation.DestinationCode","validations":[{"validator":"javax.validation.constraints.NotNull"},{"elements":{"max":4},"validator":"javax.validation.constraints.Size"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.EffDateFormatter"}],"className":"com.aexp.gcs.schema.sendcorr.CommunicationInformationType","mapping":"Envelope.Body.Request.CommunicationInformation.RequestDate"},{"className":"com.aexp.gcs.schema.sendcorr.CustomerPIIType","mapping":"Envelope.Body.Request.CustomerPII.PreferredLanguageCode","validations":[{"validator":"com.americanexpress.commutil.validator.LanguageValidator"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.CustNameFormatter"}],"className":"com.aexp.gcs.schema.sendcorr.NameDetailsType","mapping":"Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.NameDetails.FullName","validations":[{"validator":"javax.validation.constraints.NotNull"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.ScottishIrishFormatter"}],"mapping":"CommunRcpntAddrLineTxt1--Probably Dynamic","validations":[{"validator":"javax.validation.constraints.NotNull"},{"elements":{"max":40},"validator":"javax.validation.constraints.Size"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.ScottishIrishFormatter"}],"mapping":"CommunRcpntAddrLineTxt2--Probably Dynamic"},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.ScottishIrishFormatter"}],"mapping":"CommunRcpntAddrLineTxt3--Probably Dynamic"},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.ScottishIrishFormatter"}],"className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","mapping":"Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.City","validations":[{"validator":"javax.validation.constraints.NotNull"},{"elements":{"max":20},"validator":"javax.validation.constraints.Size"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.UpperCaseFormatter"}],"className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","mapping":"Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.StateCode","validations":[{"elements":{"max":3},"validator":"javax.validation.constraints.Size"}]},{"className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","mapping":"Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.PostalCode","validations":[{"elements":{"max":20},"validator":"javax.validation.constraints.Size"}]},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.UpperCaseFormatter"}],"className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","mapping":"Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.CountryCode"},{"business-rules":[{"rule":"com.americanexpress.commutil.formatter.ECRAccountEndingDigitsFormatter"}],"mapping":"Envelope.Body.Request.CommunicationInformation.CommunicationVariables.Variable.RelationShipId","validations":[{"validator":"javax.validation.constraints.NotNull"},{"validator":"com.americanexpress.commutil.validator.RelationshipIdValidator"}]},{"mapping":"TODAY--Probably Dynamic","validations":[{"validator":"com.americanexpress.commutil.validator.TodayValidator"}]},{"className":"com.aexp.gcs.schema.sendcorr.CommunicationInformationType","mapping":"Envelope.Body.Request.CommunicationInformation.CommunicationTrackingID","validations":[{"validator":"javax.validation.constraints.NotNull"}]}]}"""
	}
	
	private String notSoGoodJsonStr()
	{
		"""{"lob":"BOL","feeder":"BOL","template":"AGNEUBOL0030001","variables":[{"mapping":"body.request.communicationInformation.deliveryInformation.destinationCode","className":"com.aexp.gcs.schema.sendcorr.DeliveryInformationType","validations":[{"validator":"javax.validation.constraints.NotNull"}]},{"mapping":"body.request.communicationInformation.requestDate","className":"com.aexp.gcs.schema.sendcorr.CommunicationInformationType","validations":[{"elements":{}}]},{"mapping":"body.request.communicationInformation.customerPII.preferredLanguageCode","className":"com.aexp.gcs.schema.sendcorr.CommunicationInformationType","validations":[{"elements":{}}]},{"mapping":"body.request.communicationInformation.customerPII.recipientContactDetails.contactDetails.nameDetails.fullName","className":"com.aexp.gcs.schema.sendcorr.NameDetailsType","validations":[{"validator":"javax.validation.constraints.NotNull"}]},{"mapping":"body.request.communicationInformation.customerPII.recipientContactDetails.contactDetails.physicalAddress.addressLine","className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","validations":[{"validator":"javax.validation.constraints.NotNull"}]},{"mapping":"body.request.communicationInformation.customerPII.recipientContactDetails.contactDetails.physicalAddress.city","className":"com.aexp.gcs.schema.sendcorr.PhysicalAddressType","validations":[{"validator":"javax.validation.constraints.NotNull"}]},{"mapping":"body.request.communicationInformation.communicationVariables.variable","className":"com.aexp.gcs.schema.sendcorr.VariableType","validations":[{"elements":{}},{"validator":"javax.validation.constraints.NotNull"}]},{"mapping":"body.request.communicationInformation.communicationVariables.variable","className":"com.aexp.gcs.schema.sendcorr.VariableType","validations":[{"elements":{}}]},{"mapping":"body.request.communicationInformation.communicationTrackingID","className":"com.aexp.gcs.schema.sendcorr.CommunicationInformationType","validations":[{"validator":"javax.validation.constraints.NotNull"}]}]}"""
	}

}
