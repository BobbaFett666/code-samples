package com.aexp.gcs.poa.transform.template.AGNEUBBK0001001;

import com.aexp.gcs.poa.transform.feeder.BBK.DynamicSubjectAndFields;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

public class YourStatementIsReady extends AGNEUBBK0001001 {

	@Override
	public void execute(EnvelopeType envelope) {
		DynamicSubjectAndFields.add(envelope, "Your ", " Statement is Ready");
	}

}
