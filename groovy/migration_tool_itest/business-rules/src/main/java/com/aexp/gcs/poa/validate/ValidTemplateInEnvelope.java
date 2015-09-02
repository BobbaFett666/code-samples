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

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.aexp.gcs.poa.dto.Template;
import com.aexp.gcs.poa.loader.TransactionDataLoader;
import com.aexp.gcs.schema.sendcorr.CommunicationInformationType;
import com.aexp.gcs.schema.sendcorr.RequestType;

/**
 * Custom Constraint which is defined to valdiate
 * -- If the TemplateId is active and support the language   
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTemplateInEnvelope.Validate.class)
public @interface ValidTemplateInEnvelope {

	String message() default "{com.aexp.gcs.TemplateXMLMissing}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Configurable
	public class Validate implements ConstraintValidator<ValidTemplateInEnvelope, RequestType> {

		private static final Logger LOG = LoggerFactory.getLogger(Validate.class);
		@Autowired
		private TransactionDataLoader loader;

		// private ValidTemplateInEnvelope validTemplate;

		public void initialize(ValidTemplateInEnvelope constraintAnnotation) {
			// validTemplate = constraintAnnotation;
		}

		public boolean isValid(RequestType request, ConstraintValidatorContext context) {
			boolean valid = false;
			if (request != null) {
				CommunicationInformationType communicationInformation = request.getCommunicationInformation();
				String templateID = communicationInformation != null ? communicationInformation.getTemplateID() : null, systemID = request
						.getFeederDetails() != null ? request.getFeederDetails().getFeederSystemID() : null, locale = communicationInformation != null ? communicationInformation
						.getPreferredLanguageCode() : null, communicationType = communicationInformation != null
						&& communicationInformation.getDeliveryInformation() != null ? communicationInformation
						.getDeliveryInformation().getDeliverMethod() : null;
				Template template = null;
				try {
					if (templateID == null || systemID == null) {
						if (templateID == null) {
							context.buildConstraintViolationWithTemplate("{javax.validation.constraints.Null.message}")
									.addNode("communicationInformation").addNode("templateID").addConstraintViolation();
						}
						if (systemID == null) {
							context.buildConstraintViolationWithTemplate("{javax.validation.constraints.Null.message}")
									.addNode("feederDetails").addNode("feederSystemID").addConstraintViolation();
						}
					} else {
						template = loader.getTemplate(templateID, systemID);
						if (template == null
								|| !template.getSpecificTemplateDetailsGroup().containsKey(
										locale + "," + communicationType)) {
							context.buildConstraintViolationWithTemplate("{com.aexp.gcs.TemplateInactive}")
									.addNode("communicationInformation").addNode("templateID").addConstraintViolation();
						} else {
							valid = true;
						}
					}
				} catch (Throwable e) {
					LOG.error("Parsing error, validation will be marked failed.", e);
				}
			}
				
			return valid;
		}
	}

}
