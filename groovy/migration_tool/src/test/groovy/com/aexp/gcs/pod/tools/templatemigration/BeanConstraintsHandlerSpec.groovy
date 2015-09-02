package com.aexp.gcs.pod.tools.templatemigration

import groovy.xml.XmlUtil

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class BeanConstraintsHandlerSpec extends Specification
{
	@Autowired
	AppSettings appSettings
	
	def "Given a classnameType corresponding classnameTypeConstraints.XML must be loaded"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BeanConstraintsHandler	aBeanConstHandler	=	new BeanConstraintsHandler("NameDetailsType", appSettings, new MessagePlaceHolder())
		expect:
			aBeanConstHandler.constraintMappings.bean[0].@class == "NameDetailsType"
	}
	
	
	def "Navigating the Constraints XML"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			File 	aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/CustomerPIITypeConstraints.xml")
	
			aBeanConstFile.withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			
		expect:
			def cm = new XmlParser(false, false).parse(new File("${appSettings.beanConstraintsFolder}/CustomerPIITypeConstraints.xml"))
			
			println cm.bean[0]
						.field.find{ it.@name == 'relationShipID' }
						.constraint.find{ it.@annotation == 'org.hibernate.validator.constraints.NotEmpty' }
						.groups.value.find{ it.text() == 'com.aexp.gcs.poa.validate.feeder.ALE' }.text()
			println cm.bean[0]
						.field.find{ it.@name == 'relationShipID' }
						.constraint.find{ it.@annotation == 'org.hibernate.validator.constraints.NotEmpty' }
						.groups
	}
	
	
	/*
	 Given a structure like:
		 <bean ...>
	        <field name="relationShipTypeCode">
	            <constraint annotation="org.hibernate.validator.constraints.NotEmpty">
	                <message>Relationship Type Code is empty</message>
	                <groups>
	                    <value>com.aexp.gcs.poa.validate.feeder.WCC</value>
	                </groups>
	            </constraint>
	        </field>
	    </bean>
	    The  method under test must add a <value> entry under <groups> if such value is not already present 
	 */
	@Unroll()
	def "Add validation group to existing entry in a bean validation XML file"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			File 	aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/${testType}Constraints.xml")
	
			aBeanConstFile.withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			
			BeanConstraintsHandler constraintsHandler = new BeanConstraintsHandler(testType, appSettings, new MessagePlaceHolder())

		when:
			def aConstraint	=	constraintsHandler.getConstraintMappings().bean[0]
								.field.find { it.@name == testField }
								.constraint.find{ it.@annotation  == testValidator}
			constraintsHandler.addValidationGroup(testTemplate, aConstraint)
		
		then:
			new XmlSlurper(false, false).parseText(XmlUtil.serialize(constraintsHandler.getConstraintMappings()))
					.bean[0].field.find{ it.@name == testField }
					.constraint.find{ it.@annotation == testValidator }
					.groups[0].value.find{ it.text() == "com.aexp.gcs.poa.validate.template.${testTemplate}" }.text() == testResult
			(old(XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == testEquals
		where:
			testType		<<	['CustomerPIIType', 									'CustomerPIIType']
			testField		<<	['relationShipTypeCode', 								'relationShipTypeCode']
			testValidator	<<	['org.hibernate.validator.constraints.NotEmpty', 		'org.hibernate.validator.constraints.NotEmpty']
			testTemplate	<<	['AGNEUBOL0030001', 									'ALEENALEOBC0002']
			testResult		<<	['com.aexp.gcs.poa.validate.template.AGNEUBOL0030001',	'com.aexp.gcs.poa.validate.template.ALEENALEOBC0002']
			testEquals		<<	[false, 												true]
	}
	
	
	/*
	 Given a structure like:
		 <bean ...>
	        <field name="relationShipTypeCode">
	            <constraint annotation="org.hibernate.validator.constraints.NotEmpty">
	                <message>Relationship Type Code is empty</message>
	                <groups>
	                    <value>com.aexp.gcs.poa.validate.feeder.WCC</value>
	                </groups>
	            </constraint>
	        </field>
	    </bean>
	    The  method under test must add a <constraint> entry under <field> if such constraint is not already present.
	    The newly created <constraint> element have an empty <groups> section, which gets populated delegating a call to addValidationGroup()
	 */
	@Unroll()
	def "Add a constraint to existing bean field in bean validation XML file"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			MessagePlaceHolder msgPlaceholderMock	=	Mock()
			File 	aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/${testType}Constraints.xml")
			
			aBeanConstFile.withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			
			BeanConstraintsHandler constraintsHandler = new BeanConstraintsHandler(testType, appSettings, msgPlaceholderMock)
	
		when:
			def aFieldNode	=	constraintsHandler.getConstraintMappings().bean[0]
													.field.find { it.@name == testField }
			constraintsHandler.addConstraint(testTemplate, testValidator, aFieldNode)
		
		then:
			println XmlUtil.serialize(constraintsHandler.getConstraintMappings())
			new XmlSlurper(false, false).parseText(XmlUtil.serialize(constraintsHandler.getConstraintMappings()))
					.bean[0].field.find{ it.@name == testField }
					.constraint.find{ it.@annotation == testValidator.validator }
					.groups[0].value.find{ it.text() == "com.aexp.gcs.poa.validate.template.${testTemplate}" }.text() == testResult
			(old(XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == testEquals
			(0..1) * msgPlaceholderMock.addPlaceholder("${testValidator.validator}.message")
			(0..1) * msgPlaceholderMock.placeholderExists(_ as String) >> testEquals
		where:
			testType		<<	['CustomerPIIType', 
								 'CustomerPIIType',
								 'CustomerPIIType',
								 'CustomerPIIType']
			testField		<<	['relationShipTypeCode', 
								 'relationShipTypeCode',
								 'relationShipTypeCode',
								 'relationShipTypeCode']
			testValidator	<<	[[validator: 'org.hibernate.validator.constraints.Email'], 
								 [validator: 'org.hibernate.validator.constraints.NotEmpty'],
								 [validator: 'javax.validation.constraints.Size', elements: ['max': '4']],
								 [validator: 'javax.validation.constraints.MultiElements', elements: ['min': 1, 'max': '40', 'elem1': 'ABC', 'elem2': 123, 'elem3':'Some value here']]
								 ]
			testTemplate	<<	['AGNEUBOL0030001', 
								 'ALEENALEOBC0002',
								 'AGNEUBOL0030001',
								 'AGNEUBOL0030001']
			testResult		<<	['com.aexp.gcs.poa.validate.template.AGNEUBOL0030001', 
								 'com.aexp.gcs.poa.validate.template.ALEENALEOBC0002',
								 'com.aexp.gcs.poa.validate.template.AGNEUBOL0030001',
								 'com.aexp.gcs.poa.validate.template.AGNEUBOL0030001']
			testEquals		<<	[false, 
								 true,
								 false,
								 false]
	}
	
	
	def "Constraint manipulation must update <message> element if it does not already contains a Message Placehorder"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			MessagePlaceHolder msgStub = Stub()
			
			msgStub.placeholderExists(_ as String) >> { true }
			new File("${appSettings.beanConstraintsFolder}/${testType}Constraints.xml").withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			BeanConstraintsHandler constraintsHandler = new BeanConstraintsHandler(testType, appSettings, msgStub)
		
		when:
			def aFieldNode	=	constraintsHandler.getConstraintMappings().bean[0].field.find { it.@name == testField }
			constraintsHandler.addConstraint(testTemplate, testValidator, aFieldNode)
		
		then:
			constraintsHandler.getConstraintMappings().bean[0].find{ it.@name == testField }
								.constraint.find{ it.@annotation = testValidator.validator }.message.text() == "{${testValidator.validator}.message}"
		
		where:
			testType		<<	['CustomerPIIType', 'CustomerPIIType']
			testField		<<	['relationShipTypeCode', 'relationShipID']
			testValidator	<<	[[validator: 'org.hibernate.validator.constraints.NotEmpty'], [validator: 'org.hibernate.validator.constraints.NotEmpty']]
			testTemplate	<<	['AGNEUBOL0030001', 'AGNEUBOL0030001']
	}
	
	
	/*
	 Given a structure like:
		 <bean ...>
	        <field name="relationShipTypeCode">
	            <constraint annotation="org.hibernate.validator.constraints.NotEmpty">
	                <message>Relationship Type Code is empty</message>
	                <groups>
	                    <value>com.aexp.gcs.poa.validate.feeder.WCC</value>
	                </groups>
	            </constraint>
	        </field>
	    </bean>
	    The  method under test must add a <field> entry under <bean> if such field element is not already present.
	    The newly created <constraint> element have an empty <groups> section, which gets populated delegating a call to addValidationGroup()
	 */
	@Unroll()
	def "Add field to existing bean in validation XML file"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			MessagePlaceHolder msgPlaceholderStub	=	Stub() 
			msgPlaceholderStub.placeHolderExists(_ as String) >> testEquals
			
			File 	aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/${testType}Constraints.xml")
	
			aBeanConstFile.withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			
			BeanConstraintsHandler constraintsHandler = new BeanConstraintsHandler(testType, appSettings, msgPlaceholderStub)
	
		when:
			constraintsHandler.addField(testTemplate, testValidator, testField)
		
		then:
			println XmlUtil.serialize(constraintsHandler.getConstraintMappings())
			new XmlSlurper(false, false).parseText(XmlUtil.serialize(constraintsHandler.getConstraintMappings()))
					.bean[0].field.find{ it.@name == testField }
					.constraint.find{ it.@annotation == testValidator.validator }
					.groups[0].value.find{ it.text() == "com.aexp.gcs.poa.validate.template.${testTemplate}" }.text() == testResult
			(old(XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == testEquals
		where:
			testType		<<	['CustomerPIIType', 
								 'CustomerPIIType']
			testField		<<	['productName', 
								 'relationShipTypeCode']
			testValidator	<<	[[validator: 'org.hibernate.validator.constraints.NotEmpty'], 
								 [validator: 'org.hibernate.validator.constraints.NotEmpty']
								 ]
			testTemplate	<<	['AGNEUBOL0030001', 
								 'ALEENALEOBC0002']
			testResult		<<	['com.aexp.gcs.poa.validate.template.AGNEUBOL0030001', 
								 'com.aexp.gcs.poa.validate.template.ALEENALEOBC0002']
			testEquals		<<	[false, 
								 true]
	}
	
	@Unroll
	def "Write the modified XML back to disk"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			MessagePlaceHolder msgPlaceholderMock	=	Mock()
			File 	aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/${testType}Constraints.xml")
	
			aBeanConstFile.withWriter{ it << defaultCustomerPIITypeConstraintsXml() }
			
			BeanConstraintsHandler constraintsHandler = new BeanConstraintsHandler(testType, appSettings, msgPlaceholderMock)
	
		when:
			constraintsHandler.addField(testTemplate, testValidator, testField)
			constraintsHandler.persist()
		
		then:
			(old(XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == XmlUtil.serialize(constraintsHandler.getConstraintMappings())) == testEquals
		where:
			testType		<<	['CustomerPIIType', 'CustomerPIIType']
			testField		<<	['relationShipTypeCode', 'productName']
			testValidator	<<	[[validator: 'org.hibernate.validator.constraints.NotEmpty'], 
								 [validator: 'org.hibernate.validator.constraints.NotEmpty']
								 ]
			testTemplate	<<	['ALEENALEOBC0002', 'AGNEUBOL0030001']
			testResult		<<	['com.aexp.gcs.poa.validate.template.ALEENALEOBC0002', 'com.aexp.gcs.poa.validate.template.AGNEUBOL0030001']
			testEquals		<<	[true, false]
			
	}
	
	def "Create bean constraint XML file"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BeanConstraintsHandler	aBeanConstHandler	=	new BeanConstraintsHandler("NameDetailsType", appSettings, Stub(MessagePlaceHolder))
			File 					aBeanConstFile 		=	new File("${appSettings.beanConstraintsFolder}/ServiceDetailsTypeConstraints.xml")
			
			if (aBeanConstFile.exists()) {
				aBeanConstFile.delete()
			}
			
		when:
			aBeanConstHandler.createConstraintsFile(aBeanConstFile)
		
		then:
			!old(aBeanConstFile.exists())
			aBeanConstFile.exists()
			
			def constraintMappings	=	new XmlSlurper(false, false).parse(aBeanConstFile)
			constraintMappings."default-package".text() == 'com.aexp.gcs.schema.sendcorr'
			constraintMappings.bean[0].@class == 'ServiceDetailsType'
	}
	
	
	@Unroll()
	def "Given a File object containing the path to #testFileName extract the Bean Class Name: #testResult"()
	{
		given:
			File aBeanConstFile = new File("${appSettings.beanConstraintsFolder}/${testFileName}")
			BeanConstraintsHandler aBeanConstHandler	=	new BeanConstraintsHandler("NameDetailsType", appSettings, new MessagePlaceHolder())
			
		when:
			String aResult = aBeanConstHandler.getBeanClassNameFromConstraintsFileName(aBeanConstFile)
		
		then:
			aResult	==	testResult
			
		where:
					testFileName						|	testResult
			"CustomerInformationTypeConstraints.xml"	|	"CustomerInformationType"
			"WrongFileName.xml"							|	null
	}
	
	@Unroll()
	def "Register new bean constrains XML file in validation.xml"()
	{
		given:
			appSettings.beanConstraintsFolder = Thread.currentThread().getContextClassLoader().getResource("business-rules-test/src/main/resources/META-INF").getFile()
			BeanConstraintsHandler aBeanConstHandler	=	new BeanConstraintsHandler("NameDetailsType", appSettings, new MessagePlaceHolder())
			new File("${appSettings.beanConstraintsFolder}/validation.xml").withWriter{ it << defaultValidationXml() }
	
//		expect:
//			new File(appSettings.beanConstraintsFolder).exists()
//			aBeanConstHandler
			
		when:
			File aConstrainsFile	=	new File("${appSettings.beanConstraintsFolder}/${testConstrainsFile}")
			boolean fileUpdated		=	aBeanConstHandler.registerConstrainsFile(aConstrainsFile)
		
		then:
			fileUpdated	==	testFileUpdated
			
			new XmlParser().parse(new File("${appSettings.beanConstraintsFolder}/validation.xml"))
				."constraint-mapping".find(){
					it.text() == "META-INF/${testConstrainsFile}"
				}
		
		where:
					testConstrainsFile					|	testFileUpdated
			'DeliveryInformationTypeConstraints.xml'	|		false
			'PhysicalAddressTypeConstraints.xml'		|		true
	}
	
	@Ignore()
	def "Adding a constraints-mapping to validation.xml"()
	{
		given:
			def valConfig = new XmlParser(false, false).parseText(defaultValidationXml())
		when:		
			def newNode		=	new Node(null, "constraint-mapping", ["META-INF/testTypeConstraints.xml"])
			
			println valConfig."constraint-mapping".find{ it.text() == "META-INF/testTypeConstraints.xml" }
			println valConfig."constraint-mapping".find{ it.text() == "META-INF/FeederDetailsTypeConstraints.xml" }
			
			valConfig.children().add(valConfig.children().size() - 1, newNode)
			
			println XmlUtil.serialize(valConfig)
		then:
			valConfig."constraint-mapping".size() < new XmlSlurper().parseText(XmlUtil.serialize(valConfig))."constraint-mapping".size()
	}
	
	
	/*
	 * Leaving out PhysicalAddressTypeConstraints.xml mapping on purpose
	 */
	private String defaultValidationXml()
	{
"""<?xml version="1.0" encoding="UTF-8"?>
<validation-config
	xmlns="http://jboss.org/xml/ns/javax/validation/configuration"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://jboss.org/xml/ns/javax/validation/configuration">

	<default-provider>org.hibernate.validator.HibernateValidator</default-provider>
	<message-interpolator>org.hibernate.validator.engine.ResourceBundleMessageInterpolator</message-interpolator>
	<traversable-resolver>org.hibernate.validator.engine.resolver.DefaultTraversableResolver</traversable-resolver>
	<constraint-validator-factory>org.hibernate.validator.engine.ConstraintValidatorFactoryImpl</constraint-validator-factory>

	<constraint-mapping>META-INF/BodyTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/CommunicationInformationTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/CustomerPIITypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/DeliveryInformationTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/ElectronicAddressTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/FeederDetailsTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/HeaderTypeConstraints.xml</constraint-mapping>
	<constraint-mapping>META-INF/NameDetailsTypeConstraints.xml</constraint-mapping>

	<property name="hibernate.validator.fail_fast">false</property>

</validation-config>"""
	}
	
	
	
	private String defaultCustomerPIITypeConstraintsXml()
	{
"""<?xml version="1.0" encoding="UTF-8"?>
<constraint-mappings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://jboss.org/xml/ns/javax/validation/mapping validation-mapping-1.0.xsd"
	xmlns="http://jboss.org/xml/ns/javax/validation/mapping">
	<default-package>com.aexp.gcs.schema.sendcorr</default-package>
	
	 <bean class="CustomerPIIType" ignore-annotations="false">
		<field name="relationShipID">
			<constraint annotation="org.hibernate.validator.constraints.NotEmpty">
				<message>relationShipID is empty</message>
				<groups>
					<value>com.aexp.gcs.poa.validate.feeder.WCC</value>
					<value>com.aexp.gcs.poa.validate.feeder.GNS</value>
					<value>com.aexp.gcs.poa.validate.feeder.ALE</value>
					<value>com.aexp.gcs.poa.validate.feeder.AEM</value>
					<value>com.aexp.gcs.poa.validate.feeder.ECR</value>
					<value>com.aexp.gcs.poa.validate.feeder.EGL</value>
					<value>com.aexp.gcs.poa.validate.feeder.AMR</value>
				</groups>
			</constraint>
		</field>

		<!--	This is out test field		-->
		<field name="relationShipTypeCode">
			<constraint annotation="org.hibernate.validator.constraints.NotEmpty">
				<message>{org.hibernate.validator.constraints.NotEmpty.message}</message>
				<groups>
					<value>com.aexp.gcs.poa.validate.template.ALEENALEOBC0002</value>
					<value>com.aexp.gcs.poa.validate.feeder.WCC</value>
				</groups>
			</constraint>
		</field>
	</bean>
</constraint-mappings>"""
	}
}
