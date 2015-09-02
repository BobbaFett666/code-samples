package com.aexp.gcs.pod.tools.templatemigration

import java.io.File;
import java.util.Map;

/**
 * This class is responsible of updating JAVA SOURCE code that deals with Dynamic Variable validation.<br/>
 * This class locates and, if needed, adds or creates validation groups and annotations to the bean
 * source code 
 * 
 * @author Victor Navarro
 *
 */

class DynamicVariableValidation
{
	File validationClassFile
	String classSourceCode
	boolean sourceChanged
	
	/**
	 * The Constructor takes a File object that points to the .java source code file to handle, reads it and places its text into the
	 * this.classSourceCode attribute.
	 * @param pValidationFile the .java source file. Raises and exception if the file does not exists or if the item referenced is not a file
	 */
	public DynamicVariableValidation(File pValidationFile)
	{
		if (!pValidationFile.exists() || !pValidationFile.isFile()) {
			throw new Exception("Validation file ${pValidationFile.absolutePath} not found or is not a file")
		}
		
		this.validationClassFile	=	pValidationFile
		this.classSourceCode		=	this.validationClassFile.text
		this.sourceChanged			=	false
	}
	
	/**
	 * The Constructor takes a String containing a java source code full path, instantiates a File with it and delegates object instantiation to the
	 * constructor that takes a File object as argument
	 * 
	 * @param pValidationFilePath path to the .java source file
	 */
	public DynamicVariableValidation(String pValidationFilePath)
	{
		this(new File(pValidationFilePath))
	}
	
	
	/**
	 * Takes the property that needs to be validated, the raw validator information from the json tree and the template this validation applies to. 
	 * Builds the annotation parameters map and delegates validation code change to addVaidationGroupGetter method.
	 * @param pPropertyName the name of the property that is required to have validation 
	 * @param pValidator the raw validation tree portion from the input json
	 * @param pTemplateId the template to what this validation applies
	 */
	public void addValidation(String pPropertyName, Map pValidator, String pTemplateId)
	{
		if (!pValidator.validator) {
			println "\tNo validator specified for Dynamic Variable ${pPropertyName}. Unable to process"
			return
		}
		
		String aValidatorName	=	pValidator.validator.substring(pValidator.validator.lastIndexOf('.') + 1)
		Map annotationParams = [name: aValidatorName]
		
		pValidator.elemens?.each{ pElemName, pElemValue ->
			annotationParams[pElemName]	=	pElemValue
		}

		println "\tAdding Dynamic Validation ${aValidatorName} for ${pTemplateId} to ${pPropertyName}"
		
		this.addValidationGroupToGetter(pPropertyName, annotationParams, pTemplateId)
	}
	
	
	/**
	 * This method retrieves the getter associated with the given property name from this.classSourceCode and extracts 
	 * the annotation referenced by pAnnotationParams.name if exists, if not creates one and adds the TEMPLATEID.class 
	 * validation group identified by pTemplateId.<br/>
	 * Then the new annotation is replaced in the getter method source code and the new getter method code is replaced
	 * into this.classSourceCode
	 * @param pPropertyName the name of the property or attribute for which the getter needs to be retrieved
	 * @param pAnnotationParams a Map containing annotation related information. A 'name' element must be present in order to identify
	 * what annotation to work with. Usual parameters here are "message", "min", "max", etc
	 * @param pTemplateId the name of the validation class to the added to the "groups" element of the annotation.
	 */
	public void addValidationGroupToGetter(String pPropertyName, Map pAnnotationParams, String pTemplateId)
	{
		if (!pAnnotationParams || !pAnnotationParams.name) return
		
		String	aMethodOrg		=	this.getGetter(pPropertyName, true)
		String	aMethodNew		=	this.hasAnnotation(aMethodOrg, pAnnotationParams.name) ? new String(aMethodOrg) : this.addAnnotation(pAnnotationParams, aMethodOrg)
		String	anAnnotationOrg	=	this.getAnnotation(pAnnotationParams.name, aMethodNew)
		String	anAnnotationNew	=	this.addValidationGroup(pTemplateId, anAnnotationOrg)
		
		if (anAnnotationNew != anAnnotationOrg) {
			aMethodNew				=	aMethodNew.replace(anAnnotationOrg, anAnnotationNew)
			this.classSourceCode	=	this.classSourceCode.replace(aMethodOrg, aMethodNew)
			this.sourceChanged		=	true
		}
	}
	
	
	/**
	 * Saves the source code back to file only if it has been changed
	 */
	public void persist()
	{
		if (this.sourceChanged) {
			println "Persisting ${this.validationClassFile.getName()}"
			this.validationClassFile.withWriter { it << classSourceCode }
		}
	}

	
	/**
	 * Given a property name, this method returns the entire property getter declaration including the list
	 * of annotations that applies to it.
	 * @param pPropertyName the name of the property or class attribute to obtain a getter text for.
	 * @return the entire property getter method source code with annotations.
	 */
	protected String getGetter(String pPropertyName, boolean pAddIfMissing = false)
	{
		String aGetterName	=	"get${pPropertyName.capitalize()}"
		def aPattern		=	'(?s).*?([^;]*' + aGetterName + '\\s*\\(\\s*\\)).*'
		def aMatcher		=	this.classSourceCode =~ aPattern
		
		if (aMatcher.matches()) {
			return aMatcher.size() ? aMatcher[0][1] : null
		}
		else {
			if (pAddIfMissing) {
				this.addGetterAndSetter(pPropertyName)
				return this.getGetter(pPropertyName, false)
			}
			else {
				return null
			}
		}
	}
	
	
	/**
	 * Retrieves the entire annotation declaration indicated by pAnnotation within the method
	 * source code referenced by pMethodTxt
	 * @param pAnnotation the name of the Annotation (with the leading '&#064;' character) to retrieve the source code from
	 * @param pMethodTxt the method source code where to look for pAnnotation
	 * @return full annotation source code
	 */
	protected String getAnnotation(String pAnnotation, String pMethodTxt)
	{
		def aPattern		=	'(?s)(?:\\s*' + pAnnotation + ')(?:\\s*\\(.*?\\))*'
		def aMatcher		=	pMethodTxt =~ aPattern
		
		aMatcher.size() ? aMatcher[0] : ""
	}

	
	/**
	 * Adds the annotation whose name and specific parameters are given in the map pAnnParams to a given a method source code. An empty "groups"
	 * parameter is automatically added to the annotation by default.
	 * @param pAnnParams this is a Map indicating the annotation parameters, from whose only required value to get this method 
	 * to do something is the "name" of the annotation, if no "name" entry provided the method returns the method source code
	 * referenced by pMthodTxt unmodified.<br/>
	 * If there is no "message" defined in the annotation parameters map a default one is provided with the text 'Default Error Message'
	 * All other values in the annotation parameters are added as annotation parameters, so for example if we provide this method with
	 * a pAnnParams like :<br/>
	 * 	['name': 'SomeValidationAnnotation', 'max':10, 'min':1, default:5]<br/>
	 * this method generates an annotation as:<br/>
	 * 	&#064;SomeValidationAnnotation(groups = {}, message = "Default Error Message", max = 10, min = 1, default = 5)
	 * @param pMethodTxt the method source code to which the specified annotation needs to be added to
	 * @return the resulting method source code after adding the annotation. Returns the method source code unmodified if
	 * no annotation name is provided as part of the annotation parameters map or if the annotation is already present in the 
	 * method source.
	 */
	protected String addAnnotation(Map pAnnParams, String pMethodTxt)
	{
		if (!pAnnParams.name) return pMethodTxt
		
		Map 			annParams		= [message: 'Default Error Message'] + pAnnParams
		String			anAnnotation	= (annParams.name[0] != '@') ? "@${annParams.name}" : annParams.name
		StringBuilder	aMethodTxt		= new StringBuilder(pMethodTxt)
		
		if (!this.hasAnnotation(pMethodTxt, anAnnotation)) {
			def aMatcher	=	aMethodTxt.toString() =~ /(?m)^\s*(.*?\s+get.*)$/
			
			if (aMatcher.size()) {
				int aPos = aMethodTxt.indexOf(aMatcher[0][1])
				String annotationParams = ""
				
				annParams.findAll{ it.key != 'name' }.collect{
					 annotationParams += ((annotationParams.size() > 0) ? ', ' : '')
					 if (it.value instanceof String) {
						 annotationParams += "${it.key} = \"${it.value}\""
					 }
					 else {
						 annotationParams += "${it.key} = ${it.value}"
					 }
				}
				
				aMethodTxt.insert(aPos, "${anAnnotation}(groups = {}, ${annotationParams})${getSpaces(pMethodTxt)}")
			}
		}
		
		aMethodTxt.toString()
	}


