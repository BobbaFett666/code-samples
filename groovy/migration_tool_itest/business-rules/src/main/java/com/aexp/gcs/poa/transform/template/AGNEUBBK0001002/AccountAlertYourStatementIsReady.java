package com.aexp.gcs.poa.transform.template.AGNEUBBK0001002;

import com.aexp.gcs.poa.transform.feeder.BBK.DynamicSubjectAndFields;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

public class AccountAlertYourStatementIsReady extends AGNEUBBK0001002 {

	@Override
	public void execute(EnvelopeType envelope) {
		DynamicSubjectAndFields.add(envelope, "Account Alert: Your ", " Statement is Ready");
	}

}
