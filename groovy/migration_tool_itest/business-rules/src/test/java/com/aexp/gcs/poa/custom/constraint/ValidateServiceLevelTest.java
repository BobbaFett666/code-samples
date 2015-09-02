/**********************************************************************************************************************

 * Modification Log:

 * --------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         	Description
 * --------------------------------------------------------------------------------------------------------------------
 * 0.00         Aug 20, 2014    Vijay Kumar     		Initial Version
 * --------------------------------------------------------------------------------------------------------------------
 *********************************************************************************************************************/
package com.aexp.gcs.poa.custom.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit for ValidateServiceDetails custom constraint
 *********************************************************************************************************************/
public class ValidateServiceLevelTest {

	private ValidateServiceDetails.Validate validate = null;
	private ConstraintValidatorContext context = null;
	private ValidateServiceDetails annotation = null;

	@Before
	public void setUp() throws Exception {
		validate = new ValidateServiceDetails.Validate();
		context = mock(ConstraintValidatorContext.class);
		annotation = mock(ValidateServiceDetails.class);
		when(annotation.packageName()).thenReturn("com.aexp.gcs.poa.validate.template");
		validate.initialize(annotation);
	}

	/**
	 * The method will verify the template exist in GCS
	 * return true as template exist.
	 */
	@Test
	public void testExistingTemplate() {
		Boolean status = validate.isValid("ABCABCXY0001001", context);
		assertTrue("Passed", status);

	}

	/**
	 * The method will verify the template exist in GCS
	 * return false as template doesn't exist.
	 */
	@Test
	public void testNonTemplate() {
		Boolean status = validate.isValid("INVALIDTEMPLATE", context);
		assertFalse("Failed", status);

	}
}
