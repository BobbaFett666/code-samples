/*********************************************************************************************************************
 * Modification Log:
 * -------------------------------------------------------------------------------------------------------------------
 * Version      Date            Modified By         Description
 * -------------------------------------------------------------------------------------------------------------------
 * 0.00         Jun 03, 2014    Kapil Saini     Initial Version
 * -------------------------------------------------------------------------------------------------------------------
 ********************************************************************************************************************/
package com.aexp.gcs.poa.validate;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Configurable;

import com.aexp.gcs.schema.sendcorr.CommunicationInformationType;
import com.aexp.gcs.schema.sendcorr.RequestType;

/**
 * Custom Constraint which is check if the time to deliver has expired & record
 * needs to be purged.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTimeToDelivery.Validate.class)
public @interface ValidTimeToDelivery {

	String message() default "{com.aexp.gcs.TemplateXMLMissing}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Configurable
	public class Validate implements ConstraintValidator<ValidTimeToDelivery, RequestType> {

		// private ValidTimeToDelivery validTemplate;

		public void initialize(ValidTimeToDelivery constraintAnnotation) {
			// validTemplate = constraintAnnotation;
		}

		public boolean isValid(RequestType request, ConstraintValidatorContext context) {
			boolean valid = true;
			CommunicationInformationType communicationInformation = request.getCommunicationInformation();

			Date deliveryDate = communicationInformation.getTimeToDeliver();
			String purgeRequired = null;

			if (null != request.getServiceDetails() && null != request.getServiceDetails().getServiceFeatures()) {
				purgeRequired = request.getServiceDetails().getServiceFeatures().getPurge();
			}

			if (null != purgeRequired && null != deliveryDate && purgeRequired.equalsIgnoreCase("Y")) {
				Calendar calendar = Calendar.getInstance();
				Date now = calendar.getTime();

				if (now.compareTo(deliveryDate) > 0) {
					context.buildConstraintViolationWithTemplate("{com.aexp.gcs.RecordExpired}")
							.addConstraintViolation();
					valid = false;
				}
			}

			return valid;
		}
	}

}
