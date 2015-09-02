/*********************************************************************************************************************

 * Modification Log:

 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         	Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         July 16, 2014    Vijay Kumar     		Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/
package com.aexp.gcs.poa.custom.constraint;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom annotation to check if required number of AddressLine lines are empty
 *******************************************************************************/
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidateAddressLines.class)
@Documented

public @interface AddressLinesNotEmpty {

	String message() default "{com.aexp.gcs.poa.custom.constraint.AddressLinesNotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    int maxLinesToValidate() default 0;

}
