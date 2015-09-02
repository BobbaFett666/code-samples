package com.aexp.gcs.pod.tools.templatemigration

import groovy.xml.XmlUtil

/**
 * This class is used to create and update information concerning a specific bean constraints XML file.<br/>
 * The location of the files to manipulate is provided via an AppSettings value. The way a specific
 * file to work with is identified is by passing the Bean Class Name to the constructor, for example
 * "NameDetailsType" and the corresponding file has a name in the form of "NameDetailsTypeConstraints.xml",
 * if the file exists this class will parse it and will perform operations on it.
 * If the file does not exists, then it needs to be created by taking 
 * resources/filetemplates/bean-constraints.xml as template, gives it the right file name, 
 * defines the <bean> class attribute, saves it to the folder containing all other bean constraints XML
 * files and updates validation.xml with appropriate <constraint-mapping> entry pointing
 * to the newly created file
 * 
 * @author Victor Navarro
 *
 */

class BeanConstraintsHandler
{
	AppSettings appSettings
	def constraintMappings
	String originalMarkup
	File constraintFile
	MessagePlaceHolder msgPlaceholder
	
	private static String TEMPLATE_VALIDATOR_PKG = 'com.aexp.gcs.poa.validate.template'
	
	
	/**
	 * This Constructor requires the Bean Class Name and the Application Settings in order to check for
	 * existance of the Bean Constraints XML files and also the existance of the specific validation XML
	 * file to work with. If the specific file does not exists it calls out createConstraintsFile 
	 * in order to make one based on the template.
	 * 
	 * @param pBeanClassName is the name of the class for which the corresponding Validation XML file is going to be parsed in
	 * @param pAppSettings the general application settings that contains bean constraints folder location
	 */
	BeanConstraintsHandler(String pBeanClassName, AppSettings pAppSettings, MessagePlaceHolder pMsgPlaceholder)
	{
		if (!pAppSettings?.beanConstraintsFolder) {
			throw new Exception("AppSettings is null or no Bean Constraints Folder location has been set")
		}
		
		this.appSettings		=	pAppSettings
		
		File aConstraintsDir	=	new File(this.appSettings.beanConstraintsFolder)
		if (!aConstraintsDir.isDirectory()) {
			throw new Exception("Bean Constraints Folder location indicated in Application Settings does not exist or is not a folder")
		}
		
		this.constraintFile	=	new File("${this.appSettings.beanConstraintsFolder}/${pBeanClassName}Constraints.xml")
		if (!this.constraintFile.exists()) {
			this.createConstraintsFile(this.constraintFile)
			this.registerConstrainsFile(this.constraintFile)
		}
		
		this.constraintMappings	=	new XmlParser(false, false).parse(this.constraintFile)
		this.originalMarkup		=	this.constraintMappings.toString()
		this.msgPlaceholder		=	pMsgPlaceholder
	}
	
	
	/**
	 * Adds a <value> element under  <constraint>...<groups> for a given bean and field if such value is not already present
	 * @param pTemplateId the template ID whose validation group will be added as <value> text
	 * @param pConstraintNode the <constraint> Node reference. A search of its group values is performed here based on the
	 * template name, and if not match found the new <value> entry is created under pConstraintNode.groups[0] 
	 */
	protected void addValidationGroup(String pTemplateId, pConstraintNode)
	{
		def aValGroup	=	pConstraintNode.groups.value.find{
								it.text() == "${TEMPLATE_VALIDATOR_PKG}.${pTemplateId}"
							}

		//	If specific templateId validation group not found in list of groups then add it
		if (!aValGroup) {
			println "\tAdding <value>${pTemplateId}</value> to constraint ${pConstraintNode.@annotation} on ${constraintFile.getName()}"
			//	add new group to the end of <groups> list
			new Node(pConstraintNode.groups[0], "value", ["${TEMPLATE_VALIDATOR_PKG}.${pTemplateId}"])
		}
	}
	
	
	/**
	 * Adds a <constraint> element under <field> for a given bean and field if such constraint is not already present
	 * @param pTemplateId the template ID whose validation group will be added as <value> text
	 * @param pValidator the specific validation class applied to pFieldName
	 * @param pFieldNode the <field> Node reference. A search for pValidator into pFieldNode constraints is performed to
	 * determine if pValidator must be added as new field constraint
	 */
	protected void addConstraint(String pTemplateId, Map pValidator, pFieldNode)
	{
		def aConstraintNode	=	pFieldNode.constraint.find { it.@annotation == pValidator.validator }
		
		if (!aConstraintNode) {
			println "\tAdding <constraint annotation=\"${pValidator.validator}\"> to field ${pFieldNode.@name} on ${constraintFile.getName()}"
			aConstraintNode	=	new Node(pFieldNode, "constraint", [annotation: pValidator.validator])
			new Node(aConstraintNode, "message", ["{${pValidator.validator}.message}"])
			new Node(aConstraintNode, "groups", [])
			pValidator.elements?.each { pElemName, pElemValue ->
				def anElemNode = new Node(aConstraintNode, "element", [name: pElemName])
				anElemNode.value = pElemValue
			}
			this.msgPlaceholder.addPlaceholder("${pValidator.validator}.message")
		}
		else {
			if (aConstraintNode.message.text() != "{${pValidator.validator}.message}") {
				aConstraintNode.message[0].value = "{${pValidator.validator}.message}"
				this.msgPlaceholder.addPlaceholder("${pValidator.validator}.message")
			}
		}
		
		this.addValidationGroup(pTemplateId, aConstraintNode)
	}
	

