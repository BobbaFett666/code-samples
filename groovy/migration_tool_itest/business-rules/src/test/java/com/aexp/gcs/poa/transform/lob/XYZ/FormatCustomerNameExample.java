package com.aexp.gcs.poa.transform.lob.XYZ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aexp.gcs.schema.sendcorr.EnvelopeType;
import com.aexp.gcs.schema.sendcorr.NameDetailsType;

/**
 * Line of Business (LOB) Specific Rules We can use OriginatorName from
 * HeaderType In SendCorr Schema to pass these details.<BR>
 * 
 * This rule will execute on all the templates of all the feeder System falling
 * under LOB.<BR><BR>
 * 
 * Right rules in exact package where you need to execute. i.e at LOB, Feeder or template.
 * 
 */
public class FormatCustomerNameExample extends XYZ {

	private static final Logger LOG = LoggerFactory
			.getLogger(FormatCustomerNameExample.class);

	/*Expecting that customer name is being sent by XYZ first name in SendCorr 4*/

	/* (non-Javadoc)
	 * @see com.aexp.gcs.poa.transform.AbstractTransformation#execute(com.aexp.gcs.schema.sendcorr.EnvelopeType)
	 */
	@Override
	public void execute(EnvelopeType envelope) {
		LOG.info("LOB specific Rules");

		NameDetailsType customerName = getRecipientContactNameDetailsOrNull(envelope);

		if (null != customerName) {
			String firstName = customerName.getFirstName();
			LOG.info(firstName);
			String result = firstName.toUpperCase();
			LOG.info(
					"Rule FormatCustomerName fired and changed First Name from {} to {}",
					firstName, result.toString());

			customerName.setFirstName(result);
		}
	}

}
