package com.aexp.gcs.poa.transform.template.ABCABCXY0001001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aexp.gcs.schema.sendcorr.CommunicationVariablesType;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

/**
 * This class is an example of how a template level rule should be written.<br>
 * <br>
 * Rule should extend template if it has to be execute at template level,
 * Extends feeder if rule has to be executed for all templates inside feeder.
 * Similarly for LOB and Base.
 * 
 */
public class ChangeVariableValueRuleExample extends ABCABCXY0001001 {

	private static final Logger LOG = LoggerFactory
			.getLogger(ChangeVariableValueRuleExample.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aexp.gcs.poa.transform.AbstractTransformation#execute(com.aexp.gcs
	 * .schema.sendcorr.EnvelopeType)
	 */
	@Override
	public void execute(EnvelopeType envelope) {

		CommunicationVariablesType communicationVariables = getCommunicationVariables(envelope);
		if (communicationVariables != null) {
			/**
			 * Always get the Dynamic Values from dto. Each feeder will have its
			 * own DTO defined. DTO will hold getter and setters of all the
			 * variables corresponding to a feeder.
			 */
			com.aexp.gcs.poa.validate.feeder.ABC dto = communicationVariables
					.getDto(com.aexp.gcs.poa.validate.feeder.ABC.class);

			String value = dto.getVariable1();
			String changedVariableValue = null;
			if (null != value) {

				changedVariableValue = value + "Changed";
			}

			LOG.info(
					"Rule ChangeVariableValueRuleExample fired and changed Variable1 from {} to {}",
					value, changedVariableValue);
			dto.setVariable1(changedVariableValue);
		}

	}

}