	/**
	 * Adds the validation group referenced by the TemplateId passed in to the given annotation text. 
	 * So for example, if we have an annotation like:<br/>
	 * &#064;NotNull(groups = {FINFIWCCPNR0004.class, FINSVWCCPNR0007.class,<br/>
	 * 		NLDNLWCCCNS0011.class, ESPESWCCCNS0021.class,<br/>
            AUTDEWCCCNS0013.class, SWESVWCCCNS0015.class,<br/>
            SWESVWCCCNS0018.class, DEUDEWCCCNS0013.class}, message = "Name is null")<br/>
       and need to add validation for template 'ITAITWCCCNS0136' the call to this method will 
       add 'ITAITWCCCNS0136.class' at the end of the validation group list. 
	 * @param pTemplateId the template Id (with or without the .class extension) to the added to the validation group
	 * @param pAnnotationTxt the annotation source code to where TEMPLATEID.class will be added  
	 * @return new annotation source code with the validation group added
	 */
	protected String addValidationGroup(String pTemplateId, String pAnnotationTxt)
	{
		StringBuilder anAnnotationTxt	=	new StringBuilder(pAnnotationTxt)
		
		if (anAnnotationTxt.indexOf(pTemplateId) == -1) {
			
			//	If there is no "groups" parameter in the annotation
			if (anAnnotationTxt.indexOf('groups') == -1) {
				String	groupsStr	= 'groups = {}'
				
				//	If annotation has parenthesis, adds "groups" at the beginning of the annotation parameter list
				if (anAnnotationTxt.toString().matches(/(?s)\s*.*?\s*?\(.*?\)\s*/)) {
					anAnnotationTxt.insert(anAnnotationTxt.indexOf('(') + 1, groupsStr)
				}
				else {
					anAnnotationTxt.append("(${groupsStr})")
				}
			}
			
			def 	aGroupsMatcher	= anAnnotationTxt.toString() =~ /(?s)groups\s*=\s*\{(.*?)\}/
			String	aGroupTxt		= null
			String	aSpace			= this.getSpaces(anAnnotationTxt.toString())
			
			if (aGroupsMatcher[0][1].trim().length() > 0) {
				aGroupTxt = (aGroupsMatcher[0][1].trim().split(/,\s*/) + "${pTemplateId}.class").join(', ')
									.replaceAll(/((.*?\.class,*\s*){2})/, '$1\n' + "${aSpace}${aSpace}${aSpace}")
			}
			else {
				aGroupTxt = "${pTemplateId}.class"
			}
			
			anAnnotationTxt = new StringBuilder(anAnnotationTxt.replaceAll(/(?s)groups\s*=\s*\{(.*?)\}/){ "groups = {${aGroupTxt}}" })
		}
		
		anAnnotationTxt.toString()
	}

	
	/**
	 * Adds getter and setter method signatures for a given property. Assumes property is type String as there is no way to figure out the real property type
	 * @param pPropertyName the name of the property to which add getter and setter method signatures
	 */
	protected void addGetterAndSetter(String pPropertyName)
	{
		String aGetterAndSetter = "\tpublic String get${pPropertyName.capitalize()}();\n\n\tpublic void set${pPropertyName.capitalize()}(String p${pPropertyName.capitalize()});\n\n"
		int aIdx = this.classSourceCode.lastIndexOf('}')
		int aPos = aIdx - 1
		
		while (aPos >= 0) {
			if (this.classSourceCode.charAt(aPos) == "\n") {
				this.classSourceCode = this.classSourceCode.substring(0, aPos + 1) + aGetterAndSetter + this.classSourceCode.substring(aPos + 1)
				break
			}
			aPos--
		}
	}
	
