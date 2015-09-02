package com.aexp.gcs.pod.tools.templatemigration

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader)
class DynamicVariableValidationSpec extends Specification
{
	@Autowired
	AppSettings appSettings

	@Shared
	File aValidatorClassFile = new File(Thread.currentThread().getContextClassLoader()
											.getResource('AGNEUBOL0030001.java.txt')
											.getFile())
	
	@Unroll()
	def "Given a property '#testAttr' name obtain it's method declaration with all its annotations"()
	{
		given:
			DynamicVariableValidation dynVarValidation	=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = this.testCodeSnipet()
		when:
			String aResult =	dynVarValidation.getGetter(testAttr)
		then:
			println aResult
			aResult.indexOf(testGetter) >= 0
			(aResult.indexOf('@NotNull') >= 0) == testPos1
			(aResult.indexOf('@Size') >= 0) == testPos1
		where:
			testAttr			|	testGetter			|	testPos1	|	testPos2
			'minimumDue'		|	'getMinimumDue'		|	true		|	true
			'totalDelqAmt'		|	'getTotalDelqAmt'	|	false		|	false
	}
	
	def "Given a property find that there is no getter for it then add it so it can be retrieved"()
	{
		given:
			DynamicVariableValidation dynVarValidation	=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = """package com.aexp.gcs.poa.validate.feeder;

public interface BOL {

	public String getValue();

	public void setValie(String pValue);

}
"""
		when:
			String aMethodSrc	=	dynVarValidation.getGetter('myCustomField')
			if (!aMethodSrc) {
				dynVarValidation.addGetterAndSetter('myCustomField')
				println "New Class:\n${dynVarValidation.classSourceCode}\n\n"
				aMethodSrc	=	dynVarValidation.getGetter('myCustomField')
			}
			
		then:
			aMethodSrc
	}
	
	@Unroll()
	def "Given a Method declaration text with all its annotations, look for #testAnnotation in them and retrieve the complete annotation declaration text"()
	{
		given:
			DynamicVariableValidation	dynVarValidation	=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = this.testCodeSnipet()
			String 						aMethodDefTxt		=	dynVarValidation.getGetter('minimumDue')
		when:
			String anAnnDefTxt		=	dynVarValidation.getAnnotation(testAnnotation, aMethodDefTxt)
		then:
			println anAnnDefTxt
			(anAnnDefTxt.indexOf(testAnnotation) > -1) == testRes1
		where:
			testAnnotation			|	testRes1
				'@NotNull'			|	true
				'@Size'				|	true
				'@OtherValidation'	|	true
				'@Pattern'			|	false
	}
	
	
	//	This one needs to add the @Annotation declaration only
	@Unroll()
	def "Add Validation Annotation if getter does not already has it"()
	{
		given:
			DynamicVariableValidation	dynVarValidation	=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = this.testCodeSnipet()
			String 						aMethodTxt			=	dynVarValidation.getGetter('minimumDue')
		when:
			aMethodTxt	=	dynVarValidation.addAnnotation(testArgs + ['name': testAnnotation], aMethodTxt)
		then:
			println aMethodTxt
			(old(aMethodTxt) != aMethodTxt) == testRes1
			aMethodTxt.indexOf( (testAnnotation[0] != '@' ? "@${testAnnotation}" : testAnnotation ) ) >= 0
			aMethodTxt.indexOf('groups') > 0
			
			if (testRes1) {
				testArgs.each{
					dynVarValidation.getAnnotation(testAnnotation, aMethodTxt).indexOf(it.key) > 0
				}
			}
			
		where:
			testAnnotation	|	testArgs							|	testRes1
			'@Pattern'		|	[:]									|	true
			'@Pattern'		|	[min: 1, message: "Error Message"]	|	true
			'@Pattern'		|	[min: 1, max: 10]					|	true
			'Pattern'		|	[min: 1, max: 10]					|	true
			'@Size'			|	[min: 1, max: 10]					|	false
			'Size'			|	[min: 1, max: 10]					|	false
	}
	

