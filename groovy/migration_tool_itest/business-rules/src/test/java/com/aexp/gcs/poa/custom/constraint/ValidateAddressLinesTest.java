/*********************************************************************************************************************

 * Modification Log:
 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         July 23, 2014    Raj Vibhav         Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/

package com.aexp.gcs.poa.custom.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ValidateAddressLines This class will test the functionality of
 * isValid method of ValidateAddressLines which will return true if
 * maximumAddressLines which is required is actually passed in the input xml and
 * false viceversa.
 */
public class ValidateAddressLinesTest {

	private ValidateAddressLines validate = null;
	private ConstraintValidatorContext context = null;
	private AddressLinesNotEmpty annotation = null;

	@Before
	public void setUp() throws Exception {
		validate = new ValidateAddressLines();
		context = mock(ConstraintValidatorContext.class);
		annotation = mock(AddressLinesNotEmpty.class);
		when(annotation.maxLinesToValidate()).thenReturn(2);
		validate.initialize(annotation);
	}

	/**
	 * The method will verify the addressLine passed in the input and will
	 * return as it is more than expected value true.
	 */
	@Test
	public void testcheckAddressLineSuccess() {
		List<String> addressLines = new ArrayList<String>();
		addressLines.add("House 680");
		addressLines.add("Domlur");
		addressLines.add("Bangalore");
		Boolean status = validate.isValid(addressLines, context);
		assertTrue("This will Succeed!!", status);

	}

	/**
	 * The method will verify addressLine passed in the input and will return
	 * false as it is less than than the required value.
	 */
	@Test
	public void testcheckAddressLineReject() {
		List<String> addressLines = new ArrayList<String>();
		addressLines.add("House 680");
		Boolean status = validate.isValid(addressLines, context);
		assertFalse("This will Fail!!", status);

	}

}