	/**
	 * Retrieves the spacing at the beginning of the method declaration, so new content can be properly indented
	 * @param pCodeTxt the getter method source code from where to figure out the leading space 
	 * @return the spacing at the beginning of the method declaration
	 */
	private String getSpaces(String pCodeTxt)
	{
		def aMatcher = pCodeTxt =~ /(?m)^(\s+).*?\s+get.*$/
		
		if (!aMatcher.size()) {
			aMatcher = pCodeTxt.replaceAll('\n', '') =~ /(?s)^(\s+)@.*$/
		}
		
		aMatcher.size() ? aMatcher[0][1] : ''
	}

	
	/**
	 * Searches for the presence of an annotation with the given name on a method source code
	 * @param pMethodTxt the method source code where to perform the annotation search
	 * @param pAnnotation the name of the annotation to look for in the method source code referenced by pMethodTxt
	 * @return true is annotation is defined for method, false otherwise
	 */
	private boolean hasAnnotation(String pMethodTxt, String pAnnotation)
	{
		if (pAnnotation[0] != '@')
			return pMethodTxt.indexOf("@${pAnnotation}") >= 0
		else
			return pMethodTxt.indexOf(pAnnotation) >= 0
	}
	
/*
	private boolean hasGroup(String pAnnotation, String pGroup)
	{
		pAnnotation.indexOf(pGroup) > -1
	}
*/	
}