	@Unroll()
	def "Add TEMPLATEID.class validation group at the end of existing template validation group names for an annotation if not already present"()
	{
		given:
			DynamicVariableValidation	dynVarValidation	=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = this.testCodeSnipet()
			String 						aMethodTxt			=	dynVarValidation.getGetter('minimumDue')
			String 						anAnnotationTxt		=	dynVarValidation.getAnnotation(testAnnotation, aMethodTxt)
		when:
			if (!anAnnotationTxt) {
				aMethodTxt		=	dynVarValidation.addAnnotation(['name': testAnnotation], aMethodTxt)
				anAnnotationTxt	=	dynVarValidation.getAnnotation(testAnnotation, aMethodTxt)
			}
			anAnnotationTxt	=	dynVarValidation.addValidationGroup(testValGroup, anAnnotationTxt)
		then:
			println anAnnotationTxt
			(old(anAnnotationTxt) != anAnnotationTxt) == testRes1
			anAnnotationTxt.indexOf(testValGroup) > 0
			anAnnotationTxt.indexOf("groups") > 0
		where:
			testAnnotation		|	testValGroup		|	testRes1
			'@Size'				|	'DEUDEWCCCNS0030'	|	false
			'@Size'				|	'DEUDEWCCCNS0039'	|	true
			'@NewAnnotation'	|	'FRAFRWCCCNS0054'	|	true
			'@OtherValidation'	|	'FRAFRWCCCNS0054'	|	true
	}
	
	@Unroll()
	def "Replace new getter source text"()
	{
		given:
			DynamicVariableValidation dynVarValidation		=	new DynamicVariableValidation(this.aValidatorClassFile)
			dynVarValidation.classSourceCode = this.testCodeSnipet()
		when:
			dynVarValidation.addValidationGroupToGetter(testPropName, testAnnArgs + [name: testAnnotationName], testValidationGroup)
		then:
			println dynVarValidation.getGetter(testPropName)
			(old(dynVarValidation.getGetter(testPropName)) != dynVarValidation.getGetter(testPropName)) == testCompResult
			dynVarValidation.sourceChanged == testCompResult
			dynVarValidation.getAnnotation(testAnnotationName, dynVarValidation.getGetter(testPropName)).indexOf('groups') > 0
			dynVarValidation.getAnnotation(testAnnotationName, dynVarValidation.getGetter(testPropName)).indexOf(testValidationGroup) > 0
		where:
			testPropName		|	testAnnotationName	|	testAnnArgs						|	testValidationGroup	|	testCompResult
			'minimumDue'		|	'@Size'				|	[min:3, max:10]					|	'SWESVWCCCNS9999'	|	true	
			'minimumDue'		|	'@OtherValidation'	|	[val1: 'ABC', val2: '999']		|	'SWESVWCCCNS9999'	|	true
			'OverdueBalanceAmt'	|	'@NotNull'			|	[val1: 'ABC', val2: '999']		|	'SWESVWCCCNS0015'	|	false
			'OverdueBalanceAmt'	|	'@NotNull'			|	[message: 'Test error Message']	|	'SWESVWCCCNS9999'	|	true
			'totalDelqAmt'		|	'@MyValidation'		|	[:]								|	'SWESVWCCCNS9999'	|	true
	}


