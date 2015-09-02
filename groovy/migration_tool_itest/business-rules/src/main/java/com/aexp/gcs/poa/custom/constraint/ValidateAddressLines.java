/*********************************************************************************************************************

 * Modification Log:

 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         	Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         july 16, 2014    Vijay Kumar     		Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/
package com.aexp.gcs.poa.custom.constraint;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * validator to check if required number of address lines in empty or not
 *******************************************************************************/

public class ValidateAddressLines implements ConstraintValidator<AddressLinesNotEmpty, List<String>> {

	private int maxLinesToValidate ;

	/**Method to get the no of lines to be validated
	 *
	 */
	public void initialize(AddressLinesNotEmpty constraintAnnotation) {
        this.maxLinesToValidate = constraintAnnotation.maxLinesToValidate();
    }

	/**Method to return true or false based upon the no. of lines to validate
	 *
	 */
	 public boolean isValid(List<String> addressLines, ConstraintValidatorContext constraintContext) {

		 try{
			 for(int i=0;i<maxLinesToValidate;i++){
				 String addressLine = addressLines.get(i);
				 if(addressLine==null || "".equalsIgnoreCase(addressLine.trim()))
					 return false;
			 }
		 }catch(ArrayIndexOutOfBoundsException exp){
			 return false;
		 }
		 catch(IndexOutOfBoundsException iExp){
			 return false;
		 }

		 return true;
	 }

}
