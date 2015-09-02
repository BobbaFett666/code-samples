/**********************************************************************************************************************

 * Modification Log:

 * --------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         	Description
 * --------------------------------------------------------------------------------------------------------------------
 * 0.00         Aug 06, 2014    Vijay Kumar     		Initial Version
 * --------------------------------------------------------------------------------------------------------------------
 *********************************************************************************************************************/
package com.aexp.gcs.poa.custom.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom annotation to validate Service level details
 *********************************************************************************************************************/
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidateServiceDetails.Validate.class)
@Documented

public @interface ValidateServiceDetails {

	String message() default "{com.aexp.gcs.poa.custom.constraint.ValidateServiceDetails.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String packageName() default "NONE";
    
    public class Validate implements ConstraintValidator<ValidateServiceDetails, String>{
    	private static final Logger LOG = LoggerFactory.getLogger(Validate.class);
    	
    	private String packageName ;
		public void initialize(ValidateServiceDetails constraintAnnotation) {
			this.packageName = constraintAnnotation.packageName();
			
		}

		public boolean isValid(String value, ConstraintValidatorContext context) {
			LOG.debug("Service Level Validation for value "+value);
			
			if(value==null || "".equals(value))
				return false;
			try{
				Class.forName(packageName+"."+value);
				return true;
			}catch (ClassNotFoundException exp){
				return false;
			}
		}
    	
    }
}