	private String testCodeSnipet()
	{
"""
    @NotNull(groups = { FINFIWCCINF0016.class, FINFIWCCPNR0001.class,
            FINFIWCCPNR0004.class, FINSVWCCPNR0007.class,
            FINFIWCCPNR0014.class, FINFIWCCPNR0009.class,
            FINFIWCCPNR0011.class, FINSVWCCPNR0011.class,
            FINFIWCCBPP0028.class, NLDNLWCCCNS0010.class,
            NLDNLWCCCNS0011.class, ESPESWCCCNS0021.class,
            AUTDEWCCCNS0013.class, SWESVWCCCNS0015.class,
            SWESVWCCCNS0018.class, DEUDEWCCCNS0013.class,
            DEUDEWCCCNS0048.class, ITAITWCCCNS0028.class,
            ITAITWCCCNS0029.class, ITAITWCCCNS0008.class,
            ITAITWCCCNS0009.class, ITAITWCCCNS0069.class,
            ITAITWCCCNS0070.class, ITAITWCCCNS0073.class,
            ITAITWCCCNS0074.class, ITAITWCCCNS0084.class,
            ITAITWCCCNS0109.class, ITAITWCCCNS0136.class }, message = "OverdueBalanceAmont is null")
    @Size(groups = { FINFIWCCINF0016.class, FINFIWCCPNR0001.class,
            FINFIWCCPNR0004.class, FINSVWCCPNR0007.class,
            FINFIWCCPNR0014.class, FINFIWCCPNR0009.class,
            FINFIWCCPNR0011.class, FINSVWCCPNR0011.class,
            FINFIWCCBPP0028.class, NLDNLWCCCNS0010.class,
            NLDNLWCCCNS0011.class, ESPESWCCCNS0021.class,
            AUTDEWCCCNS0013.class, SWESVWCCCNS0015.class,
            SWESVWCCCNS0018.class, DEUDEWCCCNS0013.class,
            DEUDEWCCCNS0048.class, ITAITWCCCNS0028.class,
            ITAITWCCCNS0029.class, ITAITWCCCNS0008.class,
            ITAITWCCCNS0009.class, ITAITWCCCNS0069.class,
            ITAITWCCCNS0070.class, ITAITWCCCNS0073.class,
            ITAITWCCCNS0074.class, ITAITWCCCNS0084.class,
            ITAITWCCCNS0109.class, ITAITWCCCNS0136.class }, min = 1, message = "OverdueBalanceAmt cannot be empty")
    String getOverdueBalanceAmt();

    void setOverdueBalanceAmt(String OverdueBalanceAmt);

    String getTotalDelqAmt();

    void setTotalDelqAmt(String TotalDelqAmt);

    @NotNull(groups = { FINFIWCCPNR0006.class, NLDNLWCCCNS0013.class,
            NLDNLWCCCNS0014.class, NLDNLWCCCNS0015.class,
            NLDNLWCCCNS0023.class, NLDNLWCCCNS0024.class,
            AUTDEWCCCNS0003.class, AUTDEWCCCNS0006.class,
            AUTDEWCCCNS0007.class, AUTDEWCCCNS0008.class,
            AUTDEWCCCNS0009.class, AUTDEWCCCNS0010.class,
            AUTDEWCCCNS0011.class, AUTDEWCCCNS0012.class,
            AUTDEWCCCNS0019.class, AUTDEWCCCNS0020.class,
            AUTDEWCCCNS0028.class, SWESVWCCCNS0010.class,
            ITAITWCCCNS0010.class, ITAITWCCCNS0011.class,
            ITAITWCCCNS0012.class, ITAITWCCCNS0013.class,
            ITAITWCCCNS0044.class, ITAITWCCCNS0048.class,
            ITAITWCCCNS0049.class, ITAITWCCCNS0005.class,
            ITAITWCCCNS0068.class, ITAITWCCCNS0071.class,
            ITAITWCCCNS0075.class, ITAITWCCCNS0076.class,
            ITAITWCCCNS0079.class, ITAITWCCCNS0095.class,
            ITAITWCCCNS0096.class, ITAITWCCCNS0097.class,
            ITAITWCCCNS0098.class, ITAITWCCCNS0099.class,
            ITAITWCCCNS0114.class, ITAITWCCCNS0118.class,
            ITAITWCCCNS0120.class, ITAITWCCCNS0121.class,
            ITAITWCCCNS0130.class, ITAITWCCCNS0131.class, ITAITWCCCNS0132.class }, message = "ReturnPaymentAmount is null")
    @Size(groups = { FINFIWCCPNR0006.class, NLDNLWCCCNS0013.class,
            NLDNLWCCCNS0014.class, NLDNLWCCCNS0015.class,
            NLDNLWCCCNS0023.class, NLDNLWCCCNS0024.class,
            AUTDEWCCCNS0003.class, AUTDEWCCCNS0006.class,
            AUTDEWCCCNS0007.class, AUTDEWCCCNS0008.class,
            AUTDEWCCCNS0009.class, AUTDEWCCCNS0010.class,
            AUTDEWCCCNS0011.class, AUTDEWCCCNS0012.class,
            AUTDEWCCCNS0019.class, AUTDEWCCCNS0020.class,
            AUTDEWCCCNS0028.class, SWESVWCCCNS0010.class,
            ITAITWCCCNS0010.class, ITAITWCCCNS0011.class,
            ITAITWCCCNS0012.class, ITAITWCCCNS0013.class,
            ITAITWCCCNS0044.class, ITAITWCCCNS0048.class,
            ITAITWCCCNS0049.class, ITAITWCCCNS0005.class,
            ITAITWCCCNS0068.class, ITAITWCCCNS0071.class,
            ITAITWCCCNS0075.class, ITAITWCCCNS0076.class,
            ITAITWCCCNS0079.class, ITAITWCCCNS0095.class,
            ITAITWCCCNS0096.class, ITAITWCCCNS0097.class,
            ITAITWCCCNS0098.class, ITAITWCCCNS0099.class,
            ITAITWCCCNS0114.class, ITAITWCCCNS0118.class,
            ITAITWCCCNS0120.class, ITAITWCCCNS0121.class,
            ITAITWCCCNS0130.class, ITAITWCCCNS0131.class, ITAITWCCCNS0132.class}, min = 1, message = "ReturnPaymentAmt cannot be empty")
    String getReturnPaymentAmt();

    void setReturnPaymentAmt(String ReturnPaymentAmt);

    @NotNull(groups = { FINFIWCCSPL0024.class, NLDNLWCCCNS0022.class,
            FRAFRWCCCNS0054.class, FRAFRWCCCNS0059.class,
            FRAFRWCCCNS0060.class, FRAFRWCCCNS0061.class,
            FRAFRWCCCNS0062.class, FRAFRWCCCNS0071.class,
            FINFIWCCSPL0024.class, NLDNLWCCCNS0022.class,
            ESPESWCCCNS0045.class, SWESVWCCCNS0027.class,
            DEUDEWCCCNS0030.class, ITAITWCCCNS0088.class,
            ITAITWCCCNS0089.class, ITAITWCCCNS0112.class }, message = "MinimumDue is null")
    @OtherValidation
    @Size(groups = { FINFIWCCSPL0024.class, NLDNLWCCCNS0022.class,
            FRAFRWCCCNS0054.class, FRAFRWCCCNS0059.class,
            FRAFRWCCCNS0060.class, FRAFRWCCCNS0061.class,
            FRAFRWCCCNS0062.class, FRAFRWCCCNS0071.class,
            FINFIWCCSPL0024.class, NLDNLWCCCNS0022.class,
            ESPESWCCCNS0045.class, SWESVWCCCNS0027.class,
            DEUDEWCCCNS0030.class, ITAITWCCCNS0088.class,
            ITAITWCCCNS0089.class, ITAITWCCCNS0112.class }, min = 1, message = "MinimumDue cannot be empty")
    String getMinimumDue();

    void setMinimumDue(String MinimumDue);

    @NotNull(groups = { NLDNLWCCCNS0001.class, NLDNLWCCCNS0002.class,
            NLDNLWCCCNS0003.class, NLDNLWCCCNS0004.class,
            NLDNLWCCCNS0005.class, NLDNLWCCCNS0006.class,
            NLDNLWCCCNS0007.class, NLDNLWCCCNS0008.class,
            NLDNLWCCCNS0009.class, NLDNLWCCCNS0010.class,
            NLDNLWCCCNS0011.class, NLDNLWCCCNS0012.class,
            NLDNLWCCCNS0013.class, NLDNLWCCCNS0014.class,
            NLDNLWCCCNS0015.class, NLDNLWCCCNS0016.class,
            NLDNLWCCCNS0017.class, NLDNLWCCCNS0018.class,
            NLDNLWCCCNS0019.class, NLDNLWCCCNS0020.class,
            NLDNLWCCCNS0021.class, NLDNLWCCCNS0022.class,
            NLDNLWCCCNS0023.class, NLDNLWCCCNS0024.class }, message = "Salutation is null")
    @Size(groups = { NLDNLWCCCNS0001.class, NLDNLWCCCNS0002.class,
            NLDNLWCCCNS0003.class, NLDNLWCCCNS0004.class,
            NLDNLWCCCNS0005.class, NLDNLWCCCNS0006.class,
            NLDNLWCCCNS0007.class, NLDNLWCCCNS0008.class,
            NLDNLWCCCNS0009.class, NLDNLWCCCNS0010.class,
            NLDNLWCCCNS0011.class, NLDNLWCCCNS0012.class,
            NLDNLWCCCNS0013.class, NLDNLWCCCNS0014.class,
            NLDNLWCCCNS0015.class, NLDNLWCCCNS0016.class,
            NLDNLWCCCNS0017.class, NLDNLWCCCNS0018.class,
            NLDNLWCCCNS0019.class, NLDNLWCCCNS0020.class,
            NLDNLWCCCNS0021.class, NLDNLWCCCNS0022.class,
            NLDNLWCCCNS0023.class, NLDNLWCCCNS0024.class }, min = 1, message = "Salutation cannot be empty")
    String getLtrRecipSalnDs();
"""
	}
}