	/**
	 * Adds a <field> element under <bean> for a given bean if such field is not already present
	 * @param pTemplateId the template ID whose validation group will be added as <value> text
	 * @param pValidator the specific validation class applied to pFieldName
	 * @param pFieldName the name of the field to which this validation is applied
	 */
	public void addField(String pTemplateId, Map pValidator, String pFieldName)
	{
		if (!pValidator.validator) {
			println "\tNo validator specified for Static Variable ${pFieldName}. Unable to process"
			return
		}

		def aFieldNode		=	this.constraintMappings.bean[0].field.find{ it.@name == pFieldName }
		
		if (!aFieldNode) {
			println "\tAdding <field name=\"${pFieldName}\"> to ${constraintFile.getName()}"
			aFieldNode	=	new Node(this.constraintMappings.bean[0], "field", [name: pFieldName])
		}
		
		this.addConstraint(pTemplateId, pValidator, aFieldNode)
	}
	
	
	/**
	 * Compares the original markup read when the file was parsed with the original markup currently in memory and if they are different (there has been modifications) 
	 * the contents of the xml file gets updated. Otherwise it is not modified.
	 */
	public void persist()
	{
		if (this.originalMarkup != this.constraintMappings.toString()) {
			println "Persisting ${this.constraintFile.getName()}"
			this.constraintFile.withWriter { it << XmlUtil.serialize(constraintMappings) }
		}
	}
	
	
	/**
	 * Creates a bean constraints XML file for the given File, taking bean-constraints.xml as template and deducting the Class Name from the filename itself
	 * @param pConstraintsFile the filename of the file to the created
	 */
	protected void createConstraintsFile(File pConstraintsFile)
	{
		String	aBeanClassName	= this.getBeanClassNameFromConstraintsFileName(pConstraintsFile)
		InputStream	inStream	= getClass().getResourceAsStream('/filetemplates/bean-constraints.xml')
		String	aConstText		= inStream.getText().replaceAll(/\\\$\{beanClassname\}\\\$/, aBeanClassName)

		pConstraintsFile.withWriter{ it << aConstText }
	}
	
	/**
	 * Given a file constraints this method edits validation.xml and adds a constraints-mapping element to it if it does not already exist.
	 * @param pConstraintsFile is the bean constraints XML file to register with validation.xml
	 * @return true if validation.xml was updated with a new constraints-mapping element, false otherwise.  
	 */
	protected boolean registerConstrainsFile(File pConstraintsFile)
	{
		File aValidationXmlFile	= new File("${this.appSettings.beanConstraintsFolder}/validation.xml")
		String entryText		= "META-INF/${pConstraintsFile.getName()}"
		def valConfig 			= new XmlParser(false, false).parse(aValidationXmlFile)
		def entryExists			= valConfig."constraint-mapping".find { it.text() == entryText } != null
		
		if (!entryExists) {
			println "\tAdding constraint-mapping ${entryText} to validation.xml file"
			Node aNewMappingNode	=	new Node(null, "constraint-mapping", [entryText])
			
			valConfig.children().add(valConfig.children().size() - 1, aNewMappingNode)
			aValidationXmlFile.withWriter{ it << XmlUtil.serialize(valConfig) }
		}
		
		!entryExists
	}
	
	/**
	 * Given a File object that points to a <BeanClassName>Constraints.xml file this method extracts only the <BeanClassName> portion of it. Based on
	 * POA Migration Guide this must match a class in the sendcorr package
	 * @param pConstraintsFile the File to extract the sendcorr bean class name from
	 * @return the bean class name
	 */
	private String getBeanClassNameFromConstraintsFileName(File pConstraintsFile)
	{
		def m = pConstraintsFile.absolutePath =~ /^.*?[\\\/]META-INF[\\\/](.*)?Constraints.xml$/
		
		m.size() > 0 ? m[0][1] : null
	}
}
